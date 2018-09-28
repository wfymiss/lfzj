package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.UserHouseInfo;
import com.ovov.lfzj.base.dao.UserHouseDao;
import com.ovov.lfzj.event.HouseEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户拥有房间弹出框
 * Created by Administrator on 2017/10/28.
 */

public class HousePopup extends PopupWindow {
    private RecyclerView recyclerView;
    private View mview;   //视图
    private Context context;
    private HousePopupAdapter adapter;;
    private List<UserHouseInfo> list=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    public HousePopup(Context mcontext){
        this.context=mcontext;
        init(context);   //布局
        setPopupWindow();   //布局属性
    }

    private void init(Context ncontext) {
        adapter=new HousePopupAdapter(ncontext);    // 初始化认证的小区适配器
        getUserHouse();   // 获取用户房间数据源
        LayoutInflater infalter = LayoutInflater.from(ncontext);
        mview=infalter.inflate(R.layout.house_popup,null);
        recyclerView= (RecyclerView) mview.findViewById(R.id.house_popup_recy);
        linearLayoutManager = new LinearLayoutManager(ncontext, LinearLayoutManager.VERTICAL, false);    //竖向刷新
        recyclerView.setLayoutManager(linearLayoutManager);  //recycler 刷新
        recyclerView.setAdapter(adapter);
        adapter.setHouseItemListener(new HousePopupAdapter.HouseItemListener() {
            @Override
            public void HouseItemClick(int position, String house_id, String house_name, String sub_id, String sub_name) {
                EventBus.getDefault().post(new HouseEvent(house_id,house_name,sub_id,sub_name));
                dismiss();   // 选择小区后关闭弹出框
            }
        });
    }

    private void setPopupWindow() {
        this.setContentView(mview); //设置View
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);  //弹出窗宽度
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT); //弹出高度
        this.setFocusable(true);  // 弹出窗可触摸、获取焦点
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));   //设置背景透明
        this.setAnimationStyle(R.style.mypopupwindow_anim_style);   //弹出动画
        mview.setOnTouchListener(new View.OnTouchListener() {   //如果触摸位置在窗口外面则销毁
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mview.findViewById(R.id.house_popup_recy).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();   // 销毁
                        try {
                            HousePopup.this.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
    }

    private void getUserHouse() {
        list=new UserHouseDao(context).SelectHouseAll();   // 所有房间信息
        adapter.setVillageData(list);
    }
}

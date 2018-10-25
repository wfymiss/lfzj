package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.ActivityTitleInfo;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.BuildingListEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 小区活动标题弹出框
 * Created by Administrator on 2017/10/30.
 */

public class BuildingListPopup extends PopupWindow {
    private RecyclerView recyclerView;
    private View mview;   //视图
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private ActivityTitleInfo info;  // 活动标题实体类
    private BuildingListTitleAdapter adapter;
    private List<ActivityTitleInfo> list=new ArrayList<>();
    private String activity_category[];  // 活动标题
    private String category_id[];    // 活动标题ID

    public BuildingListPopup(Context mcontext){
        this.context=mcontext;
        init(context);   //布局
        setPopupWindow();   //布局属性
    }

    private void init(Context ncontext) {
        adapter=new BuildingListTitleAdapter(ncontext);    // 初始化认证的小区适配器
        activityCategory();  // 获取活动标题
        LayoutInflater infalter = LayoutInflater.from(ncontext);
        mview=infalter.inflate(R.layout.popup_activity_title,null);   // 弹出框页面视图
        recyclerView= (RecyclerView) mview.findViewById(R.id.activity_title_recy);
        linearLayoutManager = new LinearLayoutManager(ncontext, LinearLayoutManager.VERTICAL, false);    //竖向刷新
        recyclerView.setLayoutManager(linearLayoutManager);  //recycler 刷新
        recyclerView.setAdapter(adapter);
        adapter.setTitleItemListener(new BuildingListTitleAdapter.TitleItemListener() {
            @Override
            public void titleItemClick(int position, String building, String type_id) {
                EventBus.getDefault().post(new BuildingListEvent(building,"0","1"));
                RxBus.getDefault().post(new BuildingListEvent(building,"0","1"));
                dismiss();
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
                dismiss();    // 点击弹出框外部界面关闭弹出框
                return true;
            }
        });
    }

    // 获取活动标题
    private void activityCategory() {
        SharedPreferences spf=context.getSharedPreferences("building", Context.MODE_PRIVATE);
        String category=spf.getString("building_id","");
        activity_category=category.split(",");   //转换成数组
        for (int i=0;i<activity_category.length;i++){
            info=new ActivityTitleInfo();
            info.setTitle(activity_category[i]);
            list.add(info);
        }
        adapter.setTitleData(list);    // 活动标题集合
    }
}

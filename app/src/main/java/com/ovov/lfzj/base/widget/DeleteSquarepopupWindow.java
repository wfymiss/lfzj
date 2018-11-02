package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.DeleteEvent;
import com.ovov.lfzj.event.ShareSquareEvent;
import com.ovov.lfzj.event.TransmitSquareEvent;

/**
 * 刘永毅
 * 拍照 popupwindow 方法类
 * Created by Administrator on 2017/7/14.
 */

public class DeleteSquarepopupWindow extends PopupWindow {
    private TextView btnPhoto,btnSelect,btnCancel;    //popup控件
    private View mview;   //视图

    public DeleteSquarepopupWindow(Context context) {
        super(context);
        init(context);   //布局
        setPopupWindow();   //布局属性
    }

    /**
     * 初始化布局
     */
    private void init(Context context) {
        LayoutInflater infalter= LayoutInflater.from(context);
        mview=infalter.inflate(R.layout.delete_select,null);    //绑定布局
        //获取控件
        btnPhoto= (TextView) mview.findViewById(R.id.id_btn_take_photo);
        btnSelect= (TextView) mview.findViewById(R.id.id_btn_select);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new DeleteEvent());
                dismiss();
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setPopupWindow() {
        this.setContentView(mview); //设置View
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);  //弹出窗宽度
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT); //弹出高度
        this.setFocusable(true);  //弹出窗可触摸
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));   //设置背景透明
        this.setAnimationStyle(R.style.mypopupwindow_anim_style);   //弹出动画
        mview.setOnTouchListener(new View.OnTouchListener() {   //如果触摸位置在窗口外面则销毁
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height=mview.findViewById(R.id.id_pop_layout).getTop();
                int y= (int) event.getY();
                if (event.getAction()== MotionEvent.ACTION_UP){
                    if (y<height){
                        dismiss();   //销毁
                    }
                }
                return true;
            }
        });
    }


}

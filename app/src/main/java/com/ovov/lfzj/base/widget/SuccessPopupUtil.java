package com.ovov.lfzj.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ovov.lfzj.R;

/**
 * 成功提醒弹出框
 * Created by lyy on 2017/8/30.
 */

public class SuccessPopupUtil extends Dialog {

    public SuccessPopupUtil(@NonNull Context context) {
        super(context);
    }

    public SuccessPopupUtil(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private String title,content;

        public String getContent() {     // 获取提醒内容
            return content;
        }

        public void setContent(String re_title, String re_content) {   // 提醒标题、内容
            this.title=re_title;
            this.content = re_content;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public SuccessPopupUtil Create(){
            final TextView remind_title,remind_text,rem_confirm;
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SuccessPopupUtil dialog=new SuccessPopupUtil(context, R.style.Dialog);         // 弹框属性
            View view=inflater.inflate(R.layout.popup_success_pop_item,null);
            // 弹出背景占用界面
            dialog.addContentView(view,new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));   // 全屏显示
            dialog.setContentView(view);
            remind_title= (TextView) view.findViewById(R.id.activity_sign_up);      // 标题
            remind_text= (TextView) view.findViewById(R.id.activity_sign_point);   // 内容
            rem_confirm= (TextView) view.findViewById(R.id.activity_confirm);      // 确认按钮

            // 显示弹出框内容
            remind_title.setText(title);
            remind_text.setText(content);     // 活动报名提示内容
            // 弹出框确定按钮
            rem_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTextViewClickListener!=null){
                        onTextViewClickListener.onTextClick();
                    }
                }
            });

            return dialog;
        }
        // 弹出框点击事件监听
        private OnTextViewClickListener onTextViewClickListener;

        public interface OnTextViewClickListener{
            void onTextClick();
        }
        public void setOnTextViewClickListener(OnTextViewClickListener listener){
            this.onTextViewClickListener=listener;
        }
    }
}

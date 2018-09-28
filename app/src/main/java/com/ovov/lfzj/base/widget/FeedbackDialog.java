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
 * 反馈弹出框
 * Created by lyy on 2017/10/23.
 */

public class FeedbackDialog extends Dialog {
    public FeedbackDialog(@NonNull Context context) {
        super(context);
    }

    public FeedbackDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class BuilderLog{
        private Context context;
        private String content;

        public BuilderLog (Context context){
            this.context=context;
        }
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public FeedbackDialog feedbackDialog(){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final FeedbackDialog dialog=new FeedbackDialog(context, R.style.Dialog);    // 弹出款样式
            View view= inflater.inflate(R.layout.layout_opendoor_log, null);
            // 弹出背景占用界面
            dialog.addContentView(view,new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(view);
            TextView feedConten= (TextView) view.findViewById(R.id.feedback_log);
            feedConten.setText(getContent());   // 弹出框内容
            return dialog;
        }
    }
}

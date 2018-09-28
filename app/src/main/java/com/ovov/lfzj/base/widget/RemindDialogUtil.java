package com.ovov.lfzj.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ovov.lfzj.R;

/**
 * 提醒框
 * Created by lyy on 2018/4/14.
 */

public class RemindDialogUtil extends Dialog {

    public RemindDialogUtil(@NonNull Context context) {
        super(context);
    }

    public RemindDialogUtil(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RemindDialogUtil(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private String title = null;       // 弹框标题

        public Builder(Context mContext) {
            this.context = mContext;
        }

        public void setContent(String re_title) {     // 弹框传值标题
            this.title = re_title;
        }

        public RemindDialogUtil Create() {
            final TextView rem_Title, rem_cancel, rem_confirm;
            final RemindDialogUtil dialog = new RemindDialogUtil(context, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_remind_select_item, null);
            // 弹出背景占用界面
            dialog.addContentView(view, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));   // 全屏显示
            dialog.setContentView(view);    // 绑定页面

            rem_Title = (TextView) view.findViewById(R.id.pop_delete_title);
            rem_cancel = (TextView) view.findViewById(R.id.pop_delete_cancel);
            rem_confirm = (TextView) view.findViewById(R.id.pop_delet_confirm);
            rem_Title.setText(title);

            // 取消按钮
            rem_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();   // 关闭弹框
                }
            });
            // 确认按钮
            rem_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (confirmListener!=null){
                        confirmListener.onClickListener();
                    }
                }
            });
            return dialog;
        }
        // 定义确认点击shijan事件
        private ConfirmClickListener confirmListener;

        public interface ConfirmClickListener{
            void onClickListener();
        }
        public void setConfirmListener(ConfirmClickListener listener){
            this.confirmListener=listener;
        }
    }
}
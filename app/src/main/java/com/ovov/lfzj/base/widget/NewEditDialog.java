package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.event.LstSendEvent;
import com.ovov.lfzj.event.SendEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kaite on 2018/9/18.
 */

public class NewEditDialog extends BaseDialog {
    int id ;
    public NewEditDialog(Context context, int id) {
        super(context);
        this.id = id;
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null, false);
        TextView tvSend = view.findViewById(R.id.tv_send);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final EditText etContent = view.findViewById(R.id.et_comment);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString())){
                    UIUtils.displayToast(UIUtils.getString(R.string.text_input_comment));
                    return;
                }

                EventBus.getDefault().post(new LstSendEvent(etContent.getText().toString(),id));
                //RxBus.getDefault().post(new SendEvent(etContent.getText().toString(),id));
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }

}

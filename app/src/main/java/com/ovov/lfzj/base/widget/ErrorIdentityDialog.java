package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.ErrorDialogCancelEvent;

import org.w3c.dom.Text;

/**
 * Created by kaite on 2018/9/11.
 */

public class ErrorIdentityDialog extends BaseDialog {
    public ErrorIdentityDialog(Context context) {
        super(context);

    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_error_identity,null,false);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvReinput = view.findViewById(R.id.tv_reinput);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new ErrorDialogCancelEvent());
                dismiss();
            }
        });
        tvReinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}

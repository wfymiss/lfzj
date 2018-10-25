package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;

/**
 * Created by kaite on 2018/10/25.
 */

public class CancelDispathDialog extends BaseDialog{
    public CancelDispathDialog(Context context) {
        super(context);
    }
    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }
    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dispath_cancel,null,false);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}

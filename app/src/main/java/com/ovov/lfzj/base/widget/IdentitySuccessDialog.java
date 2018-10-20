package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;

/**
 * Created by kaite on 2018/10/19.
 */

public class IdentitySuccessDialog extends BaseDialog {
    public IdentitySuccessDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.update_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_identity_success,null,false);
        TextView tvTohome = view.findViewById(R.id.tv_tohome);
        tvTohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCancelable(false);
        return view;
    }
}

package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;

/**
 * Created by kaite on 2018/10/9.
 */

public class SucceseDialog extends BaseDialog {
    public SucceseDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.update_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.succese_item, null, false);
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                dismiss();

            }
        });
        return view;
    }

}

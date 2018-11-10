package com.ovov.lfzj.base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.DeleteFamilyEvent;

/**
 * Created by kaite on 2018/10/9.
 */

public class AdviceDialog extends BaseDialog {

    public AdviceDialog(Context context) {
        super(context);
    }


    @Override
    protected int getDialogStyleId() {
        return R.style.update_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.advice_flag_item, null, false);
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}

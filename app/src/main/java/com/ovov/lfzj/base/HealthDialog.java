package com.ovov.lfzj.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;

/**
 * Created by kaite on 2018/10/9.
 */

public class HealthDialog extends BaseDialog {
    public HealthDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.update_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.health_dialog, null, false);
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                dismiss();

            }
        });
        view.findViewById(R.id.updata_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCancelable(false);
        return view;
    }
}

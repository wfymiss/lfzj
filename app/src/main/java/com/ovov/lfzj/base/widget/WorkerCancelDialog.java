package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;

/**
 * Created by kaite on 2018/10/25.
 */

public class WorkerCancelDialog extends BaseDialog {

    private String reason;

    public WorkerCancelDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_worker_cancel, null, false);
        TextView tvReason1 = view.findViewById(R.id.tv_reason1);
        TextView tvReason2 = view.findViewById(R.id.tv_reason2);
        ImageView ivReason1 = view.findViewById(R.id.iv_reason1);
        ImageView ivReason2 = view.findViewById(R.id.iv_reason2);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        LinearLayout linReason1 = view.findViewById(R.id.lin_reason1);
        LinearLayout linReason2 = view.findViewById(R.id.lin_reason2);
        EditText etRemarks = view.findViewById(R.id.et_mark);
        ivReason1.setSelected(true);
        linReason1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = tvReason1.getText().toString();
                ivReason1.setSelected(true);
            }
        });
        linReason2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = tvReason2.getText().toString();
                ivReason2.setSelected(true);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}

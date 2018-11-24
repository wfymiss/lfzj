package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.DetailWorkerCancelEvent;
import com.ovov.lfzj.event.ListWorkerCancelEvent;
import com.ovov.lfzj.event.OwnerCancelEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kaite on 2018/10/25.
 */

public class OwnerCancelDialog extends BaseDialog {


    private String reason;

    public OwnerCancelDialog(Context context) {
        super(context);

    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_owner_cancel, null, false);
        TextView tvReason1 = view.findViewById(R.id.tv_reason1);
        TextView tvReason2 = view.findViewById(R.id.tv_reason2);
        TextView tvReason3 = view.findViewById(R.id.tv_reason3);

        ImageView ivReason1 = view.findViewById(R.id.iv_reason1);
        ImageView ivReason2 = view.findViewById(R.id.iv_reason2);
        ImageView ivReason3 = view.findViewById(R.id.iv_reason3);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        LinearLayout linReason1 = view.findViewById(R.id.lin_reason1);
        LinearLayout linReason2 = view.findViewById(R.id.lin_reason2);
        LinearLayout linreason3 = view.findViewById(R.id.lin_reason3);
        EditText etRemarks = view.findViewById(R.id.et_mark);
        ivReason1.setSelected(true);
        reason = tvReason1.getText().toString();
        linReason1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = tvReason1.getText().toString();
                ivReason1.setSelected(true);
                ivReason2.setSelected(false);
                ivReason3.setSelected(false);
            }
        });
        linReason2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = tvReason2.getText().toString();
                ivReason2.setSelected(true);
                ivReason1.setSelected(false);
                ivReason3.setSelected(false);
            }
        });
        linreason3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = tvReason3.getText().toString();
                ivReason2.setSelected(false);
                ivReason1.setSelected(false);
                ivReason3.setSelected(true);
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
                RxBus.getDefault().post(new OwnerCancelEvent(reason,etRemarks.getText().toString()));
                dismiss();
            }
        });
        return view;
    }
}

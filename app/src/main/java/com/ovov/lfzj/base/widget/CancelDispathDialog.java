package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.DetailCancelDispathEvent;
import com.ovov.lfzj.event.ListCancelDispathEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kaite on 2018/10/25.
 */

public class CancelDispathDialog extends BaseDialog {
    private int type;//1是从列表进入2是从详情进入
    private int posistion;//工单下标（从详情页进入可瞎传）

    public CancelDispathDialog(Context context, int type,int posistion) {
        super(context);
        this.type = type;
        this.posistion = posistion;

    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dispath_cancel, null, false);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        EditText mEtRemark = view.findViewById(R.id.et_mark);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtRemark.getText().toString())){
                    showToast("请输入备注");
                    return;
                }
                if (type == 1)
                    EventBus.getDefault().post(new ListCancelDispathEvent(mEtRemark.getText().toString(),posistion));
                else if (type == 2){
                    RxBus.getDefault().post(new DetailCancelDispathEvent(mEtRemark.getText().toString()));
                }
                dismiss();
            }
        });
        return view;
    }
}

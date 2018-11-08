package com.ovov.lfzj.base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.DeleteFamilyEvent;
import com.ovov.lfzj.event.HouseEvent;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.event.SquareDetailIdentityEvent;

/**
 * Created by kaite on 2018/10/9.
 */

public class DelectDialog extends BaseDialog {

    private String user;
    private String house;
    private String relative_id;
    private int pos;
    public DelectDialog(Context context) {
        super(context);
    }

    public DelectDialog(Activity mActivity, String user_id, String houses_id, String relative_id,int pos) {

        super(mActivity);
        this.relative_id=relative_id;
        this.house=houses_id;
        this.user=user_id;
        this.pos=pos;
    }


    @Override
    protected int getDialogStyleId() {
        return R.style.update_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.delect_flag_item, null, false);
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                RxBus.getDefault().post(new DeleteFamilyEvent(user,house,relative_id,pos));
                dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}

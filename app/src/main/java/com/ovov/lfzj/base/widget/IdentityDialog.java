package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.HomeIdentityEvent;
import com.ovov.lfzj.event.MainIdentityEvent;
import com.ovov.lfzj.event.NeighbourIdentityEvent;
import com.ovov.lfzj.event.SquareDetailIdentityEvent;
import com.ovov.lfzj.event.ToIdentityEvent;
import com.ovov.lfzj.event.UserFragmentIdentityEvent;

import static com.ovov.lfzj.CatelApplication.HOME_FRAGMENT_IDENTITY;
import static com.ovov.lfzj.CatelApplication.MAIN_ACTIVITY_IDENTITY;
import static com.ovov.lfzj.CatelApplication.NEIGHBOUR_IDENTITY;
import static com.ovov.lfzj.CatelApplication.SQUARE_DETAIL_IDENTITY;
import static com.ovov.lfzj.CatelApplication.SQUARE_FRAGMENT_IDENTITY;
import static com.ovov.lfzj.CatelApplication.UAER_FRAGMENT_IDENTITY;

/**
 * Created by kaite on 2018/9/20.
 */

public class IdentityDialog extends BaseDialog {
    private int type;
    public IdentityDialog(Context context,int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_identity,null,false);
        TextView tvIdentity = view.findViewById(R.id.tv_identity);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == SQUARE_DETAIL_IDENTITY){
                    RxBus.getDefault().post(new SquareDetailIdentityEvent());
                    return;
                }
                if (type == SQUARE_FRAGMENT_IDENTITY){
                    RxBus.getDefault().post(new ToIdentityEvent());
                    return;
                }
                if (type == HOME_FRAGMENT_IDENTITY){
                    RxBus.getDefault().post(new HomeIdentityEvent());
                    return;
                }
                if (type == MAIN_ACTIVITY_IDENTITY){
                    RxBus.getDefault().post(new MainIdentityEvent());
                    return;
                }
                if (type == NEIGHBOUR_IDENTITY){
                    RxBus.getDefault().post(new NeighbourIdentityEvent());
                    return;
                }
                if (type == UAER_FRAGMENT_IDENTITY){
                    RxBus.getDefault().post(new UserFragmentIdentityEvent());
                    return;
                }


            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}

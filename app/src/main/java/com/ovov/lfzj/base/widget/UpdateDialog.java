package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.DownloadEvent;

/**
 * Created by kaite on 2018/9/25.
 */

public class UpdateDialog extends BaseDialog {
    String url;
    String content;
    public UpdateDialog(Context context, String apk_url, String upgrade_point) {
        super(context);
        this.url = apk_url;
        this.content = upgrade_point;
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.update_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update,null,false);
        ImageView ivUpdate = view.findViewById(R.id.iv_update);
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(content);
        ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                RxBus.getDefault().post(new DownloadEvent(url));
            }
        });
        setCancelable(false);
        return view;
    }
}

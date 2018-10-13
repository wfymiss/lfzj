package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkOrderConfirmActivity extends BaseActivity {

    @BindView(R.id.re_number)
    LinearLayout mReNumber;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, WorkOrderConfirmActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_work_order_confirm;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_worker_order_confirm);
        mReNumber.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.tv_revise, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_revise:
                finish();
                break;
            case R.id.tv_commit:
                break;
        }
    }

}

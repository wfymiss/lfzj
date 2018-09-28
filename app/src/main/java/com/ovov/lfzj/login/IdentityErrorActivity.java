package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.OnClick;

public class IdentityErrorActivity extends BaseActivity {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, IdentityErrorActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_identity_error;
    }

    @Override
    public void init() {

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends BaseActivity {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    public void init() {

        setTitleText("用户协议");
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

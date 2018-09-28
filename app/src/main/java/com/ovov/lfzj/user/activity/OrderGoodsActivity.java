package com.ovov.lfzj.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderGoodsActivity extends BaseActivity {
    @BindView(R.id.btn_pay)
    Button mBtnpay;
    @BindView(R.id.hmm_com_toolbar)
    Toolbar mToolbar;



    @OnClick(R.id.btn_pay)
    public void setOnClick() {
        Intent intent = new Intent(this, PayActivity.class);
        startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_particulars;
    }

    @Override
    public void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}

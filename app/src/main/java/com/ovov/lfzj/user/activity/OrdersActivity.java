package com.ovov.lfzj.user.activity;

import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;

public class OrdersActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_orders;
    }

    @Override
    public void init() {
        tvTitle.setText("我的订单");

    }

}

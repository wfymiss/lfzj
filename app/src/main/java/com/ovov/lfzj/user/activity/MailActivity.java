package com.ovov.lfzj.user.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MailActivity extends BaseActivity {
    @BindView(R.id.rl_orderxq)
    LinearLayout relativeLayout;
    @BindView(R.id.hmm_com_toolbar)
    Toolbar mToolbar;


    @OnClick(R.id.rl_orderxq)
    public void onClick() {
        Intent intent = new Intent(this, OrderedActivity.class);
        startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mail;
    }

    @Override
    public void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}

package com.ovov.lfzj.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.user.QuestionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvicesActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.suggestion)
    TextView suggestion;
    @BindView(R.id.service)
    TextView service;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, AdvicesActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void init() {
        tvTitle.setText("帮助与反馈");


    }

    @OnClick({R.id.iv_back, R.id.question, R.id.suggestion, R.id.service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.question:
                QuestionActivity.toActivity(mActivity);
                break;
            case R.id.suggestion:
                HelpActivity.toActivity(mActivity);
                break;
            case R.id.service:
                break;
        }
    }

}

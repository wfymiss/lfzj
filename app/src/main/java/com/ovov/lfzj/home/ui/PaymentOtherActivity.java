package com.ovov.lfzj.home.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.home.payment.activity.PayMentRecordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentOtherActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.web)
    WebView web;

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_other;
    }

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, PaymentOtherActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void init() {
        tvTitle.setText("代缴费");

        web.loadUrl("http://api_test.catel-link.com/v1/payment/select?user_id=" + LoginUserBean.getInstance().getUserId()+"&type=1");

        web.getSettings().setJavaScriptEnabled(true);  //加上这一行网页为响应式的
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.getSettings().setJavaScriptEnabled(true);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;   //返回true， 立即跳转，返回false,打开网页有延时
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (web != null) {
            web.clearHistory();
            web.removeAllViews();
            web.destroy();
            web = null;
        }
        super.onDestroy();

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

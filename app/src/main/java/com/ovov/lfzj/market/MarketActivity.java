package com.ovov.lfzj.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youzan.androidsdkx5.YouzanBrowser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketActivity extends BaseActivity {


    @BindView(R.id.youzan_view)
    YouzanBrowser mYouzanView;

    public static void toActivity(Context context, String url) {
        Intent intent = new Intent(context, MarketActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_market;
    }

    @Override
    public void init() {

        String url = getIntent().getStringExtra("url");
        mYouzanView.loadUrl(url);
        mYouzanView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {

                if (s.contains("https://www.youzan.com/?from_source=support"))
                    return true;

                return false;
            }
        });
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        if (mYouzanView.canGoBack()) {
            mYouzanView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mYouzanView.canGoBack()) {
            mYouzanView.goBack();
        } else {
            finish();
        }
    }
}

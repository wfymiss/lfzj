package com.ovov.lfzj.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView mWeb;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_service;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void init() {

        setTitleText("用户协议");
        WebSettings webSettings = mWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);                              // enable navigator.geolocation
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");     // enable Web Storage: localStorage, sessionStorage
        webSettings.setDomStorageEnabled(true);
        mWeb.requestFocus();
        mWeb.setScrollBarStyle(0);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        //设置不用系统浏览器打开,直接显示在当前Webview
       /* webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/
        mWeb.setWebChromeClient(new WebChromeClient());
        mWeb.loadUrl("file:////android_asset/userAgreement.html");
        //mWeb.setWebViewClient(webViewClient);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}

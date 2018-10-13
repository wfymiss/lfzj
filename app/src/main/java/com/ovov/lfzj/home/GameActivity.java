package com.ovov.lfzj.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.utils.JavaSccriptFinishActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends BaseActivity {

    @BindView(R.id.web_game)
    WebView mWebGame;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_game;
    }
    @SuppressLint("JavascriptInterface")
    @Override
    public void init() {

        WebSettings webSettings = mWebGame.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);                              // enable navigator.geolocation
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");     // enable Web Storage: localStorage, sessionStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebGame.requestFocus();
        mWebGame.setScrollBarStyle(0);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        mWebGame.setWebViewClient(webViewClient);
        mWebGame.addJavascriptInterface(new JavaSccriptFinishActivity(mActivity),"JavaScriptInterface");
        //设置不用系统浏览器打开,直接显示在当前Webview
       /* webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/
        mWebGame.loadUrl("http://game_test.catel-link.com/?playID=" + LoginUserBean.getInstance().getUserId());

    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //showLoadingDialog();
        }


        /**/
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebGame.clearView();
    }
}

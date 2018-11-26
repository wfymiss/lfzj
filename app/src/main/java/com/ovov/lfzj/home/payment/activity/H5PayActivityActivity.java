package com.ovov.lfzj.home.payment.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.PayResultEvent;
import com.ovov.lfzj.event.PaymentEvent;
import com.ovov.lfzj.home.bean.PayInfo;
import com.ovov.lfzj.home.bean.PayResult;
import com.ovov.lfzj.home.bean.PaymentPayView;
import com.ovov.lfzj.home.bean.WXPayInfo;
import com.ovov.lfzj.home.bean.WxPaySuccessResult;
import com.ovov.lfzj.home.event.RefreshEvent;
import com.ovov.lfzj.home.payment.presenter.PaymentPayPresenter;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.api.CatelApiService;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.Subscription;

public class H5PayActivityActivity extends BaseActivity implements PaymentPayView {

    @BindView(R.id.active_xq_toolbar)
    Toolbar activeXqToolbar;
    @BindView(R.id.webview)
    WebView webview;
    private String token;
    private String sub_id;
    private PaymentPayPresenter presenter;      // 定义支付方法
    private int i = 0;
    private String type;
    private String order_id;
    private String order_number;

    public static void toActivity(Context context, String type, String order_id, String order_number) {
        Intent intent = new Intent(context, H5PayActivityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("Order_id", order_id);
        bundle.putString("order_number", order_number);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void init() {

        presenter = new PaymentPayPresenter(this);

        initToolBar();
        storageTokenRead();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        order_id = bundle.getString("Order_id");
        order_number = bundle.getString("order_number");
        if (i == 0) {

        }
        String url = CatelApiService.HOST+"v1/pay/index?type=" + type + "&order_id=" + order_id + "&subdistrict_id="+LoginUserBean.getInstance().getSub_id()+"&token"+LoginUserBean.getInstance().getAccess_token();
        Log.e("url", url);
        initWeb(url);



    }

    @SuppressLint("WrongConstant")
    private void initWeb(String url) {

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);                              // enable navigator.geolocation
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");     // enable Web Storage: localStorage, sessionStorage
        webSettings.setDomStorageEnabled(true);
        webview.requestFocus();
        webview.setScrollBarStyle(0);
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
       webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
        webview.setWebViewClient(webViewClient);
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token", "");
        sub_id = spf.getString("subId", "");
    }


    private void initToolBar() {
        setSupportActionBar(activeXqToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);        //显示导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);     // 不显示子标题（禁用）
            activeXqToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getDefault().post(new PayResultEvent(order_id,type));
                    finish();
                }
            });
        }
        activeXqToolbar.setNavigationIcon(R.mipmap.ic_back_black);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webview.goBack(); // goBack()表示返回webView的上一页面
            return true;
        } else {
            RxBus.getDefault().post(new PayResultEvent(order_id,type));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // 如下方案可在非微信内部WebView的H5页面中调出微信支付
            String url = request.getUrl().toString();
            if (url.startsWith("weixin://wap/pay?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
               /* Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("Referer", "http://api_test.catel-link.com");
                view.loadUrl(url, extraHeaders);*/
                return true;
            } else if (url.startsWith("alipays://platformapi/startApp?")) {
                /*Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("Referer", "https://api.catel-link.com");
                view.loadUrl(url, extraHeaders);*/
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
        /**/
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_h5_pay_activity;
    }


    @Override
    public void onResume() {
        super.onResume();
        i = i + 1;
        if (i > 1)
            confirmPay(type,order_id,order_number);
        Log.e("onresume","onresume");

    }
    private void confirmPay(String order_type, String order_id, String order_number) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().confirmPayResult(order_type, order_id, order_number)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showError(dataResultException.errorInfo);
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        RxBus.getDefault().post(new PayResultEvent(order_id,type));
                        RxBus.getDefault().post(new PaymentEvent());
                        showToast("支付成功");
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onresume","onpause");
    }

    @Override
    public void setPayInfo(PayInfo payInfo) {

    }

    @Override
    public void setWXPayInfo(WXPayInfo wxPayInfo) {

    }

    @Override
    public void getAliPayResult(PayResult result) {

    }

    @Override
    public void getWXPayResult(WxPaySuccessResult result) {
        if (result.getStatus() == 0) {
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new RefreshEvent());     // 支付成功刷新数据
            finish();
        } else {
            Toast.makeText(this, result.getError_msg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMsg(DataResultException dataResult) {

        // showToast(dataResult.getMsg());
    }
}

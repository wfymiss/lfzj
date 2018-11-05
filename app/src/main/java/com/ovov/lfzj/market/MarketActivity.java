package com.ovov.lfzj.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.YouzanLoginBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdk.YouzanToken;
import com.youzan.androidsdk.event.AbsAuthEvent;
import com.youzan.androidsdk.event.Event;
import com.youzan.androidsdkx5.YouzanBrowser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class MarketActivity extends BaseActivity {


    @BindView(R.id.youzan_view)
    YouzanBrowser mYouzanView;
    private String url;
    private int type;

    public static void toActivity(Context context, String url, int type) {
        Intent intent = new Intent(context, MarketActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_market;
    }

    @Override
    public void init() {
        url = getIntent().getExtras().getString("url");
        type = getIntent().getExtras().getInt("type");
        mYouzanView.subscribe(new AbsAuthEvent() {
            @Override
            public void call(Context context, boolean b) {

                if (b){
                    youzanLogin();
                }
            }
        });
        mYouzanView.loadUrl(url);
        mYouzanView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {

                if (s.contains("https://www.youzan.com/?from_source=support"))
                    return true;

                return false;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                dismiss();
                setTitleText(mYouzanView.getTitle());
            }
        });
    }

    private void youzanLogin() {

        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().youzanLogin()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<YouzanLoginBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<YouzanLoginBean> dataInfo) {
                        //dismiss();
                        YouzanToken youzanToken = new YouzanToken();
                        youzanToken.setAccessToken(dataInfo.datas().access_token);
                        youzanToken.setCookieKey(dataInfo.datas().cookie_key);
                        youzanToken.setCookieValue(dataInfo.datas().cookie_value);
                        YouzanSDK.sync(mActivity, youzanToken);
                        mYouzanView.sync(youzanToken);
                        //mYouzanView.loadUrl(url);
                    }
                });
        addSubscrebe(subscription);

    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        if (mYouzanView.pageCanGoBack()) {
            mYouzanView.pageGoBack();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mYouzanView.pageCanGoBack()) {
            mYouzanView.pageGoBack();
        } else {
            finish();
        }
    }
}

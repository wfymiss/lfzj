package com.ovov.lfzj.market;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.UrlBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends BaseFragment {


    @BindView(R.id.web)
    WebView mWeb;
    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;
    Unbinder unbinder;
    @BindView(R.id.scroll)
    ScrollView mScroll;
    @BindView(R.id.frame)
    FrameLayout mFrame;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    mWeb.goBack();
                }
                break;
            }
        }
    };
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            mProgressbar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    public static MarketFragment newInstance() {

        Bundle args = new Bundle();

        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MarketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }


    @SuppressLint("JavascriptInterface")
    @Override
    public void init() {
        super.init();
        mWeb.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        mWeb.setWebViewClient(webViewClient);

        WebSettings webSettings = mWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        mWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb.canGoBack()) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) { //只处理一次
                        handler.sendEmptyMessage(1);
                    }
                    return true;
                }
                return false;
            }
        });

        getMarketUrl();

    }

    private void getMarketUrl(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getMarketUrl()
                .compose(RxUtil.<DataInfo<UrlBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<UrlBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<UrlBean> urlBeanDataInfo) {
                        dismiss();
                        if (urlBeanDataInfo.datas().url.equals("")){
                            mScroll.setVisibility(View.VISIBLE);
                            mWeb.setVisibility(View.GONE);
                            mFrame.setVisibility(View.GONE);
                        }else {
                            mScroll.setVisibility(View.GONE);
                            mWeb.setVisibility(View.VISIBLE);
                            mWeb.loadUrl(urlBeanDataInfo.datas().url);
                            mFrame.setVisibility(View.VISIBLE);
                        }
                    }
                });
        addSubscrebe(subscription);
    }


    public boolean canGoback() {
        Log.e("webcangoback", mWeb.canGoBack() + "");

        return mWeb.canGoBack();
    }

    public void goBack() {
        mWeb.goBack();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}

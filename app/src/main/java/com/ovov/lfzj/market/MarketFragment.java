package com.ovov.lfzj.market;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
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
import com.youzan.androidsdkx5.YouzanBrowser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.youzan_view)
    YouzanBrowser mYouzanView;



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


    @Override
    public void init() {
        super.init();
        youzanLogin();
        mYouzanView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                if (s != null) {
                    if (s.contains("https://www.youzan.com/?from_source=support"))
                        return true;
                    //Log.e("urllllll",s);
                    MarketActivity.toActivity(mActivity, s);
                    return true;
                }
                return false;
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
                        dismiss();
                        YouzanToken youzanToken = new YouzanToken();
                        youzanToken.setAccessToken(dataInfo.datas().access_token);
                        youzanToken.setCookieKey(dataInfo.datas().cookie_key);
                        youzanToken.setCookieValue(dataInfo.datas().cookie_value);
                        YouzanSDK.sync(mActivity, youzanToken);
                        mYouzanView.sync(youzanToken);
                        mYouzanView.loadUrl("https://h5.youzan.com/wscshop/feature/FElLHR3x42");
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


}

package com.ovov.lfzj.home.ui;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.home.bean.NewsDetailBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

public class NoticeDetailActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web)
    WebView web;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void init() {
        tvTitle.setText("社区公告");

        String id = getIntent().getStringExtra("id");
        web.setWebViewClient(new WebViewClient());
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        //加载数据
        initData(id);


    }

    private void initData(String id) {

        Subscription subscription = RetrofitHelper.getInstance().getNoticeDetailList(id)
                .compose(RxUtil.<NewsDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<NewsDetailBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(NewsDetailBean listInfoDataInfo) {
                        Log.e("web", listInfoDataInfo.getDatas().getContent());
                        if (listInfoDataInfo.getCode().equals("200")) {
                            web.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
                            web.loadData(listInfoDataInfo.getDatas().getContent(), "text/html; charset=UTF-8", null);//这种写法可以正确解码

                        }
                    }
                });
        addSubscrebe(subscription);


    }

    public void showToast(String string) {
        UIUtils.displayToast(string);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

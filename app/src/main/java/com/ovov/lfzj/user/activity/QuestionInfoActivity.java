package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.WaveView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class QuestionInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.lin_title)
    FrameLayout lin_title;
    private String id;

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, QuestionInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void init() {
        web.setWebViewClient(new WebViewClient());
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        tvTitle.setText("问题详情");
        id = getIntent().getExtras().get("id").toString();
        initdata();
    }

    private void initdata() {
        Subscription subscription = RetrofitHelper.getInstance().questionInfo(id)
                .compose(RxUtil.<DataInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<SquareListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast("数据获取异常");
                        } else {

                            doFailed();
                        }


                    }

                    @Override
                    public void onNext(DataInfo<SquareListInfo> dataInfo) {
                        web.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
                        web.loadData(dataInfo.datas().getInfo(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

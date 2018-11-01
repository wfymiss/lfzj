package com.ovov.lfzj.home.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class MessageInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    String id;
    @BindView(R.id.tv_info_title)
    TextView tvInfoTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_coment)
    TextView tvComent;

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, MessageInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_info;
    }

    @Override
    public void init() {
        tvTitle.setText("消息详情");
        id = getIntent().getExtras().get("id").toString();
        initData();
    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getInfomationdetail(id)
                .compose(RxUtil.<DataInfo<BannerBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<BannerBean>>() {
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
                    public void onNext(DataInfo<BannerBean> listInfoDataInfo) {
                        tvInfoTitle.setText(listInfoDataInfo.datas().getTitle());
                        tvTime.setText(listInfoDataInfo.datas().getTime());
                        tvComent.setText(listInfoDataInfo.datas().getMessage());
                    }
                });
        addSubscrebe(subscription);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}

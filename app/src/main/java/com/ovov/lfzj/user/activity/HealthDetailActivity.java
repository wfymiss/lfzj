package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.bean.HealthDetailBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class HealthDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.state_time)
    TextView stateTime;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_check_health)
    TextView tvCheckHealth;
    @BindView(R.id.tv_order_numb)
    TextView tvOrderNumb;
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, HealthDetailActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_health_detail;
    }

    @Override
    public void init() {
        Subscription subscription = RetrofitHelper.getInstance().getHealthDetail()
                .compose(RxUtil.<HealthDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<HealthDetailBean>() {
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
                    public void onNext(HealthDetailBean listInfoDataInfo) {

                        if (!listInfoDataInfo.getDatas().getTime().isEmpty()) {
                            tvOrderTime.setText(listInfoDataInfo.getDatas().getTime());
                        }
                        if (!listInfoDataInfo.getDatas().getSite().isEmpty()) {
                            tvCheckHealth.setText(listInfoDataInfo.getDatas().getSite());
                        }
                        if (!listInfoDataInfo.getDatas().getOrder_sn().isEmpty()) {
                            tvOrderNumb.setText(listInfoDataInfo.getDatas().getOrder_sn());
                        }
                        if (!listInfoDataInfo.getDatas().getRemainingDays().isEmpty()) {
                            stateTime.setText(listInfoDataInfo.getDatas().getRemainingDays());
                        }
                    }

                });
        addSubscrebe(subscription);


    }


}

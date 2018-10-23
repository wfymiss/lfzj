package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class WorkerOrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_item)
    TextView tvItem;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.gridView)
    NoScrollGridView gridView;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_trouble)
    TextView tvTrouble;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_repair_name)
    TextView tvRepairName;
    private String id;

    public static void toActivity(Context context, int id) {
        Intent intent = new Intent(context, WorkerOrderDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_worker_order_detail;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_worker_order_detail);
        id = String.valueOf(getIntent().getIntExtra("id", 0));
        initData();
    }

    private void initData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getWorkDetail(id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();

                    }
                });
        addSubscrebe(subscription);
    }
}

package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.CheckBean;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.WorkDetailBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.utils.WorkerOrderTypeUtils;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.OwnerCancelDialog;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.event.OwnerCancelEvent;
import com.ovov.lfzj.event.OwnerCancelSuccessEvent;
import com.ovov.lfzj.event.PayResultEvent;
import com.ovov.lfzj.event.PaymentEvent;
import com.ovov.lfzj.event.RepairCommentSuccessEvent;
import com.ovov.lfzj.event.WorkerOrderCheckSuccessEvent;
import com.ovov.lfzj.home.payment.activity.H5PayActivityActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

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
    @BindView(R.id.lin_item)
    LinearLayout linItem;
    @BindView(R.id.lin_trouble)
    LinearLayout mLinTrouble;
    @BindView(R.id.lin_price)
    LinearLayout mLinPrice;
    @BindView(R.id.lin_repair_name)
    LinearLayout mLinRepairName;
    @BindView(R.id.tv_check)
    TextView mTvCheck;
    @BindView(R.id.tv_trouble)
    TextView tvTrouble;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_repair_name)
    TextView tvRepairName;
    @BindView(R.id.rating_speed)
    RatingBar mRatingSpeed;
    @BindView(R.id.rating_attitude)
    RatingBar mRatingAttitude;
    @BindView(R.id.rating_technology)
    RatingBar mRatingTechnology;
    @BindView(R.id.lin_comment)
    LinearLayout mLinComment;
    @BindView(R.id.re_number)
    LinearLayout reNumber;
    @BindView(R.id.lin_umber)
    LinearLayout linUmber;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.tv_reason)
    TextView mTvReason;
    @BindView(R.id.tv_remarks)
    TextView mTvRemarks;
    @BindView(R.id.lin_cancel_content)
    LinearLayout mLinCancelContent;
    private String id;
    private String order_id;
    private String type;

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
        addRxBusSubscribe(RepairCommentSuccessEvent.class, new Action1<RepairCommentSuccessEvent>() {
            @Override
            public void call(RepairCommentSuccessEvent repairCommentSuccessEvent) {
                initData();
            }
        });
        addRxBusSubscribe(OwnerCancelEvent.class, new Action1<OwnerCancelEvent>() {
            @Override
            public void call(OwnerCancelEvent ownerCancelEvent) {
                cancelWorkerOrder(ownerCancelEvent.reason, ownerCancelEvent.remarks);
            }
        });
        addRxBusSubscribe(PayResultEvent.class, new Action1<PayResultEvent>() {
            @Override
            public void call(PayResultEvent payResultEvent) {
                confirmPay(payResultEvent.type, payResultEvent.order_id, "");
            }
        });

    }

    private void initData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getWorkDetail(id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<WorkDetailBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<WorkDetailBean> dataInfo) {
                        dismiss();

                        switch (WorkerOrderTypeUtils.getStatus(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd)) {
                            case 1://未派单
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));
                                break;
                            case 2://派单中
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));
                                mTvCheck.setVisibility(View.VISIBLE);
                                mTvCheck.setText("撤销工单");
                                mTvCheck.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //cancelWorkerOrder();
                                        OwnerCancelDialog ownerCancelDialog = new OwnerCancelDialog(mActivity);
                                        ownerCancelDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.75));
                                        ownerCancelDialog.show();
                                    }
                                });
                                break;
                            case 3://维修中
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                mLinRepairName.setVisibility(View.VISIBLE);
                                tvRepairName.setText(dataInfo.datas().worker_user.name);
                                break;
                            case 5://待验收
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                mTvCheck.setVisibility(View.VISIBLE);
                                mLinTrouble.setVisibility(View.VISIBLE);
                                mLinPrice.setVisibility(View.VISIBLE);
                                mLinRepairName.setVisibility(View.VISIBLE);
                                tvTrouble.setText(dataInfo.datas().worker_offer.failure_briefing);
                                tvPrice.setText(dataInfo.datas().worker_offer.material_cost);
                                tvRepairName.setText(dataInfo.datas().worker_user.name);
                                break;
                            case 6://已完成
                                if (dataInfo.datas().work_evaluate.evaluation_content == null) {
                                    tvStatus.setText("未评价");

                                } else {
                                    tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));
                                    mLinComment.setVisibility(View.VISIBLE);
                                    mTvComment.setText(dataInfo.datas().work_evaluate.evaluation_content);
                                    mRatingSpeed.setRating(Float.parseFloat(dataInfo.datas().work_evaluate.door_speed));
                                    mRatingAttitude.setRating(Float.parseFloat(dataInfo.datas().work_evaluate.service_attitude));
                                    mRatingTechnology.setRating(Float.parseFloat(dataInfo.datas().work_evaluate.repair_technology));
                                }
                                mTvCheck.setVisibility(View.GONE);
                                mLinTrouble.setVisibility(View.VISIBLE);
                                mLinPrice.setVisibility(View.VISIBLE);
                                mLinRepairName.setVisibility(View.VISIBLE);
                                tvTrouble.setText(dataInfo.datas().worker_offer.failure_briefing);
                                tvPrice.setText(dataInfo.datas().worker_offer.material_cost);
                                tvRepairName.setText(dataInfo.datas().worker_user.name);
                                break;
                            case 7://已取消
                                mTvCheck.setVisibility(View.GONE);
                                mLinCancelContent.setVisibility(View.VISIBLE);
                                if (dataInfo.datas().work_revoke.content != null) {
                                    mTvReason.setVisibility(View.VISIBLE);
                                    mTvReason.setText(dataInfo.datas().work_revoke.content);
                                }
                                mTvRemarks.setText(dataInfo.datas().work_revoke.remarks);
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));
                                break;
                        }
                        tvName.setText(dataInfo.datas().username);
                        tvPhone.setText(dataInfo.datas().phone);
                        tvType.setText(dataInfo.datas().position_str);
                        tvLocation.setText(dataInfo.datas().address);
                        tvItem.setText(dataInfo.datas().category_name);
                        tvContent.setText(dataInfo.datas().content);
                        tvNumber.setText(dataInfo.datas().order_number);
                        tvTime.setText(dataInfo.datas().repair_time);
                        List<String> mGridData = dataInfo.datas().repair_img;
                        CommonAdapter<String> mAdapter = new CommonAdapter<String>(mActivity, mGridData, R.layout.item_grid_image) {
                            @Override
                            public void convert(ViewHolder viewHolder, String s, int i) {
                                ImageView ivImage = viewHolder.getView(R.id.iv_image);
                                Picasso.with(mActivity).load(s).into(ivImage);
                            }
                        };
                        gridView.setAdapter(mAdapter);
                        if (mGridData.size() > 0) {
                            gridView.setVisibility(View.VISIBLE);
                        } else {
                            gridView.setVisibility(View.GONE);
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    //业主撤销工单
    private void cancelWorkerOrder(String reason, String remarks) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().ownerCancelWorkerOrder(id, reason, remarks)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("撤销成功");
                        initData();
                        RxBus.getDefault().post(new OwnerCancelSuccessEvent());
                    }
                });
        addSubscrebe(subscription);
    }

    @OnClick({R.id.iv_back, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_check:
                recieptRemind();
                break;
        }
    }

    // 接单提醒
    private void recieptRemind() {
        final RemindDialogUtil.Builder dialogBuilder = new RemindDialogUtil.Builder(mActivity);
        dialogBuilder.setContent("确认验收吗？");
        final RemindDialogUtil dialog = dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                checkWorkerOrder();
            }
        });
        dialog.show();

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.68);
        lp.alpha = 0.96f;
        dialog.getWindow().setAttributes(lp);
    }

    private void checkWorkerOrder() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().workerOrderCheck(id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<CheckBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultExceptio = (DataResultException) e;
                            showToast(dataResultExceptio.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<CheckBean> dataInfo) {
                        dismiss();
                        if (dataInfo.datas().order_id == null) {
                            showToast("验收成功");
                            order_id = dataInfo.datas().order_id;
                            type = dataInfo.datas().type;
                            initData();
                            RxBus.getDefault().post(new WorkerOrderCheckSuccessEvent());
                            RepairCommentActivity.toActivity(mActivity, id);
                        }else if (dataInfo.datas().order_id!= null){

                            H5PayActivityActivity.toActivity(mActivity,dataInfo.datas().type,dataInfo.datas().order_id,"");
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        initData();
                        RxBus.getDefault().post(new WorkerOrderCheckSuccessEvent());
                        RepairCommentActivity.toActivity(mActivity, id);
                    }
                });
        addSubscrebe(subscription);
    }
}

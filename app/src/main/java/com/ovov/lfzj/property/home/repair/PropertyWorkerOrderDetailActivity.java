package com.ovov.lfzj.property.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.WorkDetailBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.utils.WorkerOrderTypeUtils;
import com.ovov.lfzj.base.widget.CancelDispathDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.base.widget.WorkerCancelDialog;
import com.ovov.lfzj.event.CancelEvent;
import com.ovov.lfzj.event.DetailCancelDispathEvent;
import com.ovov.lfzj.event.DetailWorkerCancelEvent;
import com.ovov.lfzj.event.DispathEvent;
import com.ovov.lfzj.event.DispathSuccessEvent;
import com.ovov.lfzj.event.RecieptSuccessEvent;
import com.ovov.lfzj.event.WorkerCancelSuccessEvent;
import com.ovov.lfzj.event.WorkerConfirmSuccessEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class PropertyWorkerOrderDetailActivity extends BaseActivity {

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
    @BindView(R.id.re_number)
    LinearLayout reNumber;
    @BindView(R.id.lin_umber)
    LinearLayout linUmber;
    @BindView(R.id.lin_repair_quote)
    LinearLayout mLinRepairQuote;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_dispatch)
    TextView mTvDispatch;
    @BindView(R.id.lin_dispath)
    LinearLayout mLinDispath;
    @BindView(R.id.tv_select_worker)
    TextView mTvSelectWorker;
    @BindView(R.id.lin_repair_worker_select)
    LinearLayout mLinRepairWorkerSelect;
    @BindView(R.id.et_trouble)
    EditText mEtTrouble;
    @BindView(R.id.et_cost)
    EditText mEtCost;
    @BindView(R.id.tv_reason)
    TextView mTvReason;
    @BindView(R.id.tv_remarks)
    TextView mTvRemarks;
    @BindView(R.id.lin_cancel_content)
    LinearLayout mLinCancelContent;
    @BindView(R.id.rating_speed)
    RatingBar mRatingSpeed;
    @BindView(R.id.rating_attitude)
    RatingBar mRatingAttitude;
    @BindView(R.id.rating_technology)
    RatingBar mRatingTechnology;
    @BindView(R.id.lin_comment)
    LinearLayout mLinComment;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    private String id;//工单id
    private WorkDetailBean workDetailBean;
    private String work_id;//维修工id

    public static void toActivity(Context context, int id) {
        Intent intent = new Intent(context, PropertyWorkerOrderDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_property_worker_order_detail;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        setTitleText(R.string.text_worker_order_detail);
        id = String.valueOf(getIntent().getIntExtra("id", 0));
        initData();
        addRxBusSubscribe(DispathEvent.class, new Action1<DispathEvent>() {
            @Override
            public void call(DispathEvent dispathEvent) {
                mTvSelectWorker.setText(dispathEvent.work_name);
                work_id = dispathEvent.work_id;

            }
        });
        addRxBusSubscribe(DetailCancelDispathEvent.class, new Action1<DetailCancelDispathEvent>() {
            @Override
            public void call(DetailCancelDispathEvent detailCancelDispathEvent) {
                cancelWorkeOrder(detailCancelDispathEvent.remarks);//维修主管取消工单弹出框返回
            }
        });

    }

    //维修主管取消工单
    private void cancelWorkeOrder(String remarks) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().cancelWorkOrder(this.id, remarks)
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
                        showToast("取消成功");
                        initData();
                        RxBus.getDefault().post(new CancelEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    //派单
    private void dispath(String id) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().workerDispath(this.id, id)
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
                        showToast("派单成功");
                        initData();
                        RxBus.getDefault().post(new DispathSuccessEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    //维修工取消工单
    private void workerCancel(String reason, String remarks) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().workerCancels(id, getTime(), reason, remarks)
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
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("取消接单成功");
                        RxBus.getDefault().post(new WorkerCancelSuccessEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    //获取工单详情
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
                        workDetailBean = dataInfo.datas();
                        tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));
                        switch (WorkerOrderTypeUtils.getStatus(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd)) {
                            case 1://未派单
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                                    mTvCheck.setVisibility(View.GONE);
                                    mLinDispath.setVisibility(View.VISIBLE);
                                    mTvDispatch.setText("派单");
                                    mTvCancel.setText("取消工单");
                                    mTvDispatch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(mTvSelectWorker.getText().toString())) {
                                                showToast("请选择维修工");
                                                return;
                                            }
                                            dispatchRemind(work_id);
                                        }
                                    });
                                    mTvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CancelDispathDialog dispathDialog = new CancelDispathDialog(mActivity, 2, 0);
                                            dispathDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.75));
                                            dispathDialog.show();
                                        }
                                    });
                                    mLinRepairWorkerSelect.setVisibility(View.VISIBLE);
                                } else if (LoginUserBean.getInstance().getAppPermission().gongdan_jiedan) {

                                } else {

                                }
                                break;
                            case 2://派单中
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                                    //处理维修主管操作
                                    mLinCancelContent.setVisibility(View.GONE);
                                    mLinDispath.setVisibility(View.VISIBLE);
                                    mTvDispatch.setText("改派");
                                    mTvCancel.setText("取消工单");
                                    mTvSelectWorker.setText(workDetailBean.worker_user.name);
                                    mTvDispatch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dispatchRemind(work_id);
                                        }
                                    });
                                    mTvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CancelDispathDialog dispathDialog = new CancelDispathDialog(mActivity, 2, 0);
                                            dispathDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.75));
                                            dispathDialog.show();
                                        }
                                    });
                                    mLinRepairWorkerSelect.setVisibility(View.VISIBLE);
                                } else if (LoginUserBean.getInstance().getAppPermission().gongdan_jiedan) {
                                    //处理维修工操作
                                    mLinDispath.setVisibility(View.VISIBLE);
                                    mTvDispatch.setText("接单");
                                    mTvCancel.setText("取消接单");
                                    mTvSelectWorker.setText(workDetailBean.worker_user.name);
                                    mTvDispatch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recieptRemind();
                                        }
                                    });
                                    mTvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            WorkerCancelDialog dispathDialog = new WorkerCancelDialog(mActivity, 2, 0);
                                            dispathDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.75));
                                            dispathDialog.show();
                                        }
                                    });
                                    mLinRepairWorkerSelect.setVisibility(View.GONE);
                                }
                                break;
                            case 3://维修中
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                mLinDispath.setVisibility(View.GONE);
                                mLinRepairWorkerSelect.setVisibility(View.GONE);
                                if (LoginUserBean.getInstance().getAppPermission().gongdan_jiedan) {

                                    if (dataInfo.datas().position.equals("0"))
                                        mLinRepairQuote.setVisibility(View.VISIBLE);
                                    else
                                        mLinRepairQuote.setVisibility(View.GONE);
                                    mEtTrouble.setVisibility(View.VISIBLE);
                                    mTvCheck.setVisibility(View.VISIBLE);
                                    mTvCheck.setText("提交");
                                    mTvCheck.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            workerCommit();
                                        }
                                    });
                                }
                                break;
                            case 4://已拒单
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                                    mTvCheck.setVisibility(View.GONE);
                                    mLinDispath.setVisibility(View.VISIBLE);
                                    mTvDispatch.setText("派单");
                                    mTvCancel.setText("取消工单");
                                    mTvDispatch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dispatchRemind(work_id);
                                        }
                                    });
                                    mTvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CancelDispathDialog dispathDialog = new CancelDispathDialog(mActivity, 2, 0);
                                            dispathDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.75));
                                            dispathDialog.show();
                                        }
                                    });
                                    mLinCancelContent.setVisibility(View.VISIBLE);
                                    if (dataInfo.datas().work_revoke.content != null) {
                                        mTvReason.setVisibility(View.VISIBLE);
                                        mTvReason.setText(dataInfo.datas().work_revoke.content);
                                    }
                                    mTvRemarks.setText(dataInfo.datas().work_revoke.remarks);
                                    mLinRepairWorkerSelect.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 5://待验收
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                mLinRepairQuote.setVisibility(View.GONE);
                                mEtTrouble.setVisibility(View.GONE);
                                mTvCheck.setVisibility(View.GONE);
                                mLinTrouble.setVisibility(View.VISIBLE);
                                mLinPrice.setVisibility(View.VISIBLE);
                                mLinRepairName.setVisibility(View.VISIBLE);
                                tvTrouble.setText(workDetailBean.worker_offer.failure_briefing);
                                tvPrice.setText(workDetailBean.worker_offer.material_cost);
                                tvRepairName.setText(workDetailBean.worker_user.name);
                                break;
                            case 6://已完成
                                if (workDetailBean.work_evaluate.evaluation_content != null) {
                                    tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));
                                    mLinComment.setVisibility(View.VISIBLE);
                                    mTvComment.setText(dataInfo.datas().work_evaluate.evaluation_content);
                                    mRatingSpeed.setRating(Float.parseFloat(dataInfo.datas().work_evaluate.door_speed));
                                    mRatingAttitude.setRating(Float.parseFloat(dataInfo.datas().work_evaluate.service_attitude));
                                    mRatingTechnology.setRating(Float.parseFloat(dataInfo.datas().work_evaluate.repair_technology));
                                } else {
                                    tvStatus.setText("未评价");
                                }
                                mLinTrouble.setVisibility(View.VISIBLE);
                                mLinPrice.setVisibility(View.VISIBLE);
                                mLinRepairName.setVisibility(View.VISIBLE);
                                tvTrouble.setText(workDetailBean.worker_offer.failure_briefing);
                                tvPrice.setText(workDetailBean.worker_offer.material_cost);
                                tvRepairName.setText(workDetailBean.worker_user.name);
                                break;
                            case 7://已取消
                                tvStatus.setText(WorkerOrderTypeUtils.getStatusName(dataInfo.datas().status, dataInfo.datas().status_wx, dataInfo.datas().status_jd, dataInfo.datas().status_pd));

                                mTvCheck.setVisibility(View.GONE);
                                mLinCancelContent.setVisibility(View.VISIBLE);
                                if (dataInfo.datas().work_revoke.content != null) {
                                    mTvReason.setVisibility(View.VISIBLE);
                                    mTvReason.setText(dataInfo.datas().work_revoke.content);
                                }
                                mTvRemarks.setText(dataInfo.datas().work_revoke.remarks);
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

    private void workerCommit() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().workerConfirm(id, mEtCost.getText().toString().trim(), mEtTrouble.getText().toString().trim())
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
                        showToast("提交成功");
                        initData();
                        RxBus.getDefault().post(new WorkerConfirmSuccessEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick({R.id.iv_back, R.id.tv_check, R.id.tv_select_worker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_check:
                //dispath(work_id);
                break;
            case R.id.tv_select_worker:
                DispathActivity.toActivity(mActivity);
                break;
        }
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    // 派单提醒
    private void dispatchRemind(String w_id) {
        final RemindDialogUtil.Builder dialogBuilder = new RemindDialogUtil.Builder(mActivity);
        dialogBuilder.setContent("确认派单吗？");
        final RemindDialogUtil dialog = dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                if (TextUtils.isEmpty(mTvSelectWorker.getText().toString())) {
                    showToast("请选择维修工");
                    return;
                }
                dispath(w_id);

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

    //接单
    private void workRecipet(int id) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().workReciept(String.valueOf(id))
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
                            dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("接单成功");
                        RxBus.getDefault().post(new RecieptSuccessEvent());
                        initData();
                        finish();

                    }
                });
        addSubscrebe(subscription);

    }

    // 接单提醒
    private void recieptRemind() {
        final RemindDialogUtil.Builder dialogBuilder = new RemindDialogUtil.Builder(mActivity);
        dialogBuilder.setContent("确认接单吗？");
        final RemindDialogUtil dialog = dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                workRecipet(Integer.parseInt(id));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recipetDialogMsg(DetailWorkerCancelEvent detailWorkerCancelEvent) {
        workerCancel(detailWorkerCancelEvent.reason, detailWorkerCancelEvent.remarks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}

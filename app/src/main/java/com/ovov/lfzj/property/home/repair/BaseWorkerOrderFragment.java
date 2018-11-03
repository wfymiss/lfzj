package com.ovov.lfzj.property.home.repair;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.utils.WorkerOrderTypeUtils;
import com.ovov.lfzj.base.widget.CancelDispathDialog;
import com.ovov.lfzj.base.widget.CancelWorkerOrderEvent;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.base.widget.ScaleImageView;
import com.ovov.lfzj.base.widget.WorkerCancelDialog;
import com.ovov.lfzj.event.DetailWorkerCancelEvent;
import com.ovov.lfzj.event.ListCancelDispathEvent;
import com.ovov.lfzj.event.ListWorkerCancelEvent;
import com.ovov.lfzj.event.RecieptSuccessEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

/**
 * Created by kaite on 2018/10/10.
 */

public abstract class BaseWorkerOrderFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.list_wolrorder)
    ListView mListWolrorder;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    @BindView(R.id.lin_null)
    LinearLayout mLinNull;
    private int page;
    List<WorkOrderListInfo> mData = new ArrayList<>();
    private CommonAdapter<WorkOrderListInfo> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_work_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    protected abstract Observable<ListInfo<WorkOrderListInfo>> getObservable(int page);

    protected abstract boolean isMaintenanceSupervisor();//是否维修主管

    protected abstract boolean isPending();//待处理

    protected abstract boolean isMaintenance();//是否维修工

    protected abstract boolean isInHand();//处理中

    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);

        mAdapter = new CommonAdapter<WorkOrderListInfo>(mActivity, mData, R.layout.item_work_order) {
            @Override
            public void convert(ViewHolder viewHolder, WorkOrderListInfo workOrderListInfo, int i) {
                viewHolder.setText(R.id.tv_nickname, workOrderListInfo.username);
                viewHolder.setText(R.id.tv_time, workOrderListInfo.repair_time);

                viewHolder.setText(R.id.tv_content, workOrderListInfo.content);
                viewHolder.setText(R.id.tv_location, workOrderListInfo.address);
                TextView tvDispatch = viewHolder.getView(R.id.tv_dispatch);
                TextView tvCancel = viewHolder.getView(R.id.tv_cancel);
                CircleImageView mIvHeader = viewHolder.getView(R.id.iv_header);
                if (workOrderListInfo.user_logo != null && !TextUtils.isEmpty(workOrderListInfo.user_logo))
                    Picasso.with(mActivity).load(workOrderListInfo.user_logo).placeholder(R.mipmap.ic_default_head).into(mIvHeader);
                else
                    Picasso.with(mActivity).load(R.mipmap.ic_default_head).into(mIvHeader);
                viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status,workOrderListInfo.status_wx,workOrderListInfo.status_jd,workOrderListInfo.status_pd));
                switch (WorkerOrderTypeUtils.getStatus(workOrderListInfo.status,workOrderListInfo.status_wx,workOrderListInfo.status_jd,workOrderListInfo.status_pd)){
                    case 1://未派单
                        if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                            tvDispatch.setText("派单");
                            tvDispatch.setVisibility(View.VISIBLE);
                            tvDispatch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PropertyWorkerOrderDetailActivity.toActivity(mActivity,workOrderListInfo.id);
                                }
                            });
                            tvCancel.setVisibility(View.VISIBLE);
                            tvCancel.setText("取消工单");
                            tvCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CancelDispathDialog dispathDialog = new CancelDispathDialog(mActivity, 1,i);
                                    dispathDialog.setWidth((int) (UIUtils.getScreenWidth()*0.75));
                                    dispathDialog.show();
                                }
                            });
                        } else if (LoginUserBean.getInstance().getAppPermission().gongdan_jiedan) {
                            tvDispatch.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);

                        } else {
                            tvDispatch.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);
                        }
                        break;
                    case 2://派单中
                        if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                            tvDispatch.setText("改派");
                            tvDispatch.setVisibility(View.VISIBLE);
                            tvCancel.setVisibility(View.VISIBLE);
                            tvCancel.setText("取消工单");
                            tvCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CancelDispathDialog dispathDialog = new CancelDispathDialog(mActivity, 1,i);
                                    dispathDialog.setWidth((int) (UIUtils.getScreenWidth()*0.75));
                                    dispathDialog.show();
                                }
                            });

                        } else if (LoginUserBean.getInstance().getAppPermission().gongdan_jiedan) {
                            tvDispatch.setText("接单");
                            tvDispatch.setVisibility(View.VISIBLE);
                            tvCancel.setText("取消接单");
                            tvCancel.setVisibility(View.VISIBLE);
                            tvDispatch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recieptRemind(workOrderListInfo.id);
                                }
                            });
                            tvCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    WorkerCancelDialog workerCancelDialog = new WorkerCancelDialog(mActivity,1,i);
                                    workerCancelDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.75));
                                    workerCancelDialog.show();
                                }
                            });
                        } else {
                            tvDispatch.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);
                        }
                        break;
                    case 3://维修中
                        if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                            tvDispatch.setText("派单");
                            tvDispatch.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);
                            tvCancel.setText("取消工单");
                        } else if (LoginUserBean.getInstance().getAppPermission().gongdan_jiedan) {
                            tvDispatch.setText("提交");
                            tvDispatch.setVisibility(View.VISIBLE);
                            tvCancel.setVisibility(View.GONE);
                            tvDispatch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PropertyWorkerOrderDetailActivity.toActivity(mActivity,workOrderListInfo.id);
                                }
                            });

                        } else {
                            tvDispatch.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);
                        }
                        break;
                    case 4://已拒单
                        if (LoginUserBean.getInstance().getAppPermission().gongdan_paidan) {
                            tvDispatch.setText("派单");
                            tvDispatch.setVisibility(View.VISIBLE);
                            tvDispatch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PropertyWorkerOrderDetailActivity.toActivity(mActivity,workOrderListInfo.id);
                                }
                            });
                            tvCancel.setVisibility(View.VISIBLE);
                            tvCancel.setText("取消工单");
                            tvCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CancelDispathDialog dispathDialog = new CancelDispathDialog(mActivity, 1,i);
                                    dispathDialog.setWidth((int) (UIUtils.getScreenWidth()*0.75));
                                    dispathDialog.show();
                                }
                            });
                        }
                        break;
                    case 5://待验收
                        tvDispatch.setVisibility(View.GONE);
                        break;
                    case 6://已完成
                        tvDispatch.setVisibility(View.GONE);
                        break;
                    case 7://已取消
                        tvDispatch.setVisibility(View.GONE);
                        break;
                }

                NoScrollGridView mGridImage = viewHolder.getView(R.id.gridView);
                List<String> mGrid = workOrderListInfo.repair_img;
                if (mGrid.size() > 0) {
                    mGridImage.setVisibility(View.VISIBLE);
                } else {
                    mGridImage.setVisibility(View.GONE);
                }
                CommonAdapter<String> mGridAdapter = new CommonAdapter<String>(mActivity, mGrid, R.layout.item_grid_image) {
                    @Override
                    public void convert(ViewHolder viewHolder, String s, int i) {
                        ImageView ivGrid = viewHolder.getView(R.id.iv_image);
                        Picasso.with(mActivity).load(s).into(ivGrid);
                        ivGrid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScaleImageView scaleImageView = new ScaleImageView(mActivity);
                                scaleImageView.setUrls(mGrid, i);
                                scaleImageView.create();
                            }
                        });
                    }
                };
                mGridImage.setAdapter(mGridAdapter);
                RelativeLayout reContainer = viewHolder.getView(R.id.re_container);
                reContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PropertyWorkerOrderDetailActivity.toActivity(mActivity, workOrderListInfo.id);
                    }
                });

            }
        };
        mListWolrorder.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
            }
        });
        mRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });
        mRefresh.autoRefresh();
        /*addRxBusSubscribe(ListWorkerCancelEvent.class, new Action1<ListWorkerCancelEvent>() {
            @Override
            public void call(ListWorkerCancelEvent listWorkerCancelEvent) {
                workerCancel(listWorkerCancelEvent.posistion,listWorkerCancelEvent.reason,listWorkerCancelEvent.remarks);
            }
        });
*/
    }

    private void workerCancel(int posistion, String reason, String remarks) {
        Subscription subscription = RetrofitHelper.getInstance().workerCancels(String.valueOf(mData.get(posistion).id),getTime(),reason,remarks)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        showToast("取消接单成功");
                        mRefresh.autoRefresh();
                    }
                });
        addSubscrebe(subscription);
    }

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
                        mRefresh.autoRefresh();
                        RxBus.getDefault().post(new RecieptSuccessEvent());
                    }
                });
        addSubscrebe(subscription);

    }

    private void initData(int refreshType) {
        if (refreshType == REFRESH) {
            page = 1;
        } else {
            page = page + 1;
        }
        Subscription subscription = getObservable(page)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<WorkOrderListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            if (refreshType == REFRESH) {
                                mRefresh.finishRefresh(true);
                            } else {
                                mRefresh.finishLoadmore(true);
                            }
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            if (refreshType == REFRESH) {
                                mRefresh.finishRefresh(false);
                            } else {
                                mRefresh.finishLoadmore(false);
                            }
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ListInfo<WorkOrderListInfo> listInfo) {
                        if (listInfo.datas().size() < 10) {
                            mRefresh.setEnableLoadmore(false);
                        } else {
                            mRefresh.setEnableLoadmore(true);
                        }
                        if (refreshType == REFRESH) {
                            mRefresh.finishRefresh(true);
                            if (listInfo.datas().size() > 0) {
                                mLinNull.setVisibility(View.GONE);
                                mData.clear();
                                mData.addAll(listInfo.datas());
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mData.clear();
                                mAdapter.notifyDataSetChanged();
                                mLinNull.setVisibility(View.VISIBLE);
                            }

                        } else {
                            mRefresh.finishLoadmore(true);
                            mData.addAll(listInfo.datas());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
        addSubscrebe(subscription);
    }



    private void cancelWorkeOrder(String id,String remarks) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().cancelWorkOrder(id,remarks)
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
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("取消成功");
                        mRefresh.autoRefresh();
                        RxBus.getDefault().post(new CancelWorkerOrderEvent());
                    }
                });
        addSubscrebe(subscription);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelWorkOrder(ListCancelDispathEvent listCancelDispathEvent){
        cancelWorkeOrder(String.valueOf(mData.get(listCancelDispathEvent.posistion).id),listCancelDispathEvent.remarks);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void workerCancel(ListWorkerCancelEvent listWorkerCancelEvent){
        workerCancel(listWorkerCancelEvent.posistion,listWorkerCancelEvent.reason,listWorkerCancelEvent.remarks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    // 提醒
    private void recieptRemind(int w_id) {
        final RemindDialogUtil.Builder dialogBuilder = new RemindDialogUtil.Builder(mActivity);
        dialogBuilder.setContent("确认接单吗？");
        final RemindDialogUtil dialog=dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                workRecipet(w_id);
            }
        });
        dialog.show();

        WindowManager windowManager=mActivity.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        lp.width= (int) (display.getWidth()*0.68);
        lp.alpha=0.96f;
        dialog.getWindow().setAttributes(lp);
    }

}

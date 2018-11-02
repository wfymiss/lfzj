package com.ovov.lfzj.home.repair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.utils.WorkerOrderTypeUtils;
import com.ovov.lfzj.base.widget.CancelDispathDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.ScaleImageView;
import com.ovov.lfzj.base.widget.WorkerCancelDialog;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.property.home.repair.DispathActivity;
import com.ovov.lfzj.property.home.repair.PropertyWorkerOrderDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;

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

        mAdapter = new CommonAdapter<WorkOrderListInfo>(mActivity, mData, R.layout.item_work_order) {
            @Override
            public void convert(ViewHolder viewHolder, WorkOrderListInfo workOrderListInfo, int i) {
                viewHolder.setText(R.id.tv_nickname, workOrderListInfo.username);
                viewHolder.setText(R.id.tv_time, workOrderListInfo.repair_time);

                viewHolder.setText(R.id.tv_content, workOrderListInfo.content);
                viewHolder.setText(R.id.tv_location, workOrderListInfo.address);
                TextView tvDispatch = viewHolder.getView(R.id.tv_dispatch);
                TextView tvCancel = viewHolder.getView(R.id.tv_cancel);
                switch (WorkerOrderTypeUtils.getStatus(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd)) {
                    case 1://未派单
                        viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));
                        break;
                    case 2://派单中
                        viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));
                        break;
                    case 3://维修中
                        tvDispatch.setVisibility(View.GONE);
                        viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));
                        break;
                    case 4://已拒单
                        //viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));
                        break;
                    case 5://待验收

                        viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));

                        tvDispatch.setVisibility(View.VISIBLE);
                        tvDispatch.setText("验收");
                        tvDispatch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                WorkerOrderDetailActivity.toActivity(mActivity, workOrderListInfo.id);
                            }
                        });
                        break;
                    case 6://已完成
                        if (workOrderListInfo.work_evaluate.evaluation_content == null) {
                            viewHolder.setText(R.id.tv_status, "未评价");
                            tvDispatch.setVisibility(View.VISIBLE);
                            tvDispatch.setText("去评价");
                            tvDispatch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RepairCommentActivity.toActivity(mActivity, String.valueOf(workOrderListInfo.id));
                                }
                            });
                        } else {
                            tvDispatch.setVisibility(View.GONE);
                            viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));
                        }
                        break;
                    case 7://已取消
                        viewHolder.setText(R.id.tv_status, WorkerOrderTypeUtils.getStatusName(workOrderListInfo.status, workOrderListInfo.status_wx, workOrderListInfo.status_jd, workOrderListInfo.status_pd));
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
                        WorkerOrderDetailActivity.toActivity(mActivity, workOrderListInfo.id);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

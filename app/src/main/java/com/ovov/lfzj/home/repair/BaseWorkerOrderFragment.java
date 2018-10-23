package com.ovov.lfzj.home.repair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.ScaleImageView;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
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
                if (isMaintenanceSupervisor() && isPending()) {
                    tvDispatch.setVisibility(View.VISIBLE);
                } else {
                    tvDispatch.setVisibility(View.GONE);
                }
                switch (workOrderListInfo.receipt_staff) {
                    case 0:
                        viewHolder.setText(R.id.tv_status, "待维修");
                        break;
                    case 1:
                        viewHolder.setText(R.id.tv_status, "派单中");
                        break;
                    case 2:
                        viewHolder.setText(R.id.tv_status, "维修中");
                        break;
                    case 3:
                        viewHolder.setText(R.id.tv_status, "已完成");
                        break;
                    case 4:
                        viewHolder.setText(R.id.tv_status, "待评价");
                        break;
                    case 5:
                        viewHolder.setText(R.id.tv_status, "已评价");
                        break;
                    case 6:
                        viewHolder.setText(R.id.tv_status, "业主取消");
                        break;
                    default:
                        break;
                }
                NoScrollGridView mGridImage = viewHolder.getView(R.id.gridView);
                List<String> mGrid = workOrderListInfo.repair_img;
                if (mGrid.size() > 0){
                    mGridImage.setVisibility(View.VISIBLE);
                }else {
                    mGridImage.setVisibility(View.GONE);
                }
                CommonAdapter<String> mGridAdapter = new CommonAdapter<String>(mActivity,mGrid,R.layout.item_grid_image) {
                    @Override
                    public void convert(ViewHolder viewHolder, String s, int i) {
                        ImageView ivGrid = viewHolder.getView(R.id.iv_image);
                        Picasso.with(mActivity).load(s).into(ivGrid);
                        ivGrid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScaleImageView scaleImageView = new ScaleImageView(mActivity);
                                scaleImageView.setUrls(mGrid,i);
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
                        WorkerOrderDetailActivity.toActivity(mActivity,workOrderListInfo.id);
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
                        mRefresh.setEnableLoadmore(false);
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
                        }
                        if (refreshType == REFRESH) {
                            mRefresh.finishRefresh(true);
                            mData.clear();
                            mData.addAll(listInfo.datas());
                            mAdapter.notifyDataSetChanged();
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

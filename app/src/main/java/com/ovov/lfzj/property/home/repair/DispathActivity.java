package com.ovov.lfzj.property.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkerListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.DispathEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class DispathActivity extends BaseActivity {

    @BindView(R.id.list_dispath)
    ListView mListDispath;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    private String token;
    private String orderId;
    private String type;
    private List<WorkerListInfo> mData;
    private CommonAdapter<WorkerListInfo> mAdapter;

    public static void toActivity(Context context, String orderId) {
        Intent intent = new Intent(context, DispathActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderid", orderId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dispath;
    }

    @Override
    public void init() {
        setTitleText("维修工");
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderid");
        initList();
        mRefresh.setEnableLoadmore(false);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
        mRefresh.autoRefresh();
    }

    private void initList() {
        mData = new ArrayList<>();
        mAdapter = new CommonAdapter<WorkerListInfo>(mActivity, mData, R.layout.item_dispath) {
            @Override
            public void convert(ViewHolder viewHolder, WorkerListInfo workerListInfo, int i) {
                viewHolder.setText(R.id.tv_name,workerListInfo.name);
                TextView tvName = viewHolder.getView(R.id.tv_name);
                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        mListDispath.setAdapter(mAdapter);
        mListDispath.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dispath(mData.get((int) id).id);
            }
        });
    }

    private void dispath(String id) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().workerDispath(orderId,id)
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
                        showToast("派单成功");
                        RxBus.getDefault().post(new DispathEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getWorks()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<WorkerListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        mRefresh.finishRefresh(false);
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ListInfo<WorkerListInfo> listInfo) {
                        mRefresh.finishRefresh(true);
                        mData.addAll(listInfo.datas());
                        mAdapter.notifyDataSetChanged();
                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

package com.ovov.lfzj.opendoor;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.VisistorRecordResult;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.opendoor.adapter.VIsitorListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

/**
 * 访客通行记录
 * Created by Administrator on 2017/7/25.
 */

public class VistitorActivity extends BaseActivity {
    @BindView(R.id.visitor_list)
    SmartRefreshLayout visitorList;
    @BindView(R.id.tv_title)
    TextView visitorToolbar;
    @BindView(R.id.recyclerview)
    ListView listView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private ActivityUtils activityUtil;
    private List<String> listName;
    private VIsitorListAdapter adapter;
    private String token;
    private int page = 1;
    private List<VisistorRecordResult.DataBean> mData;


    @Override
    public void init() {
        visitorToolbar.setText("访客记录");
        initList();
        visitorList.setEnableLoadmore(false);
        visitorList.setEnableLoadmore(false);
        visitorList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
                //  initBanner();
            }
        });
        visitorList.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });

        visitorList.autoRefresh();

    }

    private void initList() {


        adapter = new VIsitorListAdapter(this);
        listView.setAdapter(adapter);


    }

    private void initData(final int type) {
        if (type == REFRESH) {
            page = 1;
        } else if (type == LOADMORE) {
            page = page + 1;
        }

        Subscription subscription = RetrofitHelper.getInstance().getVisitorLog(page)
                .compose(RxUtil.<VisistorRecordResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<VisistorRecordResult>() {
                    @Override
                    public void onError(Throwable e) {
                        if (type == REFRESH) {
                            visitorList.finishRefresh(false);

                        } else {
                            visitorList.finishLoadmore(false);
                        }

                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(VisistorRecordResult listInfoDataInfo) {

                        if (listInfoDataInfo.getCode().equals("200")) {
                            if (listInfoDataInfo.getData().size() < 0) {
                                visitorList.setEnableLoadmore(false);
                            }
                            if (type == REFRESH) {
                                visitorList.finishRefresh(true);
                                adapter.setData(listInfoDataInfo.getData());

                            } else {
                                adapter.addAll(listInfoDataInfo.getData());
                                visitorList.finishLoadmore(true);
                            }
                        } else {
                            if (type == REFRESH) {
                                visitorList.finishRefresh(false);

                            } else {
                                visitorList.finishLoadmore(false);
                            }
                        }


                    }
                });
        addSubscrebe(subscription);

    }
//    }
//        @Override
//        public void setVisistorData(VisistorRecordResult recordResult) {
//            mData = recordResult.getData();
//            adapter.setData(mData);
//            visitorList.onRefreshComplete();
//    }
//
//    @Override
//    public void setNoData() {
//        Toast.makeText(this, "暂时没有记录", Toast.LENGTH_SHORT).show();
//        visitorList.onRefreshComplete();
//    }
//
//    @Override
//    public void setMoreData(VisistorRecordResult recordResult) {
//        visitorList.onRefreshComplete();
//        mData.addAll(recordResult.getData());
//    }
//
//    @Override
//    public void setNoMore() {
//        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
//        visitorList.onRefreshComplete();
//    }
//
//    @Override
//    public void showMsg(String dataResult) {
//        Toast.makeText(this,dataResult, Toast.LENGTH_SHORT).show();
//
//    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_visitor;
    }



    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

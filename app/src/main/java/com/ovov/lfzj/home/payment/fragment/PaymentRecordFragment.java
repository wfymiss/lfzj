package com.ovov.lfzj.home.payment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.PaymentEvent;
import com.ovov.lfzj.home.payment.activity.PayMentPayActivity;
import com.ovov.lfzj.home.payment.adapter.PaymentRecordListAdapter;
import com.ovov.lfzj.home.payment.presenter.PropertyPaymentPresent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

//*
// * 物业缴费记录界面——已缴费
// * Created by Administrator on 2017/8/2.


public class PaymentRecordFragment extends BaseFragment {
    @BindView(R.id.lin_null)
    LinearLayout mLinNull;
    private Context mContext;
    private Unbinder unbinder;
    @BindView(R.id.pm_record_swf)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.pm_record_recy)
    RecyclerView recyclerView;

    private String token, sub_id = null;
    private String order_state = "paid";     // 付款状态（已缴费）
    private int page = 1;                   // 数据页面
    private int lastItem = 0;
    private boolean loading = true;         // 页面评论recycler刷新判断, 数据加载完为false

    private List<PropertyPaymentInfo> list = new ArrayList<>();             // 定义历史账单数据集

    private PropertyPaymentPresent present;
    private LinearLayoutManager linearLayoutManager;
    private PaymentRecordListAdapter adapter;        // 历史账单适配器

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private CommonAdapter<PropertyPaymentInfo> mAdapter;

    public static PaymentRecordFragment newInstance() {
        Bundle args = new Bundle();
        PaymentRecordFragment fragment = new PaymentRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        super.init();
        initList();
        initRefresh();
        addRxBusSubscribe(PaymentEvent.class, new Action1<PaymentEvent>() {
            @Override
            public void call(PaymentEvent paymentEvent) {
                refreshLayout.autoRefresh();
            }
        });
    }

    private void initList() {
        mAdapter = new CommonAdapter<PropertyPaymentInfo>(mActivity, list, R.layout.layout_paymentrecord_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, final PropertyPaymentInfo propertyPaymentInfo) {
                viewHolder.setText(R.id.payment_obj, propertyPaymentInfo.room_number);
                viewHolder.setText(R.id.payment_time, propertyPaymentInfo.charge_from);
                viewHolder.setText(R.id.payment_endtime, propertyPaymentInfo.charge_end);
                final ImageView ivSelect = viewHolder.getView(R.id.payment_sel_img);
                DecimalFormat df = new DecimalFormat("0.00");
                double money = propertyPaymentInfo.money;
                String newMoney = df.format(money);
                viewHolder.setText(R.id.payment_bill, "￥" + newMoney);
                viewHolder.setText(R.id.payment_title, propertyPaymentInfo.name);
                RelativeLayout reContainer = viewHolder.getView(R.id.re_container);
                reContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PayMentPayActivity.toActivity(mActivity, propertyPaymentInfo.name, propertyPaymentInfo.id, "paid");
                    }
                });
            }
        };
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);    //竖向刷新
        recyclerView.setLayoutManager(linearLayoutManager);          // recycler 竖向滑动
        recyclerView.setAdapter(mAdapter);

    }

    private void getData(final int type) {
        if (type == REFRESH)
            page = 1;
        else
            page = page + 1;
        Subscription subscription = RetrofitHelper.getInstance().getAlreadyPay(page)
                .compose(RxUtil.<ListInfo<PropertyPaymentInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<PropertyPaymentInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.setLoadmoreFinished(false);
                        if (type == REFRESH) {
                            refreshLayout.finishRefresh(false);
                        } else {
                            refreshLayout.finishLoadmore(false);
                        }
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();

                        }
                    }

                    @Override
                    public void onNext(ListInfo<PropertyPaymentInfo> propertyPaymentInfoListInfo) {
                        if (type == REFRESH) {
                            refreshLayout.finishRefresh(true);
                            list.clear();
                            list.addAll(propertyPaymentInfoListInfo.datas());
                            mAdapter.notifyDataSetChanged();
                            if (propertyPaymentInfoListInfo.datas().size() == 0) {
                                mLinNull.setVisibility(View.VISIBLE);
                            } else {
                                mLinNull.setVisibility(View.GONE);
                            }

                        } else {
                            refreshLayout.finishLoadmore(true);
                            list.addAll(propertyPaymentInfoListInfo.datas());
                            mAdapter.notifyDataSetChanged();
                        }
                        if (propertyPaymentInfoListInfo.datas().size() < 10) {
                            refreshLayout.setEnableLoadmore(false);
                        }

                    }
                });
        addSubscrebe(subscription);
    }


    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(REFRESH);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(LOADMORE);
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

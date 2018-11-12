package com.ovov.lfzj.home.payment.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.PaymentEvent;
import com.ovov.lfzj.home.event.PassValueEvent;
import com.ovov.lfzj.home.payment.activity.PayMentPayActivity;
import com.ovov.lfzj.home.payment.activity.PropertyBillAffirmActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

/**
 * 业主版未缴费页
 * Created by  lyy on 2018/4/25.
 */

public class PaymentNoneFragment extends BaseFragment {
    private Unbinder unbinder;
    @BindView(R.id.pm_none_swf)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.pm_none_recy)
    RecyclerView recyclerView;
    @BindView(R.id.pm_none_rel_menu)
    LinearLayout under_menu;               // 全选——layout
    @BindView(R.id.pm_whole)
    LinearLayout lay_whole;                // 全选——layout
    @BindView(R.id.shopcar_check_all)
    ImageView check_all;                   // 全选按钮
    @BindView(R.id.pm_none_amount)
    TextView bill_amount;                 // 支付金额
    @BindView(R.id.pm_merge)
    TextView check_merge;                    // 合并
    private LinearLayoutManager linearLayoutManager;
    private List<PropertyPaymentInfo> list = new ArrayList<>();             // 定义账单数据集
    private List<PropertyPaymentInfo> lisCheck = new ArrayList<>();
    private int page = 1;                   // 数据页面

    private CommonAdapter<PropertyPaymentInfo> mAdapter;
    private Boolean mSelect = false;
    private double totalMoney = 0;
    private DecimalFormat df;

    public PaymentNoneFragment() {
    }

    public static PaymentNoneFragment newInstance() {
        Bundle args = new Bundle();
        PaymentNoneFragment fragment = new PaymentNoneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_none, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }


    @Override
    public void init() {
        super.init();
        initList();
        initRefresh();            // 刷新，加载
        df = new DecimalFormat("0.00");
        addRxBusSubscribe(PassValueEvent.class, new Action1<PassValueEvent>() {
            @Override
            public void call(PassValueEvent passValueEvent) {
                mSelect = passValueEvent.getSelect();
                mAdapter.notifyDataSetChanged();
                check_all.setSelected(false);
                if (passValueEvent.getSelect()) {
                    under_menu.setVisibility(View.VISIBLE);     // 下标题菜单
                } else {
                    under_menu.setVisibility(View.GONE);        // 隐藏下标题菜单
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelect(false);
                    }
                }
            }
        });
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
                if (mSelect) {
                    ivSelect.setVisibility(View.VISIBLE);
                } else {
                    ivSelect.setVisibility(View.GONE);
                }
                if (propertyPaymentInfo.isSelect) {
                    ivSelect.setSelected(true);
                } else {
                    ivSelect.setSelected(false);
                }
                ivSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (propertyPaymentInfo.isSelect) {
                            ivSelect.setSelected(false);
                            propertyPaymentInfo.setSelect(false);
                            lisCheck.remove(propertyPaymentInfo);
                            totalMoney = totalMoney - propertyPaymentInfo.money;
                            bill_amount.setText("￥"+df.format(totalMoney));
                            if (lisCheck.size() != list.size())
                                check_all.setSelected(false);
                        } else {
                            ivSelect.setSelected(true);
                            propertyPaymentInfo.setSelect(true);
                            lisCheck.add(propertyPaymentInfo);
                            totalMoney = totalMoney + propertyPaymentInfo.money;
                            bill_amount.setText("￥"+df.format(totalMoney));
                            if (lisCheck.size() == list.size())
                                check_all.setSelected(true);
                        }
                    }
                });

                double money = propertyPaymentInfo.money;
                String newMoney = df.format(money);
                viewHolder.setText(R.id.payment_bill, "￥" + newMoney);
                viewHolder.setText(R.id.payment_title, propertyPaymentInfo.name);
                RelativeLayout reContainer = viewHolder.getView(R.id.re_container);
                reContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PayMentPayActivity.toActivity(mActivity, propertyPaymentInfo.name, propertyPaymentInfo.id, "");
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
        Subscription subscription = RetrofitHelper.getInstance().getOrderUndone(page)
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

    @OnClick({R.id.shopcar_check_all, R.id.pm_merge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shopcar_check_all:
                if (check_all.isSelected()) {
                    check_all.setSelected(false);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelect(false);

                    }
                    lisCheck.removeAll(list);
                    mAdapter.notifyDataSetChanged();
                    totalMoney = 0;
                    bill_amount.setText("￥" + df.format(totalMoney));
                } else {
                    totalMoney = 0;
                    check_all.setSelected(true);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelect(true);
                        double money = list.get(i).money;
                        totalMoney = totalMoney + money;

                    }
                    lisCheck.clear();
                    lisCheck.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    bill_amount.setText("￥" + df.format(totalMoney));

                }
                break;
            case R.id.pm_merge:
                // 合并收费
                if (lisCheck != null && lisCheck.size() > 0) {
                    // 传值 list_check
                    Intent intent = new Intent(getContext(), PropertyBillAffirmActivity.class);       // 跳转确认账单提交页
                    intent.putExtra("checkList", (Serializable) lisCheck);
                    startActivity(intent);
                } else {
                    Toast.makeText(mActivity, "请选择账单", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

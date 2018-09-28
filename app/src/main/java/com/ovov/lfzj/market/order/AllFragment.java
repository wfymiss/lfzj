package com.ovov.lfzj.market.order;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.ShopListBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.market.order.bean.ShopBean;
import com.ovov.lfzj.user.fragment.SuggestFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends BaseFragment {
    Unbinder unbinder;

    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static Bundle args;
    @BindView(R.id.ry_content)
    ListView ryContent;
    @BindView(R.id.srly)
    SmartRefreshLayout mActivityListSwf;
    private List<ShopBean> shopBeanList;
    //    Unbinder unbinder;
    private CommonAdapter mAdapter;
    private int page;
    private String id;

    public static AllFragment newInstance(int columnCount) {
        AllFragment fragment = new AllFragment();
        args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        initview();
        //获取数据
        mActivityListSwf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
            }
        });
        mActivityListSwf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });
        mActivityListSwf.autoRefresh();
        return view;
    }


    private void initData(final int type) {
        Log.e("313131","有没有");
        if (type == REFRESH) {
            page = 1;
        } else if (type == LOADMORE) {
            page = page + 1;
        }

        Subscription subscription = RetrofitHelper.getInstance().getOrderList(page,
                ActivityUtils.status(getArguments().getInt(ARG_COLUMN_COUNT)))
                .compose(RxUtil.<ShopListBean<ShopBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ShopListBean<ShopBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (type == REFRESH) {
                            mActivityListSwf.finishRefresh(false);

                        } else {
                            mActivityListSwf.finishLoadmore(false);
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
                    public void onNext(ShopListBean<ShopBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            if (listInfoDataInfo.datas().size() < 10) {
                                mActivityListSwf.setEnableLoadmore(false);
                            }
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh(true);
                                shopBeanList.clear();
                                mAdapter.addDatas(listInfoDataInfo.datas());

                            } else {
                                mActivityListSwf.finishLoadmore(true);
                                mAdapter.addDatas(listInfoDataInfo.datas());
                            }
                        } else {
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh(false);

                            } else {
                                mActivityListSwf.finishLoadmore(false);
                            }
                        }


                    }
                });
        addSubscrebe(subscription);
    }

    private void initview() {
        shopBeanList = new ArrayList<>();
        mAdapter = new CommonAdapter<ShopBean>(getContext(), shopBeanList, R.layout.order_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, ShopBean shopBean, int i) {
                viewHolder.setText(R.id.tv_mine_name, shopBean.getServicestation());
                ImageView imageView = viewHolder.getView(R.id.iv_goods_im);
                if (!shopBean.getGoods_img().isEmpty()) {
                    Picasso.with(getContext()).load(shopBean.getGoods_img()).into(imageView);
                }
                viewHolder.setText(R.id.tv_order_detail, shopBean.getGoods_name());
                viewHolder.setText(R.id.tv_order_pay, "￥" + String.valueOf(shopBean.getPayprice()));
                viewHolder.setText(R.id.tv_money, "￥" + String.valueOf(shopBean.getPayprice()));
                viewHolder.setText(R.id.tv_order_numbs, "x" + String.valueOf(shopBean.getGoods_num()));
                viewHolder.setText(R.id.tv_status,String.valueOf(ActivityUtils.statusString(shopBean.getOrderstatus()))
                );
            }
        };
        ryContent.setAdapter(mAdapter);
    }

}

package com.ovov.lfzj.home;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.banner.BannerAdapter;
import com.ovov.lfzj.base.banner.BannerLayout;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.DateUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.NoScrollListView;
import com.ovov.lfzj.event.HomeIdentityEvent;
import com.ovov.lfzj.home.adapter.CardAdapter;
import com.ovov.lfzj.home.adapter.GirdAdapter;
import com.ovov.lfzj.home.adapter.HomeNeighAdapter;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.bean.SubdistrictsBean;
import com.ovov.lfzj.home.payment.activity.PayMentRecordActivity;
import com.ovov.lfzj.home.presenter.HomePresenter;
import com.ovov.lfzj.home.repair.RepairActivity;
import com.ovov.lfzj.home.ui.HomeNoticeActivity;
import com.ovov.lfzj.home.ui.MessageListActivity;
import com.ovov.lfzj.home.ui.NewsDetailActivity;
import com.ovov.lfzj.home.ui.NewsListctivity;
import com.ovov.lfzj.home.ui.NoticeDetailActivity;
import com.ovov.lfzj.home.ui.PaymentOtherActivity;
import com.ovov.lfzj.home.view.HomeView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.login.LoginActivity;
import com.ovov.lfzj.market.MarketActivity;
import com.ovov.lfzj.market.order.bean.ShopBean;
import com.ovov.lfzj.opendoor.ApplyVisitorActivity;
import com.ovov.lfzj.opendoor.adapter.HouseListAdapter;
import com.ovov.lfzj.opendoor.capture.CaptureActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.mobile.zanim.util.DateUtil;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.HOME_FRAGMENT_IDENTITY;
import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomeView {

    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    ListView mRecyclerView;
    @BindView(R.id.srly)
    SmartRefreshLayout mActivityListSwf;

    private TextView textView;
    private int page;
    private String id;
    //初始化数据
    private HomeNeighAdapter mAdapter;
    private List<SquareListInfo> datas = new ArrayList<>();
    private BannerAdapter<BannerBean> bannerAdapter;
    private List<String> bannerlist;
    private BannerLayout bannerLayout;
    private HomePresenter homePresenter;
    private List<String> list = new ArrayList();
    private List<NewsBean> newslist = new ArrayList<>();
    private List<NewsBean> noticeList = new ArrayList<>();
    private CommonAdapter<NewsBean> commonAdapter;
    private CardAdapter shopAdapter1;
    private CommonAdapter<NewsBean> newsAdapter;
    private List<SubdistrictsBean> listinfo1 = new ArrayList<>();
    private List<ShopBean> ima = new ArrayList();
    private LinearLayout basetitle;
    private TextView more;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        isLogin();
        initView();
        JPushInterface.setAlias(mActivity, 1, LoginUserBean.getInstance().getPhone());
        mActivityListSwf.setEnableLoadmore(false);
        mActivityListSwf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
                initBanner();
                homePresenter.getNoticeList();
                homePresenter.getNewsList();
                homePresenter.gethomeList();
            }
        });
        mActivityListSwf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });

        mActivityListSwf.autoRefresh();

        addRxBusSubscribe(HomeIdentityEvent.class, new Action1<HomeIdentityEvent>() {
            @Override
            public void call(HomeIdentityEvent homeIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
            }
        });
    }

    private void initView() {
        homePresenter = new HomePresenter(this);
        homePresenter.gethomeList();
        initList();
        homePresenter.getNoticeList();
        homePresenter.getNewsList();
        homePresenter.getShopList();
        initBanner();

    }

    private void initBanner() {
        Subscription subscription = RetrofitHelper.getInstance().getBanner()
                .compose(RxUtil.<ListInfo<BannerBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<BannerBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            //   doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<BannerBean> listInfoDataInfo) {
                        bannerAdapter.reset(listInfoDataInfo.datas());


                    }

                });
        addSubscrebe(subscription);


    }

    private void initList() {
        newslist = new ArrayList<>();
        View addheadlayout = View.inflate(getContext(), R.layout.banner_item, null);
        textView = addheadlayout.findViewById(R.id.im_home);
        ImageView imageView = addheadlayout.findViewById(R.id.im_list);
        basetitle = addheadlayout.findViewById(R.id.relativeLayout);
        more = addheadlayout.findViewById(R.id.tv_more);
        GridView gridView = addheadlayout.findViewById(R.id.gridview);
        RecyclerView gridView1 = addheadlayout.findViewById(R.id.gridview1);
        NoScrollListView newListView = addheadlayout.findViewById(R.id.new_list);
        NoScrollListView notice_list = addheadlayout.findViewById(R.id.notice_list);
        ImageView scan = addheadlayout.findViewById(R.id.im_scan);
        bannerLayout = addheadlayout.findViewById(R.id.banner);
        if (LoginUserBean.getInstance().getSubname() != null && !LoginUserBean.getInstance().getSubname().isEmpty()) {
            textView.setText(LoginUserBean.getInstance().getSubname());
        } else {
            textView.setText("请选择");
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.isFastClick()) {
                    MessageListActivity.toActivity(mActivity);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gridView1.setLayoutManager(linearLayoutManager);
        shopAdapter1 = new CardAdapter(ima, getContext());


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketActivity.toActivity(mActivity, "https://h5.youzan.com/v2/showcase/tag?alias=fruuoqig&reft=1541730182062&spm=f76060277", 1);
            }
        });


        bannerAdapter = new BannerAdapter<BannerBean>() {
            @Override
            protected void bind(ViewHolder holder, BannerBean data) {
                if (!data.getImg().equals("") && data.getImg() != null) {
                    Picasso.with(getContext()).load(data.getImg()).placeholder(R.mipmap.error_banner).error(R.mipmap.error_banner).into(holder.mImageView);
                } else {
                    Picasso.with(getContext()).load(R.mipmap.error_banner).into(holder.mImageView);
                }

                holder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MarketActivity.toActivity(mActivity, data.getUrl(), 1);
                    }
                });
            }
        };

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (UIUtils.isFastClick()) {

                    if (LoginUserBean.getInstance().isIs_auth()) {
                        if (position == 0) {
                            PayMentRecordActivity.toActivity(mActivity);

                        }
                        if (position == 1) {
                            RepairActivity.toActivity(mActivity);

                        }
                        if (position == 2) {
                            //RepairCommentActivity.toActivity(mActivity);
                            ApplyVisitorActivity.toActivity(mActivity);
                        }

                        if (position == 3) {
                            PaymentOtherActivity.toActivity(mActivity);
                        }
                        if (position == 4) {
                            GameActivity.toActivity(mActivity);

                        }
                    } else {
                        IdentityDialog identityDialog = new IdentityDialog(mActivity, HOME_FRAGMENT_IDENTITY);
                        identityDialog.show();
                    }
                }

            }
        });

        commonAdapter = new CommonAdapter<NewsBean>(getContext(), newslist, R.layout.news_item) {

            @Override
            public void convert(ViewHolder viewHolder, NewsBean noticeBean, final int i) {
                LinearLayout reHeader = viewHolder.getView(R.id.relativeLayout);
                if (i == 0) {

                    reHeader.setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_item_title, "社区公告");
                    viewHolder.setImageResource(R.id.item_im, R.mipmap.item_noti);
                } else {
                    reHeader.setVisibility(View.GONE);
                }
                viewHolder.setText(R.id.tv_notifi_type, "公告");
                viewHolder.setText(R.id.tv_title, noticeBean.getTitle());
                viewHolder.setText(R.id.tv_comment, noticeBean.getSummary());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date strtodate;
                String datas = "";

                try {
                    strtodate = formatter.parse(noticeBean.getCreated_at());
                    datas = formatter.format(strtodate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                viewHolder.setText(R.id.tv_time, datas);

                ImageView view = viewHolder.getView(R.id.iv_image);

                if (noticeBean.getImages().size() > 0) {
                    if (!noticeBean.getImages().get(0).isEmpty()) {
                        Picasso.with(getContext()).load(noticeBean.getImages().get(0)).error(R.drawable.meinv).into(view);
                    }
                } else {
                    view.setVisibility(View.GONE);
                }

                viewHolder.setOnClickListener(R.id.ly_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UIUtils.isFastClick()) {
                            Intent intent = new Intent(getContext(), NoticeDetailActivity.class);
                            intent.putExtra("id", newslist.get(i).getId());
                            startActivity(intent);
                        }

                    }
                });
                viewHolder.setOnClickListener(R.id.delect_im, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UIUtils.isFastClick()) {
                            newslist.remove(i);
                            notifyDataSetChanged();
                        }
                    }
                });

                viewHolder.setOnClickListener(R.id.tv_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeNoticeActivity.toActivity(mActivity);

                    }
                });


            }
        };
    newsAdapter =new CommonAdapter<NewsBean>(getContext(),noticeList,R.layout.news_item) {
        @Override
        public void convert (ViewHolder viewHolder, NewsBean noticeBean,final int i){
        if (i == 0) {
            viewHolder.setVisible(R.id.relativeLayout, true);
            viewHolder.setText(R.id.tv_item_title, "新闻");
            viewHolder.setImageResource(R.id.item_im, R.mipmap.item_news);
        } else {
            viewHolder.setVisible(R.id.relativeLayout, false);
        }
        viewHolder.setText(R.id.tv_title, noticeBean.getTitle());
        viewHolder.setText(R.id.tv_comment, noticeBean.getSummary());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate;
        String datas = "";
        try {
            strtodate = formatter.parse(noticeBean.getCreated_at());
            datas = formatter.format(strtodate);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        viewHolder.setText(R.id.tv_time, datas);
        viewHolder.setOnClickListener(R.id.ly_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.isFastClick()) {
                    Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                    intent.putExtra("id", noticeList.get(i).getId());
                    startActivity(intent);
                }
            }
        });
        viewHolder.setOnClickListener(R.id.delect_im, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.isFastClick()) {
                    noticeList.remove(i);
                    notifyDataSetChanged();
                }
            }
        });

        viewHolder.setOnClickListener(R.id.tv_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsListctivity.toActivity(mActivity);

            }
        });
        ImageView view = viewHolder.getView(R.id.iv_image);
        if (noticeBean.getImages().size() > 0) {
            Picasso.with(getContext()).load(noticeBean.getImages().get(0)).into(view);
        } else {
            view.setVisibility(View.GONE);
        }

    }
    }

    ;

        newListView.setAdapter(commonAdapter);
        notice_list.setAdapter(newsAdapter);
        scan.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivity(intent);

    }
    });


        gridView1.setAdapter(shopAdapter1);
        bannerLayout.setAdapter(bannerAdapter);
    GirdAdapter adapter = new GirdAdapter(getContext());
        gridView.setAdapter(adapter);
        mRecyclerView.addHeaderView(addheadlayout);
    mAdapter =new

    HomeNeighAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        /*mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (UIUtils.isFastClick()) {
                    SquareDetailActivity.toActivity(getContext(), datas.get(position).id, position);
                }

            }
        });*/
        textView.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        if (UIUtils.isFastClick()) {
            if (LoginUserBean.getInstance().isIs_auth()) {

                initpopuwindow(textView);
            } else {

                IdentityDialog identityDialog = new IdentityDialog(mActivity, HOME_FRAGMENT_IDENTITY);
                identityDialog.show();
            }
        }

    }
    });

}

    private void initData(final int type) {
        if (type == REFRESH) {
            page = 1;
            id = "";
        } else if (type == LOADMORE) {
            page = page + 1;
            id = mAdapter.getData().get((page - 1) * 10 - 1).id;
        }

        Subscription subscription = RetrofitHelper.getInstance().getHomeSquareList(page, id)
                .compose(RxUtil.<ListInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SquareListInfo>>() {
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
                    public void onNext(ListInfo<SquareListInfo> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            if (listInfoDataInfo.datas().size() < 0) {
                                mActivityListSwf.setEnableLoadmore(false);
                            }
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh(true);
                                mAdapter.reset(listInfoDataInfo.datas());

                            } else {
                                datas.addAll(listInfoDataInfo.datas());
                                mActivityListSwf.finishLoadmore(true);
                                mAdapter.addAll(listInfoDataInfo.datas());
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


    private void isLogin() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().isLogin()
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(R.string.text_login_invaild);
                            LoginUserBean.getInstance().reset();
                            LoginUserBean.getInstance().save();
                            LoginActivity.toActivity(mActivity);
                            YouzanSDK.userLogout(mActivity);
                            mActivity.finish();
                        } else {

                            //    doFailed();
                        }


                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    private void initpopuwindow(final TextView textView) {
        View view = View.inflate(getContext(), R.layout.popup_activity_title1, null);
        ListView lv_appointment = (ListView) view.findViewById(R.id.activity_title_recy);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HouseListAdapter adapter = new HouseListAdapter(list, getContext());
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
        lv_appointment.setAdapter(adapter);
        lv_appointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(list.get(position));
                popupWindow.dismiss();
                LoginUserBean.getInstance().setSub_id(listinfo1.get(position).getSubdistrict_id());
                LoginUserBean.getInstance().setSubname(listinfo1.get(position).getSubdistrict_name());
                LoginUserBean.getInstance().save();
                initData(REFRESH);
                initBanner();
                homePresenter.getNoticeList();
                homePresenter.getNewsList();
                homePresenter.gethomeList();

            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });


        // 使其聚集,可点击
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        //  popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);


    }

    @Override
    public void notifiData(List<NewsBean> listInfo) {
        newslist.clear();
        newslist.addAll(listInfo);
        commonAdapter.notifyDataSetChanged();
    }


    @Override
    public void NewsData(List<NewsBean> listInfo) {
        noticeList.clear();
        noticeList.addAll(listInfo);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void getsubList(List<SubdistrictsBean> listInfo) {
        listinfo1 = listInfo;
        list.clear();
        if (LoginUserBean.getInstance().isIs_auth()) {
            if (LoginUserBean.getInstance().getSubname() == null || LoginUserBean.getInstance().getSubname().isEmpty()) {
                LoginUserBean.getInstance().setSubname(listInfo.get(0).getSubdistrict_name());
                LoginUserBean.getInstance().save();
                textView.setText(listinfo1.get(0).getSubdistrict_name());
            }

            if (LoginUserBean.getInstance().getSub_id() == null || LoginUserBean.getInstance().getSub_id().isEmpty()) {
                LoginUserBean.getInstance().setSub_id(listInfo.get(0).getSubdistrict_id());
                LoginUserBean.getInstance().save();
            }
        }

        for (SubdistrictsBean s : listInfo) {
            list.add(s.getSubdistrict_name());

        }

    }

    @Override
    public void getShopList(List<ShopBean> listInfo) {
        if (listInfo.size() > 0) {
            shopAdapter1.setmData(listInfo);
        } else {
            bannerLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void showMsg(String dataResult) {

    }

}

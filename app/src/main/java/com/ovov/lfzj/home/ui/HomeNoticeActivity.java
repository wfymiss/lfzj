package com.ovov.lfzj.home.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

public class HomeNoticeActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout mActivityListSwf;
    int page = 0;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private CommonAdapter<NewsBean> newsAdapter;
    private List<NewsBean> newslist = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_notice;
    }

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, HomeNoticeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void init() {
        tvTitle.setText("公告列表");
        initView();
        mActivityListSwf.setEnableLoadmore(false);
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


    }

    private void initView() {
        newsAdapter = new CommonAdapter<NewsBean>(this, newslist, R.layout.news_item) {


            @Override
            public void convert(ViewHolder viewHolder, NewsBean noticeBean, final int i) {
                viewHolder.setVisible(R.id.relativeLayout, false);
                viewHolder.setVisible(R.id.tv_notifi_type, false);
                viewHolder.setText(R.id.tv_title, noticeBean.getTitle());
                viewHolder.setText(R.id.tv_comment, noticeBean.getSummary());
                viewHolder.setText(R.id.tv_time, noticeBean.getCreated_at());
                ImageView view = viewHolder.getView(R.id.iv_image);
                if (noticeBean.getImages().size() > 0) {
                    if (!noticeBean.getImages().get(0).isEmpty()) {
                        Picasso.with(mActivity).load(noticeBean.getImages().get(0)).error(R.drawable.meinv).into(view);
                    }
                } else {
                    view.setVisibility(View.GONE);
                }

            }


        };

        lv.setAdapter(newsAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, NoticeDetailActivity.class);
                intent.putExtra("id", newslist.get(position).getId());
                startActivity(intent);
            }
        });

    }


    private void initData(final int type) {
        if (type == REFRESH) {
            page = 1;
        } else if (type == LOADMORE) {
            page = page + 1;
        }
        Subscription subscription = RetrofitHelper.getInstance().getHomeNoticeList(page)
                .compose(RxUtil.<ListInfo<NewsBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<NewsBean>>() {
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
                    public void onNext(ListInfo<NewsBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            if (listInfoDataInfo.datas().size() == 0) {
                                mActivityListSwf.setEnableLoadmore(false);
                            }
                            if (type == REFRESH) {
                                newslist.clear();
                                newslist.addAll(listInfoDataInfo.datas());
                                mActivityListSwf.finishRefresh(true);
                                newsAdapter.setDatas(listInfoDataInfo.datas());

                            } else {
                                newslist.addAll(listInfoDataInfo.datas());
                                mActivityListSwf.finishLoadmore(true);
                                newsAdapter.addDatas(listInfoDataInfo.datas());
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


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

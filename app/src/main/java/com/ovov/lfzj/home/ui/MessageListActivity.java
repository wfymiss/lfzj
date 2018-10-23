package com.ovov.lfzj.home.ui;

import android.content.Context;
import android.content.Intent;
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
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.home.bean.BannerBean;
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

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.recyclerview)
    ListView recyclerview;
    @BindView(R.id.srly)
    SmartRefreshLayout mActivityListSwf;
    List<BannerBean> noticeList = new ArrayList<>();
    private CommonAdapter newsAdapter;
    private int page;
    private String id;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    public void init() {
        tvTitle.setText("消息通知");
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.list_serch);

        initList();

        mActivityListSwf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
        mActivityListSwf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData();
            }
        });

        mActivityListSwf.autoRefresh();


        recyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String index_id= noticeList.get(position).getId();
               MessageDetailActivity.toActivity(MessageListActivity.this,index_id);
            }
        });

    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getinfomationlist()
                .compose(RxUtil.<ListInfo<BannerBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<BannerBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        mActivityListSwf.finishLoadmore(false);
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<BannerBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {

                            mActivityListSwf.finishLoadmore(true);
                            mActivityListSwf.finishRefresh(true);
                            newsAdapter.setDatas(listInfoDataInfo.datas());

                        }
                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {
        newsAdapter = new CommonAdapter<BannerBean>(this, noticeList, R.layout.message_list_item) {


            @Override
            public void convert(ViewHolder viewHolder, BannerBean noticeBean, final int i) {


                viewHolder.setText(R.id.iv_bbs, noticeBean.getTitle());
                viewHolder.setText(R.id.tv_comment, noticeBean.getMessage());
                viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                switch (noticeBean.getId()) {
                    case "1":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_bbs);
                        break;
                    case "2":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_activity);
                        break;
                    case "3":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_shop);
                        break;
                    case "4":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_system);
                        break;
                    case "5":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_education);
                        break;
                    case "6":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_sevice);
                        break;
                    case "7":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_job);
                        break;
                    case "8":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_mine);
                        break;
                    case "9":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_unitiy);
                        break;
                    case "10":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_message);
                        break;
                    case "11":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_notifi);
                        break;
                    case "12":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_order);
                        break;
                    case "13":
                        viewHolder.setImageResource(R.id.bbs_im, R.mipmap.list_bbs);
                        break;


                }

            }
        };


        recyclerview.setAdapter(newsAdapter);

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

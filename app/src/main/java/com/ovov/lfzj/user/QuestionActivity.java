package com.ovov.lfzj.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.user.activity.QuestionInfoActivity;
import com.ovov.lfzj.user.setting.SettingActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

public class QuestionActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;

    @BindView(R.id.lin_title)
    FrameLayout linTitle;
    private CommonAdapter newsAdapter;
    private List<SquareListInfo> datas = new ArrayList<>();
    private int page;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, QuestionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_question;
    }

    @Override
    public void init() {
        tvTitle.setText("常见问题解答");
        initList();
        smartrefresh.setEnableLoadmore(false);
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
                //  initBanner();
            }
        });
        smartrefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });

        smartrefresh.autoRefresh();

    }

    private void initData(final int type) {
        if (type == REFRESH) {
            page = 1;
        } else if (type == LOADMORE) {
            page = page + 1;
        }

        Subscription subscription = RetrofitHelper.getInstance().questionLists(page)
                .compose(RxUtil.<ListInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SquareListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (type == REFRESH) {
                            smartrefresh.finishRefresh(false);

                        } else {
                            smartrefresh.finishLoadmore(false);
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
                                smartrefresh.setEnableLoadmore(false);
                            }
                            if (type == REFRESH) {
                                smartrefresh.finishRefresh(true);
                                newsAdapter.setDatas(listInfoDataInfo.datas());

                            } else {
                                datas.addAll(listInfoDataInfo.datas());
                                smartrefresh.finishLoadmore(true);
                                newsAdapter.addDatas(listInfoDataInfo.datas());
                            }
                        } else {
                            if (type == REFRESH) {
                                smartrefresh.finishRefresh(false);

                            } else {
                                smartrefresh.finishLoadmore(false);
                            }
                        }


                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {
        newsAdapter = new CommonAdapter<SquareListInfo>(this, datas, R.layout.item_qusetion_list) {


            @Override
            public void convert(ViewHolder viewHolder,SquareListInfo noticeBean, final int i) {
                viewHolder.setText(R.id.tv_title_que, noticeBean.getTitle());
                viewHolder.setText(R.id.tv_content, noticeBean.getBrief());
                viewHolder.setText(R.id.tv_id, noticeBean.getStr());
            }
        };
        lv.setAdapter(newsAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionInfoActivity.toActivity(mActivity,datas.get(position).getId());
            }
        });

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}

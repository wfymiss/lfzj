package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.IdentityEvent;
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
import rx.functions.Action1;

public class SelectSubActivity extends BaseActivity {

    @BindView(R.id.list_sub)
    ListView mListSub;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    List<SublistInfo> mData = new ArrayList<>();
    private CommonAdapter<SublistInfo> mAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SelectSubActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_sub;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_select_sub);
        initList();

        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
        mRefresh.autoRefresh();
        mRefresh.setEnableLoadmore(false);
        addRxBusSubscribe(IdentityEvent.class, new Action1<IdentityEvent>() {
            @Override
            public void call(IdentityEvent identityEvent) {
                finish();
            }
        });
    }

    private void initData() {
        mRefresh.finishRefresh();
        Subscription subscription = RetrofitHelper.getInstance().getSubList()
                .compose(RxUtil.<ListInfo<SublistInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SublistInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ListInfo<SublistInfo> sublistInfoListInfo) {
                        dismiss();
                        mData.clear();
                        mData.addAll(sublistInfoListInfo.datas());
                        mAdapter.notifyDataSetChanged();

                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {
        mAdapter = new CommonAdapter<SublistInfo>(mActivity,mData, R.layout.item_sub) {
            @Override
            public void convert(ViewHolder viewHolder, final SublistInfo sublistInfo, int i) {
                viewHolder.setText(R.id.tv_sub,sublistInfo.subdistrict_name);
                RelativeLayout reContainer = viewHolder.getView(R.id.container);
                reContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectUnitActivity.toActivity(mActivity,sublistInfo.id,sublistInfo.str_id);
                    }
                });

            }
        };
        mListSub.setAdapter(mAdapter);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.UnitInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
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

public class SelectUnitActivity extends BaseActivity {

    @BindView(R.id.list_sub)
    ListView mListSub;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    List<UnitInfo> mData = new ArrayList<>();
    private String sub_id;
    private String subStrid;
    private CommonAdapter<UnitInfo> mAdapter;

    public static void toActivity(Context context, String sub_id,String subStrid) {

        Intent intent = new Intent(context, SelectUnitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sub_id", sub_id);
        bundle.putString("subStrid",subStrid);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_unit;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_select_bunilding);
        setRightText("放弃认证");
        Bundle bundle = getIntent().getExtras();
        sub_id = bundle.getString("sub_id");
        subStrid = bundle.getString("subStrid");
        mRefresh.setEnableLoadmore(false);
        initList();
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
        mRefresh.autoRefresh();
        addRxBusSubscribe(IdentityEvent.class, new Action1<IdentityEvent>() {
            @Override
            public void call(IdentityEvent identityEvent) {
                finish();
            }
        });
    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getUnit(sub_id)
                .compose(RxUtil.<ListInfo<UnitInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<UnitInfo>>() {
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
                    public void onNext(ListInfo<UnitInfo> unitInfoListInfo) {
                        mRefresh.finishRefresh(true);
                        Log.e("unitcd",unitInfoListInfo.datas().size()+"");
                        mData.addAll(unitInfoListInfo.datas());
                        mAdapter.notifyDataSetChanged();
                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {
        mAdapter = new CommonAdapter<UnitInfo>(mActivity,mData, R.layout.item_unit) {
            @Override
            public void convert(ViewHolder viewHolder, final UnitInfo unitInfo, int i) {
                viewHolder.setText(R.id.tv_building,unitInfo.buildings);
                NoScrollGridView mListChild = viewHolder.getView(R.id.gridView);
                List<UnitInfo.BuildingSunBean> mGridData = new ArrayList<>();
                mGridData.addAll(unitInfo.building_sun);
                CommonAdapter<UnitInfo.BuildingSunBean> mGridAdapter = new CommonAdapter<UnitInfo.BuildingSunBean>(mActivity,mGridData,R.layout.item_unit_child) {
                    @Override
                    public void convert(ViewHolder viewHolder, final UnitInfo.BuildingSunBean buildingSunBean, int i) {
                        viewHolder.setText(R.id.tv_unit,buildingSunBean.building_unit_name);
                        RelativeLayout reContainer = viewHolder.getView(R.id.container);
                        reContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SelectRoomActivity.toActivity(mActivity,sub_id,subStrid,String.valueOf(unitInfo.id),String.valueOf(buildingSunBean.building_unit_id));
                            }
                        });
                    }
                };
                mListChild.setAdapter(mGridAdapter);
            }
        };
        mListSub.setAdapter(mAdapter);
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                MainActivity.toActivity(mActivity);
                RxBus.getDefault().post(new IdentityEvent());
                finish();

                break;
        }
    }
}

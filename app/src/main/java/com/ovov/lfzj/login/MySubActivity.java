package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.StatusBarUtils;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.event.IdentitySuccessEvent;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.bean.SubdistrictsBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class MySubActivity extends BaseActivity {

    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.tv_null)
    TextView mTvNull;
    @BindView(R.id.list_sub)
    ListView mListSub;

    @BindView(R.id.sitview)
    View sitview;
    private List<SubdistrictsBean> mData;
    private CommonAdapter<SubdistrictsBean> mAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MySubActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_sub;
    }

    @Override
    public void init() {
        setTitleText("我的小区");
        mIvRight.setImageResource(R.mipmap.ic_add_sub);
        mIvRight.setVisibility(View.VISIBLE);
        StatusBarUtils.setStatusBar(this, false, false);
        initSitView();
        initList();
        getUserInfo();
        addRxBusSubscribe(IdentityEvent.class, new Action1<IdentityEvent>() {
            @Override
            public void call(IdentityEvent identityEvent) {
                finish();
            }
        });

    }

    private void initList() {
        mData = new ArrayList<>();
        mAdapter = new CommonAdapter<SubdistrictsBean>(mActivity, mData, R.layout.item_my_sub) {
            @Override
            public void convert(ViewHolder viewHolder, SubdistrictsBean subdistrictsBean, int i) {
                NoScrollGridView mHouseGrid = viewHolder.getView(R.id.grid_house);
                viewHolder.setText(R.id.tv_sub_name,subdistrictsBean.getSubdistrict_name());
                List<SubdistrictsBean.housesBean> mGridData = subdistrictsBean.getHouses();
                CommonAdapter<SubdistrictsBean.housesBean> mGridAdapter = new CommonAdapter<SubdistrictsBean.housesBean>(mActivity,mGridData,R.layout.item_house) {
                    @Override
                    public void convert(ViewHolder viewHolder, SubdistrictsBean.housesBean housesBean, int i) {
                        viewHolder.setText(R.id.tv_house_name,housesBean.getBuilding_name()+"号楼"+housesBean.getUnit()+"单元"+housesBean.getNumber()+"室");
                    }
                };
                mHouseGrid.setAdapter(mGridAdapter);
            }
        };
        mListSub.setAdapter(mAdapter);
    }

    private void getUserInfo() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().gethomeList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SubListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SubListBean subListBean) {
                        dismiss();
                        if (subListBean.getDatas().getSubdistricts().size() > 0){
                            mData.addAll(subListBean.getDatas().getSubdistricts());
                            mAdapter.notifyDataSetChanged();
                        }else {
                            mTvNull.setVisibility(View.VISIBLE);
                        }
                    }
                });
        addSubscrebe(subscription);
    }
    @OnClick({R.id.iv_back, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                IdentityConfirmActivity.toActivity(mActivity);
                break;
        }
    }
    // 获取占位视图高度
    private void initSitView() {
        // 获取占位视图高度
        ViewGroup.LayoutParams sitParams = sitview.getLayoutParams();
        sitParams.height = StatusBarUtils.getStatusBarHeight(mActivity);
    }

    @Override
    public boolean isHideNavigation() {
        return true;
    }
}

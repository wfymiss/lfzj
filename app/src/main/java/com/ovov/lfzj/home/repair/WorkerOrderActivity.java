package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;
import com.ovov.lfzj.neighbour.square.SquareFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkerOrderActivity extends BaseActivity {

    @BindView(R.id.woker_order_tablayout)
    TabLayout mWokerOrderTablayout;
    @BindView(R.id.worker_order_viewpager)
    ViewPager mWorkerOrderViewpager;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, WorkerOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_worker_order;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_my_workorder);
        initTab();
    }

    private void initTab() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.text_pending_workorder));
        titles.add(getString(R.string.text_in_hand));
        titles.add(getString(R.string.text_has_complete));
        for (int i = 0; i < titles.size(); i++) {
            mWokerOrderTablayout.addTab(mWokerOrderTablayout.newTab().setText(titles.get(i)));
        }
        mWokerOrderTablayout.setTabMode(TabLayout.GRAVITY_CENTER);   //  标题居中
        List<Fragment> list = new ArrayList<>();
        list.add(PendingWorkorderFragment.newInstance());
        list.add(InHandFragment.newInstance());
        list.add(HasCompleteFragment.newInstance());
        // list.add(new ActivityFragment());
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(getSupportFragmentManager(), list, titles);   //绑定fragment
        mWorkerOrderViewpager.setOffscreenPageLimit(titles.size());
        mWorkerOrderViewpager.setAdapter(adapter);
        mWokerOrderTablayout.setupWithViewPager(mWorkerOrderViewpager);  //标题与页面同步

        //切换活动论坛页面
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

package com.ovov.lfzj.home.repair.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.home.repair.adapter.WorkerOrderAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *  业主工单列表
 *  lyy 2017/08/01
 */
public class WorkerOrderActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.worker_order_viewpager)
    ViewPager viewPager;
    @BindView(R.id.woker_order_tablayout)
    TabLayout tabLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_worker_order;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_repair_list);
        WorkerOrderAdapter adapter=new WorkerOrderAdapter(getSupportFragmentManager(),this);
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);     //标题居中
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);             // 标题与页面同步
    }

}

package com.ovov.lfzj.market.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.user.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.tab_menu_order)
    TabLayout tabMenuOrder;
    @BindView(R.id.vp_content)
    ViewPager contentFg;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void init() {
        tvTitle.setText("订单");
        // 初始化数据
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
        String[] tabslist = {"全部", "待付款", "待发货", "待收货", "待评价"};
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), tabslist, fragmentList);
        contentFg.setAdapter(fragmentAdapter);
        tabMenuOrder.setupWithViewPager(contentFg);
    }




    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

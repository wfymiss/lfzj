package com.ovov.lfzj.home.payment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *  fragment 分页适配器（我的活动，投诉、建议（业主、管家），家政服务，缴费,优惠劵，商家优惠劵管理）
 * Created by lyy on 2017/5/2 0002.
 */

public class SwitchingFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> listTitle = new ArrayList<>();

    public SwitchingFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> listTitle) {
        super(fm);
        this.fragmentList=fragments;
        this.listTitle = listTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override   //标题与页面同步
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}

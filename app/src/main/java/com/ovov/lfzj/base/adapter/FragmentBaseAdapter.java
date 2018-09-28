package com.ovov.lfzj.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * （fragment 页面切换）选择适配器————工单报修方法，社交（切换）
 * Created by 刘永毅 on 2017/7/31.
 */

public class FragmentBaseAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;  //数据源
    private List<String> titles;

    public FragmentBaseAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        if (fragments!=null){
            this.fragments = fragments;
            this.titles = titles;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override    //标题与页面同步
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

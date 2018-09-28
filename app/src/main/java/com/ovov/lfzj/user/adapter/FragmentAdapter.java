package com.ovov.lfzj.user.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ovov.lfzj.market.order.AllFragment;
import com.ovov.lfzj.user.fragment.SuggestFragment;

import java.util.List;


public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String[] mTabIds;

    public FragmentAdapter(FragmentManager supportFragmentManager,  String[] tabIds, List<Fragment> fragments) {
        super(supportFragmentManager);
        this.mFragments = fragments;
        this.mTabIds = tabIds;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public Fragment getItem(int position) {
        return AllFragment.newInstance(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabIds[position];
    }
}

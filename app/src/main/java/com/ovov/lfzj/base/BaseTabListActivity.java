package com.ovov.lfzj.base;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.ovov.lfzj.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by jzxiang on 17/03/2018.
 */

public abstract class BaseTabListActivity extends BaseActivity {

    @BindView(R.id.tab_order)
    TabLayout mTabOrder;
    @BindView(R.id.order_vp)
    ViewPager mVpOrder;

    @Override
    public void init() {
        initTab();
    }

    private void initTab() {
        String[] tabIds = getTabStringIds();
        for (int i = 0; i < tabIds.length; i++) {
            mTabOrder.addTab(mTabOrder.newTab().setText(tabIds[i]));
        }

        FragmentManager manager = getSupportFragmentManager();
        BaseTabViewPagerAdapter adapter = new BaseTabViewPagerAdapter(manager, getFragments(), tabIds);
        mVpOrder.setOffscreenPageLimit(tabIds.length);

        mVpOrder.setAdapter(adapter);
        mTabOrder.setupWithViewPager(mVpOrder);
    }

    public abstract String[] getTabStringIds();

    public abstract List<Fragment> getFragments();


}

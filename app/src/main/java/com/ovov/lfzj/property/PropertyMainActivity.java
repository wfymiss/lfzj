package com.ovov.lfzj.property;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseMainActivity;
import com.ovov.lfzj.market.MarketFragment;
import com.ovov.lfzj.neighbour.NeighbourFragment;
import com.ovov.lfzj.property.home.HomeFragment;
import com.ovov.lfzj.property.user.PropertyUserFragment;
import com.ovov.lfzj.property.user.UserFragment;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

import static com.ovov.lfzj.MainActivity.BASE_FILE;

public class PropertyMainActivity extends BaseMainActivity {

    public static void toActivity(Context context){
        Intent intent = new Intent(context,PropertyMainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void init() {
        super.init();
        FileUtils.createOrExistsDir(BASE_FILE);
        initFragment(0);
        switchContent(1, 0);
        for (int i = 0; i < getFragmentCount(); i++) {
            mBottomViews[i] = findViewById(mMenuIds[i]);
            final int finalI = i;
            mBottomViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuItemClicked(finalI);
                }
            });
        }

        mBottomViews[0].setSelected(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_property_main;
    }

    @Override
    public int getFragmentCount() {
        return 2;
    }

    @Override
    public int getDefaultIndex() {
        return 0;
    }

    @Override
    public void initFragment(int index) {
        if (mFragments[index] != null)
            return;

        switch (index) {
            case 0:
                mFragments[index] = HomeFragment.newInstance();
                break;
            case 1:
                mFragments[index] = PropertyUserFragment.newInstance();
                break;
        }
    }

    @Override
    public int[] getMenuIds() {
        return new int[]{
                R.id.tv_home,
                R.id.tv_user
        };
    }

    @Override
    public boolean menuClicked(int index) {
        return false;
    }
}

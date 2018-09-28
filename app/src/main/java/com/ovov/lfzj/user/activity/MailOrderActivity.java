package com.ovov.lfzj.user.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ovov.lfzj.R;
import com.ovov.lfzj.user.adapter.FragmentAdapter;


public class MailOrderActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mail_order);
//   Toolbar toolbar = (Toolbar) findViewById(R.id.hmm_com_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.vp);
//    setSupportActionBar(toolbar);
//    getSupportActionBar().setDisplayShowTitleEnabled(false);
//    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),2, fragmentList);
//        mViewPager.setAdapter(fragmentAdapter);
//        mTabLayout.addTab(mTabLayout.newTab().setText("待接单(1)"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("已接单"));
//        mTabLayout.setupWithViewPager(mViewPager);
    }

}

package com.ovov.lfzj.user.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ovov.lfzj.R;
import com.ovov.lfzj.user.adapter.FragmentAdapter;

public class MessageActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.hmm_com_toolbar);
//        mTabLayout = (TabLayout) findViewById(R.id.tab);
//        mViewPager = (ViewPager) findViewById(R.id.vp);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),1, fragmentList);
//        mViewPager.setAdapter(fragmentAdapter);
//        mTabLayout.addTab(mTabLayout.newTab().setText("未回复"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("已回复"));
//        mTabLayout.setupWithViewPager(mViewPager);

    }
}

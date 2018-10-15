package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.user.adapter.FragmentAdapter;
import com.ovov.lfzj.user.fragment.FamilyFragment;
import com.ovov.lfzj.user.fragment.OwnerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FamilyActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tab_order)
    TabLayout tabOrder;
    @BindView(R.id.order_vp)
    ViewPager orderVp;


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, FamilyActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_family;
    }

    @Override
    public void init() {
        tvTitle.setText("邀请认证");
        // 初始化数据
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FamilyFragment());
        fragmentList.add(new OwnerFragment());
        String[] tabslist = {"邀请业主", "邀请家人"};
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), tabslist, fragmentList);
        orderVp.setAdapter(fragmentAdapter);
        tabOrder.setupWithViewPager(orderVp);
    }
}

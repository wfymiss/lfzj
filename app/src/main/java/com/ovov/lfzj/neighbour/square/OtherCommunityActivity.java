package com.ovov.lfzj.neighbour.square;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;


import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherCommunityActivity extends BaseActivity {

    @BindView(R.id.iv_header)
    CircleImageView mIvHeader;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
    @BindView(R.id.neigh_tablayout)
    TabLayout mNeighTablayout;
    @BindView(R.id.viewpage)
    ViewPager mViewpage;
    private List<String> titles;
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, OtherCommunityActivity.class);
        context.startActivity(intent);
    }




    @Override
    public int getLayoutId() {
        return R.layout.activity_my_community;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_my_community);
        initTab();
    }
    private void initTab() {
        titles = new ArrayList<>();
        titles.add("广场");
        titles.add("活动");
        for (int i = 0; i < titles.size(); i++) {
            mNeighTablayout.addTab(mNeighTablayout.newTab().setText(titles.get(i)));
        }
        mNeighTablayout.setTabMode(TabLayout.GRAVITY_CENTER);   //  标题居中
        List<Fragment> list = new ArrayList<>();
        list.add(new MySquareFragment());
        list.add(new MyActivityFragment());
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(this.getSupportFragmentManager(), list, titles);   //绑定fragment
        mViewpage.setAdapter(adapter);
        mNeighTablayout.setupWithViewPager(mViewpage);  //标题与页面同步

    }
    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


}

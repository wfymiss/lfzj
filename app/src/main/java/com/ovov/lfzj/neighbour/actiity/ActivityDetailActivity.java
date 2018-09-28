package com.ovov.lfzj.neighbour.actiity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;
import com.ovov.lfzj.base.banner.BannerAdapter;
import com.ovov.lfzj.base.banner.BannerLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 活动详情
 */
public class ActivityDetailActivity extends BaseActivity {

    /*@BindView(R.id.iv_right)
    ImageView mIvRight;*/
    @BindView(R.id.banner)
    BannerLayout mBanner;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_enroll_num)
    TextView mTvEnrollNum;
    /*@BindView(R.id.tv_put_name)
    TextView mTvPutName;
    @BindView(R.id.tv_address_name)
    TextView mTvAddressName;*/
   /* @BindView(R.id.tv_time)
    TextView mTvTime;*/
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpage)
    ViewPager mViewpage;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ActivityDetailActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_header;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_activity_detail);
        initTab();
    }
    private void initTab() {
        List<String> titles = new ArrayList<>();
        titles.add("活动评论");
        titles.add("活动详情");
        for (int i = 0; i < titles.size(); i++) {
            mTablayout.addTab(mTablayout.newTab().setText(titles.get(i)));
        }
        mTablayout.setTabMode(TabLayout.GRAVITY_CENTER);   //  标题居中
        List<Fragment> list = new ArrayList<>();
        list.add(new ActivityCommentFragment());
        list.add(new ActivityDescFragment());
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(this.getSupportFragmentManager(), list, titles);   //绑定fragment
        mViewpage.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewpage);  //标题与页面同步
        BannerAdapter<Integer> mBannerAdapter = new BannerAdapter<Integer>() {
            @Override
            protected void bind(ViewHolder holder, Integer data) {
                Picasso.with(ActivityDetailActivity.this).load(data).into(holder.mImageView);
            }
        };
        List<Integer> mBannerData = new ArrayList<>();
        mBannerData.add(R.mipmap.guide_view_01);
        mBannerAdapter.reset(mBannerData);
        mBanner.setAdapter(mBannerAdapter);
        //设置可以推动
       AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        mCollapsingToolbar.setLayoutParams(layoutParams);//这句必须加上
    }
   /* @OnClick({R.id.iv_back, R.id.iv_right, R.id.re_pl, R.id.re_zan, R.id.re_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_right:
                break;
            case R.id.re_pl:
                break;
            case R.id.re_zan:
                break;
            case R.id.re_share:
                break;
        }
    }*/
}

package com.ovov.lfzj.guide;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;


import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.SharedPreferenceUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nate Robinson
 * @Time 2017/12/24
 */

public class GuideActivity extends BaseActivity {

    private ViewPager root_vip;
    public static final String IS_NEED_SHOW_GUIDE = "is_need_show_guide";
    private GuideFragmentsAdapter mGuideFragmentsAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private GestureDetector gestureDetector; // 用户滑动
    private int flaggingWidth;// 滑动关闭引导页所需滚动的长度
    private int currentItem = 0; // 当前图片的位置

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void init() {

        gestureDetector = new GestureDetector(new GuideViewTouch());
        flaggingWidth = UIUtils.dip2px(20);

        root_vip = (ViewPager) findViewById(R.id.root_vip);

        mFragments.add(GuideFragment.newInstance(0));
        mFragments.add(GuideFragment.newInstance(1));
        mFragments.add(GuideFragment.newInstance(2));

        mGuideFragmentsAdapter = new GuideFragmentsAdapter(getSupportFragmentManager(), mFragments);
        root_vip.setAdapter(mGuideFragmentsAdapter);
        root_vip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 当滑动到最后一页时，继续滑动将进入到联赛选择列表页
     */
    private class GuideViewTouch extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            if (currentItem == 2) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
                        - e2.getY()) && (e1.getX() - e2.getX() <= (-flaggingWidth)
                        || e1.getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        SharedPreferenceUtil.setBoolean(getApplicationContext(), GuideActivity.IS_NEED_SHOW_GUIDE, false);
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                }
            }
            return false;
        }
    }

}

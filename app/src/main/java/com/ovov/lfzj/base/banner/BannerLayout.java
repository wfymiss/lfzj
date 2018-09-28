package com.ovov.lfzj.base.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


import com.ovov.lfzj.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class BannerLayout extends RelativeLayout {
    @BindView(R.id.pager_banner)
    ViewPager pagerBanner;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    private static final long duration = 3000;
    private Timer mCyclingTimer;
    private CyclingHandler mCyclingHandler;
    private TimerTask mCycleTask;
    private long mResumecycleTime;
    private List list;

    public BannerLayout(Context context) {
        super(context);
        init(context);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.widge_banner_layout, this, true);
        ButterKnife.bind(this);

        mCyclingHandler = new CyclingHandler(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //在视图上显示出来

        //计时器
        mCyclingTimer = new Timer();

        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mCyclingHandler.sendEmptyMessage(0);
            }
        };

        //任务、延时事件、循环时间
        mCyclingTimer.schedule(mCycleTask, duration, duration);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //取消开启的计时任务
        mCyclingTimer.cancel();
        mCycleTask.cancel();
        mCyclingTimer = null;
        mCycleTask = null;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        mResumecycleTime = System.currentTimeMillis() + duration;
        return super.dispatchTouchEvent(ev);
    }


    //切换到下页的方法

    public void moveToNextPosition() {

        //看看有没有设置适配器

        if (pagerBanner.getAdapter() == null) {

            throw new IllegalStateException("you need set a banner adapter");
        }
        //看看适配器里边是不是有数据
        int count = pagerBanner.getAdapter().getCount();
        if (count == 0) return;
        //看是不是展示的最后一条

        if (pagerBanner.getCurrentItem() == count - 1) {
            //切换到0不设置平滑滚动
            pagerBanner.setCurrentItem(0, false);
        } else {

            pagerBanner.setCurrentItem(pagerBanner.getCurrentItem() + 1, true);
        }

    }
    //设置适配器的方法

    // 设置适配器的方法
    public void setAdapter(BannerAdapter adapter){
        pagerBanner.setAdapter(adapter);
        indicator.setViewPager(pagerBanner);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    //为了防止内部类持有外部类的引用而造成内存泄漏，所以静态内部类加若引用的方式

    private static class CyclingHandler extends Handler {

        private WeakReference<BannerLayout> mBannerRefrence;

        public CyclingHandler(BannerLayout banner) {

            mBannerRefrence = new WeakReference<BannerLayout>(banner);

        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //收到消息处理：轮播图切换到下一页

            if (mBannerRefrence == null) return;
            BannerLayout bannerLayout = mBannerRefrence.get();
            if (bannerLayout == null) return;

            //触摸之后不过三秒不去轮播
            if (System.currentTimeMillis() < bannerLayout.mResumecycleTime) {

                return;
            }

            //切换到下一页
            bannerLayout.moveToNextPosition();
        }
    }

}

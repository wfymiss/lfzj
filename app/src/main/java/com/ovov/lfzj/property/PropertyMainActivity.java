package com.ovov.lfzj.property;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseMainActivity;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.event.Recievertype;
import com.ovov.lfzj.event.RevieverEvent;
import com.ovov.lfzj.home.repair.WorkerOrderDetailActivity;
import com.ovov.lfzj.home.ui.NewsDetailActivity;
import com.ovov.lfzj.opendoor.OpendoorActivity;
import com.ovov.lfzj.property.home.HomeFragment;
import com.ovov.lfzj.property.user.PropertyUserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;

import static com.ovov.lfzj.MainActivity.BASE_FILE;

public class PropertyMainActivity extends BaseMainActivity {

    public static void toActivity(Context context){
        Intent intent = new Intent(context,PropertyMainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);
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


    //收到推送以后点击事件的处理
    @Subscribe
    public void onEventMainThread(RevieverEvent event) {
        if (event.getType().equals(Recievertype.OWNERDISS)) {
            WorkerOrderDetailActivity.toActivity(mActivity, Integer.parseInt(event.getId()));
        }else if (event.getType().equals(Recievertype.sellerorderlist)) {
            WorkerOrderDetailActivity.toActivity(mActivity, Integer.parseInt(event.getId()));
        }

    }
    @Override
    public boolean menuClicked(int index) {
        return false;
    }

    @OnClick(R.id.iv_open)
    public void onViewClicked() {
        OpendoorActivity.toActivity(mActivity);



    }
}

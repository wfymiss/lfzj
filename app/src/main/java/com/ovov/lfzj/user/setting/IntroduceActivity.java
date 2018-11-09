package com.ovov.lfzj.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.SharedPreferenceUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.guide.GuideActivity;
import com.ovov.lfzj.guide.GuideFragment;
import com.ovov.lfzj.guide.GuideFragmentsAdapter;
import com.ovov.lfzj.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class IntroduceActivity extends BaseActivity {


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, IntroduceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_introduce;
    }

    @Override
    public void init() {
    }
    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

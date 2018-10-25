package com.ovov.lfzj.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.utils.SharedPreferenceUtil;
import com.ovov.lfzj.guide.GuideActivity;
import com.ovov.lfzj.property.PropertyMainActivity;

import static com.ovov.lfzj.CatelApplication.OWNER_LOGIN;
import static com.ovov.lfzj.CatelApplication.PROPERTY_LOGIN;


public class SplashActivity extends BaseActivity {
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isNeedShowGuide = SharedPreferenceUtil.getBoolean(getApplicationContext(), GuideActivity.IS_NEED_SHOW_GUIDE, true);
            if (isNeedShowGuide) {
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                finish();
            } else {
                if (LoginUserBean.getInstance().isLogin()) {
                    if (LoginUserBean.getInstance().getLoginType() != null && LoginUserBean.getInstance().getLoginType().equals(OWNER_LOGIN))
                        MainActivity.toActivity(mActivity);
                    else if (LoginUserBean.getInstance().getLoginType() != null &&LoginUserBean.getInstance().getLoginType().equals(PROPERTY_LOGIN))
                        PropertyMainActivity.toActivity(mActivity);
                    else {
                        LoginUserBean.getInstance().reset();
                        LoginUserBean.getInstance().save();
                        LoginActivity.toActivity(mActivity);
                    }
                    finish();
                } else {
                    LoginActivity.toActivity(mActivity);
                    finish();
                }
            }

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}

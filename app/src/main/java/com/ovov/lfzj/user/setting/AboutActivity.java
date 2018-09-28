package com.ovov.lfzj.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void init() {

        setTitleText("关于我们");
        String version= Tools.getVersionName(mActivity);
        mTvVersion.setText("当前版本（"+version+"）");
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


}

package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentitySelectActivity extends BaseActivity {

    @BindView(R.id.iv_owner)
    ImageView mIvOwner;
    @BindView(R.id.iv_seller)
    ImageView mIvSeller;
    @BindView(R.id.iv_property)
    ImageView mIvProperty;
    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;
    private int OWNER = 1;
    private int PROPETY = 2;
    private int SELLER = 3;
    private int type = 1;
    private boolean isConfirm;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, IdentitySelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_identity_select;
    }

    @Override
    public void init() {
        mIvOwner.setSelected(true);
        mIvProperty.setSelected(false);
        mIvSeller.setSelected(false);
        type = OWNER;
        mIvConfirm.setSelected(true);
        isConfirm = true;
    }

    @OnClick({R.id.iv_owner, R.id.iv_seller, R.id.iv_property, R.id.tv_next, R.id.tv_service_title, R.id.tv_skip, R.id.iv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_owner:
                mIvOwner.setSelected(true);
                mIvProperty.setSelected(false);
                mIvSeller.setSelected(false);
                type = OWNER;
                break;
            case R.id.iv_seller:
                mIvOwner.setSelected(false);
                mIvProperty.setSelected(false);
                mIvSeller.setSelected(true);
                type = SELLER;
                break;
            case R.id.iv_property:
                mIvOwner.setSelected(false);
                mIvProperty.setSelected(true);
                mIvSeller.setSelected(false);
                type = PROPETY;
                break;
            case R.id.tv_next:
                if (isConfirm) {
                    if (type == OWNER) {
                        if (!LoginUserBean.getInstance().isIs_auth())
                            IdentityConfirmActivity.toActivity(mActivity);
                        else
                            MainActivity.toActivity(mActivity);
                        finish();
                    } else
                        IdentityErrorActivity.toActivity(mActivity);
                } else {
                    showToast("请同意用户协议");
                }


                break;
            case R.id.tv_service_title:
                ServiceActivity.toActivity(mActivity);
                break;
            case R.id.tv_skip:
                if (isConfirm) {
                    MainActivity.toActivity(mActivity);
                    finish();
                } else {
                    showToast("请同意用户协议");
                }

                break;
            case R.id.iv_confirm:
                if (isConfirm) {
                    isConfirm = false;
                    mIvConfirm.setSelected(false);
                } else {
                    isConfirm = true;
                    mIvConfirm.setSelected(true);
                }
                break;
        }
    }
}

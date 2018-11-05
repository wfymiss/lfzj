package com.ovov.lfzj.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ErrorInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.FindPasswordActivity;
import com.ovov.lfzj.login.LoginActivity;
import com.youzan.androidsdk.YouzanSDK;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.Subscription;

public class SettingActivity extends BaseActivity {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_set);
    }

    @OnClick({R.id.iv_back, R.id.tv_account_safe, R.id.tv_about, R.id.btn_loginout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_account_safe:
                FindPasswordActivity.toActivity(mActivity);
                break;
            case R.id.tv_about:
                AboutActivity.toActivity(mActivity);
                break;
            case R.id.btn_loginout:
                frameRemind();
                break;
        }
    }

    private void loginout() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().loginout()
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else{

                            doFailed();
                            showError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(DataInfo errorInfoDataInfo) {
                        dismiss();
                        if (errorInfoDataInfo.success()){
                            LoginUserBean.getInstance().reset();
                            LoginUserBean.getInstance().save();
                            JPushInterface.deleteAlias(mActivity,1);
                            LoginActivity.toActivity(mActivity);
                            YouzanSDK.userLogout(mActivity);
                            for (int i = 0; i < mActivities.size(); i++) {
                                if (mActivities.get(i) != null && !mActivities.get(i).isFinishing()) {
                                    mActivities.get(i).finish();
                                }
                            }
                        }
                    }
                });
        addSubscrebe(subscription);
    }
    // 提醒
    private void frameRemind() {
        final RemindDialogUtil.Builder dialogBuilder = new RemindDialogUtil.Builder(this);
        dialogBuilder.setContent("确认退出登录吗？");
        final RemindDialogUtil dialog=dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                loginout();
            }
        });
        dialog.show();

        WindowManager windowManager=this.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        lp.width= (int) (display.getWidth()*0.68);
        lp.alpha=0.96f;
        dialog.getWindow().setAttributes(lp);
    }

}

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
import com.ovov.lfzj.base.bean.UpdateBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.Tools;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.base.widget.UpdateDialog;
import com.ovov.lfzj.guide.GuideActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.FindPasswordActivity;
import com.ovov.lfzj.login.LoginActivity;
import com.ovov.lfzj.login.ServiceActivity;
import com.youzan.androidsdk.YouzanSDK;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Subscription;

public class SettingActivity extends BaseActivity {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
    TagAliasCallback tagAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_set);
    }

    @OnClick({R.id.iv_back, R.id.tv_account_safe, R.id.tv_about, R.id.btn_loginout, R.id.tv_intro, R.id.tv_service, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_account_safe:
                PasswordReviseActivity.toActivity(mActivity);
                break;
            case R.id.tv_about:
                AboutActivity.toActivity(mActivity);
                break;
            case R.id.btn_loginout:
                frameRemind();
                break;
            case R.id.tv_service:
                ServiceActivity.toActivity(mActivity);
                break;
            case R.id.tv_intro:
                IntroduceActivity.toActivity(mActivity);
                break;
            case R.id.tv_check:
                checkVersion();
                break;
        }
    }

    private void checkVersion() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().checkVersion(Tools.getVersion(mActivity))
                .compose(RxUtil.<DataInfo<UpdateBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<UpdateBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<UpdateBean> updateBeanDataInfo) {
                        dismiss();
                        /*if (updateBeanDataInfo.datas().needUpdate()) {
                            UpdateDialog updateDialog = new UpdateDialog(mActivity, updateBeanDataInfo.datas().apk_url, updateBeanDataInfo.datas().upgrade_point);
                            updateDialog.show();
                        }*/
                    }
                });
        addSubscrebe(subscription);
    }

    private void loginout() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().loginout()
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            showError(e.getMessage());
                        }

                    }


                    @Override
                    public void onNext(DataInfo errorInfoDataInfo) {
                        dismiss();
                        if (errorInfoDataInfo.success()) {
                            LoginUserBean.getInstance().reset();
                            LoginUserBean.getInstance().save();
                            JPushInterface.setAlias(mActivity, "", tagAliasCallback);                                //  极光
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
        final RemindDialogUtil dialog = dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                loginout();
            }
        });
        dialog.show();

        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.68);
        lp.alpha = 0.96f;
        dialog.getWindow().setAttributes(lp);
    }

}

package com.ovov.lfzj.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.ErrorIdentityDialog;
import com.ovov.lfzj.event.ErrorDialogCancelEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.login.RegisterActivity.SMS_CODE_VALID_SECOND;

public class IdentityConfirmActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    TextView mEtPhone;
    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    public static final int SMS_CODE_VALID_SECOND = 60;
    private ValueAnimator valueAnimator;
    private static int whiteColor;
    private static int grayColor;
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, IdentityConfirmActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_identity_confirm;
    }

    @Override
    public void init() {
        /*ErrorIdentityDialog errorIdentityDialog = new ErrorIdentityDialog(mActivity);
        errorIdentityDialog.show();*/
        whiteColor = getResources().getColor(R.color.color_ffffff);
        grayColor = getResources().getColor(R.color.color_999999);
        addRxBusSubscribe(ErrorDialogCancelEvent.class, new Action1<ErrorDialogCancelEvent>() {
            @Override
            public void call(ErrorDialogCancelEvent errorDialogCancelEvent) {
                MainActivity.toActivity(mActivity);
            }
        });
        mEtPhone.setText(LoginUserBean.getInstance().getPhone());
        mEtVerify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6){
                    authStep1();
                }
            }
        });
    }

    @OnClick({R.id.tv_get_code, R.id.tv_no_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                sendSms();
                break;
            case R.id.tv_no_confirm:
                MainActivity.toActivity(mActivity);
                finish();
                break;
        }
    }

    private void sendSms() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().sendSms(mEtPhone.getText().toString(), "2")
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
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        valueAnimator = countTimer(mTvGetCode);
                    }
                });
        addSubscrebe(subscription);
    }
    private void authStep1(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().authStep1(mEtPhone.getText().toString(),mEtVerify.getText().toString())
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            ErrorIdentityDialog errorIdentityDialog = new ErrorIdentityDialog(mActivity);
                            errorIdentityDialog.show();
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        SelectSubActivity.toActivity(mActivity);
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }
    /**
     * 验证码获取成功后 开启倒计时
     *
     * @param sendCodeBtn
     * @return
     */
    public static ValueAnimator countTimer(final TextView sendCodeBtn) {
        sendCodeBtn.setEnabled(false);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(SMS_CODE_VALID_SECOND, 0).setDuration(SMS_CODE_VALID_SECOND * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sendCodeBtn.setText(animation.getAnimatedValue() + "s");
                sendCodeBtn.setTextColor(grayColor);
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                sendCodeBtn.setText("重新获取");
                sendCodeBtn.setEnabled(true);
                sendCodeBtn.setTextColor(whiteColor);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueAnimator != null)
            valueAnimator.cancel();
    }
}

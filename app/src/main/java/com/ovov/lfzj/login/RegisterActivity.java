package com.ovov.lfzj.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.RegisterBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_confirm)
    EditText mEtConfirm;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    public static final int SMS_CODE_VALID_SECOND = 60;
    @BindView(R.id.et_recommend)
    EditText mEtRecommend;
    private ValueAnimator valueAnimator;
    private static int whiteColor;
    private static int grayColor;
    private String password;
    private String verify;
    private String confirm;
    private String phone;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void init() {

        whiteColor = getResources().getColor(R.color.color_ffffff);
        grayColor = getResources().getColor(R.color.color_999999);
    }

    @OnClick({R.id.tv_get_code, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                phone = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast(R.string.text_phone_empty);
                    return;
                }
                if (RegexUtils.isMobile(phone) != RegexUtils.VERIFY_SUCCESS) {
                    showToast(R.string.text_phone_error);
                    return;
                }
                sendSms();
                break;
            case R.id.tv_register:
                phone = mEtPhone.getText().toString();
                password = mEtPwd.getText().toString();
                verify = mEtVerify.getText().toString();
                confirm = mEtPwd.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast(R.string.text_phone_empty);
                    return;
                }
                if (RegexUtils.isMobile(phone) != RegexUtils.VERIFY_SUCCESS) {
                    showToast(R.string.text_phone_error);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast(R.string.text_pwd_empty);
                    return;
                }
                if (TextUtils.isEmpty(confirm)) {
                    showToast(R.string.text_confirm_empty);
                    return;
                }
                if (TextUtils.isEmpty(verify)) {
                    showToast(R.string.text_verify_empty);
                    return;
                }
                if (!password.equals(confirm)) {
                    showToast(R.string.text_no_equals);
                    return;
                }
                if (!TextUtils.isEmpty(mEtRecommend.getText().toString()) && RegexUtils.isMobile(mEtRecommend.getText().toString()) != RegexUtils.VERIFY_SUCCESS) {
                    showToast("请输入正确的推荐人手机号");
                    return;
                }
                toRegister();
                break;
        }
    }

    private void sendSms() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().sendSms(mEtPhone.getText().toString(), "1")
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
                        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
                        mEtVerify.setFocusable(true);
                        mEtVerify.setFocusableInTouchMode(true);
                        mEtVerify.requestFocus();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                    }
                });
        addSubscrebe(subscription);
    }

    private void toRegister() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().register(phone, verify, password, mEtRecommend.getText().toString())
                .compose(RxUtil.<DataInfo<RegisterBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<RegisterBean>>() {
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
                    public void onNext(DataInfo<RegisterBean> registerBeanDataInfo) {
                        dismiss();
                        if (registerBeanDataInfo.success()) {
                            showToast(registerBeanDataInfo.datas().message);
                            finish();
                        }
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

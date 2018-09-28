package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginBean;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {

    }

    @OnClick({R.id.tv_login, R.id.tv_to_register, R.id.tv_find_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                String phone = mEtPhone.getText().toString();
                String password = mEtPwd.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast(R.string.text_phone_empty);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast(R.string.text_pwd_empty);
                    return;
                }
                if (RegexUtils.isMobile(phone) != RegexUtils.VERIFY_SUCCESS) {
                    showToast(R.string.text_phone_error);
                    return;
                }
                login(phone, password);
                break;
            case R.id.tv_to_register:
                RegisterActivity.toActivity(mActivity);
                break;
            case R.id.tv_find_pwd:
                FindPasswordActivity.toActivity(mActivity);
                break;
        }
    }

    private void login(String phone, String password) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().login(phone, password)
                .compose(RxUtil.<DataInfo<LoginBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<LoginBean>>() {
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
                    public void onNext(DataInfo<LoginBean> loginBeanDataInfo) {
                        dismiss();
                        if (loginBeanDataInfo.success()) {
                            LoginUserBean.getInstance().setAccess_token(loginBeanDataInfo.datas().token);
                            LoginUserBean.getInstance().setLogin(true);
                            LoginUserBean.getInstance().setUserId(loginBeanDataInfo.datas().user_id);
                            LoginUserBean.getInstance().setPhone(mEtPhone.getText().toString());
                            if (loginBeanDataInfo.datas().is_user_auth == 0) {
                                LoginUserBean.getInstance().setIs_auth(false);
                            } else {
                                LoginUserBean.getInstance().setIs_auth(true);
                            }
                            LoginUserBean.getInstance().save();

                            IdentitySelectActivity.toActivity(mActivity);

                            finish();

                        }
                    }
                });
        addSubscrebe(subscription);
    }
}

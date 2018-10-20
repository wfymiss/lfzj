package com.ovov.lfzj.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.MobileInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.StatusBarUtils;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.BuildingListDialog;
import com.ovov.lfzj.base.widget.CodeEditView;
import com.ovov.lfzj.base.widget.IdentitySuccessDialog;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.event.SubselectEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class IdentityConfirmActivity extends BaseActivity {


    public static final int SMS_CODE_VALID_SECOND = 60;
    @BindView(R.id.sitview)
    View sitview;
    @BindView(R.id.tv_select_sub)
    TextView mTvSelectSub;
    @BindView(R.id.et_building_number)
    EditText mEtBuildingNumber;
    @BindView(R.id.et_unit_number)
    EditText mEtUnitNumber;
    @BindView(R.id.et_room_number)
    EditText mEtRoomNumber;
    @BindView(R.id.tv_start_phone)
    TextView mTvStartPhone;
    @BindView(R.id.codeEditView)
    CodeEditView mCodeEditView;
    @BindView(R.id.tv_end_phone)
    TextView mTvEndPhone;
    @BindView(R.id.et_verfy)
    EditText mEtVerfy;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    private ValueAnimator valueAnimator;
    private String sub_id;
    private String midMobile;
    private String mobile;
    private String house_path;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*ErrorIdentityDialog errorIdentityDialog = new ErrorIdentityDialog(mActivity);
        errorIdentityDialog.show();*/
        IdentitySuccessDialog identitySuccessDialog = new IdentitySuccessDialog(mActivity);
        identitySuccessDialog.setWidth((int) (UIUtils.getScreenWidth()*0.8));
        identitySuccessDialog.show();
        StatusBarUtils.setStatusBar(this, false, false);
        initSitView();
        setTitleText(R.string.text_identity_owner);
        setRightText(R.string.text_abandon);
        addRxBusSubscribe(SubselectEvent.class, new Action1<SubselectEvent>() {
            @Override
            public void call(SubselectEvent subselectEvent) {
                sub_id = subselectEvent.getSub_id();
                mTvSelectSub.setText(subselectEvent.getSub_name());
                mEtBuildingNumber.removeTextChangedListener(mTextWatcherbuilding);
                mEtUnitNumber.removeTextChangedListener(mTextWatcherUnit);
                mEtRoomNumber.removeTextChangedListener(mTextWatcherRoom);
                mEtBuildingNumber.setText("");
                mEtUnitNumber.setText("");
                mEtRoomNumber.setText("");
                mEtBuildingNumber.addTextChangedListener(mTextWatcherbuilding);
                mEtUnitNumber.addTextChangedListener(mTextWatcherUnit);
                mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);

            }
        });

        mEtBuildingNumber.addTextChangedListener(mTextWatcherbuilding);
        mEtUnitNumber.addTextChangedListener(mTextWatcherUnit);
        mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);


    }
    TextWatcher mTextWatcherbuilding = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEtUnitNumber.removeTextChangedListener(mTextWatcherUnit);
            mEtRoomNumber.removeTextChangedListener(mTextWatcherRoom);
            mEtUnitNumber.setText("");
            mEtRoomNumber.setText("");
            mEtUnitNumber.addTextChangedListener(mTextWatcherUnit);
            mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher mTextWatcherUnit = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEtRoomNumber.removeTextChangedListener(mTextWatcherRoom);
            mEtRoomNumber.setText("");
            mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher mTextWatcherRoom = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMobile();
                }
            },1000);
        }
    };

    private void getMobile() {
        house_path = sub_id+"-"+mEtBuildingNumber.getText().toString().trim()+"-"+mEtUnitNumber.getText().toString().trim()+"-"+mEtRoomNumber.getText().toString().trim();
        Subscription subscription = RetrofitHelper.getInstance().getMobile(house_path)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<MobileInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                            mTvEndPhone.setVisibility(View.GONE);
                            mCodeEditView.setVisibility(View.GONE);
                            mTvStartPhone.setVisibility(View.GONE);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                            mTvEndPhone.setVisibility(View.GONE);
                            mCodeEditView.setVisibility(View.GONE);
                            mTvStartPhone.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(DataInfo<MobileInfo> mobileInfoDataInfo) {
                        mobile = mobileInfoDataInfo.datas().mobile;
                        if (RegexUtils.isMobile(mobile) != RegexUtils.VERIFY_SUCCESS){
                            showToast("手机号码有误，请去物业修正");
                            mTvEndPhone.setVisibility(View.GONE);
                            mCodeEditView.setVisibility(View.GONE);
                            mTvStartPhone.setVisibility(View.GONE);
                        }else {
                            String startMobile = mobile.substring(0,3);
                            String endMobile = mobile.substring(mobile.length()-4);
                            midMobile = mobile.substring(3,7);
                            mTvStartPhone.setText(startMobile);
                            mTvEndPhone.setText(endMobile);
                            mTvEndPhone.setVisibility(View.VISIBLE);
                            mCodeEditView.setVisibility(View.VISIBLE);
                            mTvStartPhone.setVisibility(View.VISIBLE);
                        }

                    }
                });
        addSubscrebe(subscription);
    }


    private void sendSms() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().sendSms(mobile, "2")
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
    private void authStep2(String house_path){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().authStep2(house_path,mobile,mEtVerfy.getText().toString().trim())
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("认证成功");
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
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                sendCodeBtn.setText("重新获取");
                sendCodeBtn.setEnabled(true);
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

    // 获取占位视图高度
    private void initSitView() {
        // 获取占位视图高度
        ViewGroup.LayoutParams sitParams = sitview.getLayoutParams();
        sitParams.height = StatusBarUtils.getStatusBarHeight(mActivity);
    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_get_code, R.id.tv_commit,R.id.tv_select_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                finish();
                break;
            case R.id.tv_get_code:
                Log.e("midmobile",midMobile);
                if (TextUtils.isEmpty(mCodeEditView.getText().toString().trim())){
                    showToast("请补全手机号");
                    return;
                }
                if (RegexUtils.isMobile(mobile) != RegexUtils.VERIFY_SUCCESS){
                    showToast("请获取正确的手机号");
                    return;
                }
                if (!midMobile.equals(mCodeEditView.getText().toString().trim())){
                    showToast("手机号码填写错误,请重新输入");
                    return;
                }
                sendSms();
                break;
            case R.id.tv_commit:
                if (TextUtils.isEmpty(mEtVerfy.getText().toString().trim())){
                    showToast("请输入验证码");
                    return;
                }
                authStep2(house_path);
                break;
            case R.id.tv_select_sub:
                getBuildinglist();
                break;
        }
    }

    private void getBuildinglist() {
        Subscription subscription = RetrofitHelper.getInstance().getSubList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SublistInfo>>() {
                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ListInfo<SublistInfo> buildingListResult) {
                        BuildingListDialog buildingListDialog = new BuildingListDialog(mActivity,buildingListResult.datas());
                        buildingListDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.5));
                        buildingListDialog.show();
                        buildingListDialog.setData(buildingListResult.datas());
                    }
                });
        addSubscrebe(subscription);
    }
}

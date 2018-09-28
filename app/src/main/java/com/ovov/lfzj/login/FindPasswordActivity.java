package com.ovov.lfzj.login;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;


public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.edit_find_user)               // 账号
            EditText editFindUser;
    @BindView(R.id.edit_find_check_number)     // 验证码
            EditText editFindCheckNumber;
    @BindView(R.id.btn_send_check)
    Button btnSendCheck;
    @BindView(R.id.edit_find_password)         // 新密码
            EditText editFindPassword;
    @BindView(R.id.btn_confirm)                 // 提交
            Button btnConfirm;

    private String phone;
    private String password;
    private MyCountDownTimer mtCountDown;     // 定义计时器

    public static void toActivity(Context context){
        Intent intent = new Intent(context,FindPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_password;
    }

    @Override
    public void init() {
        // presenter = new FindPasswordPresenter(this);
        mtCountDown = new MyCountDownTimer(60000, 1000);
        setTitleText("找回密码");

    }

    @OnClick({R.id.btn_send_check, R.id.btn_confirm,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_check:
                if (TextUtils.isEmpty(editFindUser.getText().toString())){
                    showToast("请输入手机号码");
                    return;
                }
                sendSms();
                break;
            case R.id.btn_confirm:      // 提交修改的密码
                if (TextUtils.isEmpty(editFindUser.getText().toString())){
                    showToast("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(editFindPassword.getText().toString())){
                    showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(editFindCheckNumber.getText().toString())){
                    showToast("请输入验证码");
                    return;
                }
                findPwd();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void findPwd(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().findPwd(editFindUser.getText().toString(),editFindPassword.getText().toString(),editFindCheckNumber.getText().toString())
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
                        showToast("修改成功");
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }
    private void sendSms() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().sendSms(editFindUser.getText().toString(), "3")
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
                        mtCountDown.start();
                    }
                });
        addSubscrebe(subscription);
    }


    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btnSendCheck.setClickable(false);
            btnSendCheck.setText(l / 1000 + "s");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnSendCheck.setText("重新获取");
            //设置可点击
            btnSendCheck.setClickable(true);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mtCountDown != null)
            mtCountDown.cancel();
    }

    /*// ————————————————————————————服务端返回————————————————————————————
    // 密码重置成功
    @Override
    public void sucess() {
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    // 密码重置 失败
    @Override
    public void failExecution(String msg) {
        if (msg!=null){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FindPasswordActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}

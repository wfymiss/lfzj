package com.ovov.lfzj.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.TagAliasCallback;
import rx.Subscription;

/**
 * 密码修改页面
 * 刘永毅  2017/08/04
 */
public class PasswordReviseActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.old_login_password)     //原密码
    EditText old_password;
    @BindView(R.id.new_login_password)    //新密码
    EditText new_password;
    @BindView(R.id.next_login_password)   //确认新密码
    EditText next_password;
    @BindView(R.id.password_login_phone)  //验证手机号
    TextView phone;
    @BindView(R.id.achieve_code)     // 点击获取验证码
    TextView achieve_code;
    @BindView(R.id.input_verify_code)    //输入验证码
    EditText code;
    @BindView(R.id.confirm_password)     //确认修改按钮
    TextView confirm_password;

    private String token=null;
    private String oldword=null;       // 旧密码
    private String newword=null;       //新密码
    private String nextword=null;     //确认密码
    private String telephone=null;     //手机号
    private String verifycode;  //验证码

    private CodeDownTimer codeDownTimer;             //  二维码计时器
    private String path = Environment.getExternalStorageDirectory().toString() + "/lefulyy/icon_bitmap/" + "touxiang.jpg";  //头像的本地保存路径

    TagAliasCallback tagAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
        }
    };

    // 验证码计时器
    private class CodeDownTimer extends CountDownTimer {
        public CodeDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            //防止计时过程中重复点击
            achieve_code.setClickable(false);
            achieve_code.setText(millisUntilFinished/ 1000 + "s");
        }
        @Override
        public void onFinish() {
            //重新给Button设置文字
            achieve_code.setText("发送验证码");
            //设置可点击
            achieve_code.setClickable(true);
        }
    }

    @Override
    public void init() {
        setTitleText("修改密码");
        phone.setText(LoginUserBean.getInstance().getPhone());    // 显示手机号
        codeDownTimer = new CodeDownTimer(60000, 1000);
    }




    @OnClick({R.id.achieve_code,R.id.confirm_password,R.id.iv_back})
    public void onClickListener(View view){
        switch (view.getId()){
            case R.id.achieve_code:          //点击获取验证码
                telephone = phone.getText().toString();
                if (!telephone.trim().equals("")){
                    sendSms();          // 请求获取验证码
                }
                break;
            case R.id.confirm_password:     //确认修改密码
                boolean judge = decideFormate();
                if (judge){
                    // 验证 验证码是否有效
                    findPwd();
                }
                break;
            case R.id.iv_back:

                finish();
                break;
            default:
        }
    }

    private void findPwd(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().findPwd(phone.getText().toString(),new_password.getText().toString(),verifycode)
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

    private boolean decideFormate() {
        oldword=old_password.getText().toString();
        newword=new_password.getText().toString();       // 新密码
        nextword=next_password.getText().toString();    // 重新输入信息密码
        telephone=phone.getText().toString();           // 验证手机号
        verifycode=code.getText().toString();          // 获取收到的验证码
        if (TextUtils.isEmpty(oldword)||TextUtils.isEmpty(newword)||TextUtils.isEmpty(nextword)){
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if (!nextword.equals(newword)){
                Toast.makeText(this, "输入新密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(verifycode)){
                Toast.makeText(this,"请获取验证码",Toast.LENGTH_SHORT).show();
                return false;
            }
//            else {
//                if (RegexUtils.isMobile(telephone) != RegexUtils.VERIFY_SUCCESS){
//                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return  R.layout.activity_password_revise;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        codeDownTimer.cancel();     // 停止计时器
    }
    private void sendSms() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().sendSms(phone.getText().toString(), "3")
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
                        codeDownTimer.start();
                    }
                });
        addSubscrebe(subscription);
    }
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, PasswordReviseActivity.class);
        context.startActivity(intent);
    }

}

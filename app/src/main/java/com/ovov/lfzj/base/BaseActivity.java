package com.ovov.lfzj.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.ovov.lfzj.R;
import com.ovov.lfzj.base.utils.AppManager;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.UIUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jzxiang on 3/11/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static String TAG = "BaseActivity";
    /**
     * 存储全局的activity
     */
    protected static List<Activity> mActivities = new LinkedList<Activity>();
    /**
     * 前台的activity
     */
    private static Activity mForegroundActivity;
    public Activity mActivity;
    protected CompositeSubscription mCompositeSubscription;
    private Unbinder mUnbinder;

    private ProgressDialog mProgressDialog;

    private TextView mTvTitle;
    private TextView mTvRight;

    /**
     * 获取前台activity
     *
     * @return
     */
    public static Activity getForegroundActivity() {
        return mForegroundActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        UIUtils.myStatusBar(this);
        super.onCreate(savedInstanceState);
        mActivity = this;
        AppManager.getInstance().addActivity(this);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        synchronized (mActivities) {
            mActivities.add(this);
        }

        setContentView(getLayoutId());

        if (isHideNavigation()){
            hideNavigation();
        }
        TAG = getClass().getSimpleName();

        mUnbinder = ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
    }

    public boolean isHideNavigation(){
        return false;
    }

    private void hideNavigation(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setRightText(String text){
        if (mTvRight == null){
            mTvRight = (TextView) findViewById(R.id.tv_right);
        }

        mTvRight.setText(text);
    }

    public void setRightText(int textId){
        if (mTvRight == null){
            mTvRight = (TextView) findViewById(R.id.tv_right);
        }

        mTvRight.setText(textId);
    }

    public void setTitleText(String text){
        if (mTvTitle == null){
            mTvTitle = (TextView) findViewById(R.id.tv_title);
        }

        mTvTitle.setText(text);
    }

    public void setTitleText(int textId){
        if (mTvTitle == null){
            mTvTitle = (TextView) findViewById(R.id.tv_title);
        }

        mTvTitle.setText(textId);
    }

    public abstract int getLayoutId();

    public abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
        synchronized (mActivities) {
            AppManager.getInstance().killActivity(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mForegroundActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mForegroundActivity = null;
    }

    /**
     * 取消订阅
     */
    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }

    /**
     * 添加订阅
     *
     * @param subscription
     */
    public void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


    /**
     * 管理各组件之间的通信
     *
     * @param eventType
     * @param act
     * @param <U>
     */
    public  <U> void addRxBusSubscribe(Class<U> eventType, Action1<U> act) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(RxBus.getDefault().toDefaultObservable(eventType, act));
    }

    /**
     * 网络请求错误提示
     *
     * @param error
     */
    public void showError(String error) {
        Log.e(TAG, "showError: error = " + error);
    }

    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
        }
        mProgressDialog.show();
    }

    public void dismiss() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    public void showToast(String string){
        UIUtils.displayToast(string);
    }

    public void showToast(int id){
        UIUtils.displayToast(getString(id));
    }

    public void doFailed(){
        UIUtils.showFailed();
    }

    public void doSuccess(){
        UIUtils.showSuccess();
    }


    public static void setIntentData(Intent intent, Parcelable parcelable){
        intent.putExtra(parcelable.getClass().getSimpleName(),parcelable);
    }

    public  <T extends Parcelable> T getIntentData(Class clazz){
        return getIntent().getParcelableExtra(clazz.getSimpleName());
    }
}

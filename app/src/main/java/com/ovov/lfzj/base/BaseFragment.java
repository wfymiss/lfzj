package com.ovov.lfzj.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;


import com.ovov.lfzj.R;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.UIUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.zyao89.view.zloading.Z_TYPE.LEAF_ROTATE;

/**
 * Created by jzxiang on 4/3/17.
 */

public class BaseFragment extends Fragment {

    public static String TAG = "";
    protected View mRootView;
    protected Activity mActivity;
    protected CompositeSubscription mCompositeSubscription;
    private ZLoadingDialog mProgressDialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
    }

    public void init() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
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
    protected <U> void addRxBusSubscribe(Class<U> eventType, Action1<U> act) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(RxBus.getDefault().toDefaultObservable(eventType, act));
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

    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ZLoadingDialog(mActivity);

            mProgressDialog.setLoadingBuilder(LEAF_ROTATE)//设置类型
                    .setLoadingColor(Color.GRAY)//颜色
                    .setHintText("乐福院子")
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                    .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                    .show();
        }
        mProgressDialog.show();
    }

    /*public void showLoadingDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
        }

        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }*/

    public void dismiss() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    public void doFailed(){
        UIUtils.showFailed();
    }

    public void doSuccess(){
        UIUtils.showSuccess();
    }

    public void showToast(String string){
        UIUtils.displayToast(string);
    }
    public void showToast(int id){
        UIUtils.displayToast(getString(id));
    }


}

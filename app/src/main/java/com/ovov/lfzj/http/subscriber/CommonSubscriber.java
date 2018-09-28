package com.ovov.lfzj.http.subscriber;






import com.ovov.lfzj.base.BaseActivity;

import rx.Subscriber;

/**
 *
 * @author jzxiang
 * create at 7/6/17 23:16
 */
public abstract class CommonSubscriber<T> extends Subscriber<T> {
    private BaseActivity mAct;
    private String mErrorMsg;

    protected CommonSubscriber(BaseActivity act){
        this.mAct = act;
    }

    protected CommonSubscriber(BaseActivity act, String errorMsg){
        this.mAct = act;
        this.mErrorMsg = errorMsg;
    }

    public CommonSubscriber() {

    }



    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        
    }

    @Override
    public void onNext(T t) {

    }

    //    @Override
//    public void onError(Throwable e) {
//        if (mAct == null||e == null)
//            return;
//
//        mErrorMsg = e.getMessage();
//
//        if (e instanceof ApiException) {
//            mAct.showError(e.toString());
//        } else if (e instanceof HttpException) {
//            mAct.showError("数据加载失败!");
//        } else if (!TextUtils.isEmpty(mErrorMsg)) {
//            mAct.showError(mErrorMsg);
//        } else {
//            mAct.showError("未知错误!");
//        }
//    }
}

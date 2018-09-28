package com.ovov.lfzj.opendoor.present;


import android.util.Log;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.opendoor.view.ApplyVisitorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

/**
 * 上传访客资料方法
 * Created by Administrator on 2017/12/19.
 */

public class ApplyVisitorPresent {
    private ApplyVisitorView visitorView;

    public ApplyVisitorPresent(ApplyVisitorView view){
        this.visitorView=view;
    }

    //  提交访客资料
    public void setVisitorUpInfo( String sub_id, String v_name, String v_phone, String v_num, String active_time){

        Subscription subscription = RetrofitHelper.getInstance().getUpVisitorInfo(sub_id,v_name, v_phone,v_num,active_time)
                .compose(RxUtil.<ServerFeedBackInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ServerFeedBackInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            visitorView.showMsg(dataResultException.errorInfo);
                        } else {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ServerFeedBackInfo listInfoDataInfo) {
                        Log.e("dadada",listInfoDataInfo+"");
                            visitorView.setVisitorData(listInfoDataInfo);
                    }
                });
        visitorView.addSubscrebe(subscription);
    }
}

//package com.ovov.lfzj.opendoor.present;
//
//import com.ovov.lfzj.base.bean.ListInfo;
//import com.ovov.lfzj.base.bean.VisistorRecordResult;
//import com.ovov.lfzj.base.net.DataResultException;
//
//import com.ovov.lfzj.base.utils.RxUtil;
//import com.ovov.lfzj.home.bean.BannerBean;
//import com.ovov.lfzj.http.RetrofitHelper;
//import com.ovov.lfzj.http.subscriber.CommonSubscriber;
//import com.ovov.lfzj.opendoor.view.VisistorView;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import rx.Subscription;
//
///**
// * Created by Administrator on 2017/9/28.
// */
//
//public class VisistorPresenter {
//    private VisistorView visistorView;
//
//    public VisistorPresenter(VisistorView visistorView) {
//        this.visistorView = visistorView;
//    }
//
//    public void getVisistorRecord(int type int page) {
//
//
//        Subscription subscription = RetrofitHelper.getInstance().getVisitorLog(page)
//                .compose(RxUtil.<VisistorRecordResult>rxSchedulerHelper())
//                .subscribe(new CommonSubscriber<VisistorRecordResult>() {
//                    @Override
//                    public void onError(Throwable e) {
//
//
//                        if (e instanceof DataResultException) {
//                            DataResultException dataResultException = (DataResultException) e;
//                            visistorView.showMsg(dataResultException.errorInfo);
//                        } else {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onNext(VisistorRecordResult listInfoDataInfo) {
//                        visistorView.setMoreData(listInfoDataInfo);
//
//
//                    }
//
//                });
//        visistorView.addSubscrebe(subscription);
//
//    }
//}

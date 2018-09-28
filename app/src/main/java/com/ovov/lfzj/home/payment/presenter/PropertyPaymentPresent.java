package com.ovov.lfzj.home.payment.presenter;

import android.util.Log;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.payment.view.PropertyPaymentView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

/**
 * 缴费账单数据请求
 * Created by Administrator on 2017/9/9.
 */

public class PropertyPaymentPresent {
    private PropertyPaymentView paymentView;

    public PropertyPaymentPresent(PropertyPaymentView view) {
        this.paymentView = view;
    }


    // 未缴费账单
    public void getPropertyOrder(final int type, int page) {
        if (type == REFRESH) {
            page = 1;

        } else if (type == LOADMORE) {
            page = page + 1;
        }

        /*Subscription subscription = RetrofitHelper.getInstance().getOrderUndone(page)
                .compose(RxUtil.<ListInfo<PropertyPaymentInfo.DataBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<PropertyPaymentInfo.DataBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (type == REFRESH) {
                            paymentView.finishRefresh(false);

                        } else {
                            paymentView.finishLoadmore(false);
                        }

                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            paymentView.showMsg(dataResultException.errorInfo);
                        } else {

                            paymentView.doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<PropertyPaymentInfo.DataBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            if (listInfoDataInfo.datas().size() > 0) {

                                paymentView.getPropertyOrder(listInfoDataInfo);
                            }
                            if (type == REFRESH) {
                                paymentView.getPropertyOrderMore(listInfoDataInfo);


                            } else {
                                paymentView.listLoadmore(listInfoDataInfo.datas());


                            }
                        } else {
                            if (type == REFRESH) {
                                paymentView.finishRefresh(false);

                            } else {
                                paymentView.finishLoadmore(false);
                            }
                        }


                    }
                });
        paymentView.addSubscrebe(subscription);*/
    }

    // 更多未缴费账单
    public void getPropertyOrderMore(String token, String subdistrict_id, int page) {
//        Call<PropertyPaymentInfo> call= NetClient.getInstance().getLeFUApi().getPropertyOrder(token,subdistrict_id,page);
//        call.enqueue(paymentInfoMore);
    }

    Callback<PropertyPaymentInfo> paymentPastCallback = new Callback<PropertyPaymentInfo>() {
        @Override
        public void onResponse(Call<PropertyPaymentInfo> call, Response<PropertyPaymentInfo> response) {
            PropertyPaymentInfo result = response.body();
            if (result != null) {
                paymentView.getPropertyOrderPast(result);
            }
        }

        @Override
        public void onFailure(Call<PropertyPaymentInfo> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                paymentView.getDataNone(dataResultException);
            }
        }
    };
//    // 已缴费缴费账单
//    public void getPropertyOrderPast(String token, String subdistrict_id, int page){
////        Call<PropertyPaymentInfo> call= NetClient.getInstance().getLeFUApi().getPropertyOrderPast(token,subdistrict_id,page);
////        call.enqueue(paymentPastCallback);
//    }
//    Callback<PropertyPaymentInfo> paymentPastMore=new Callback<PropertyPaymentInfo>() {
//        @Override
//        public void onResponse(Call<PropertyPaymentInfo> call, Response<PropertyPaymentInfo> response) {
//            PropertyPaymentInfo result=response.body();
//            if (result!=null ){
//                paymentView.getPropertyOrderMore(result);
//            }
//        }
//
//        @Override
//        public void onFailure(Call<PropertyPaymentInfo> call, Throwable t) {
//            if (t instanceof DataResultException) {
//                DataResultException dataResultException = (DataResultException) t;
//                paymentView.showMsg(dataResultException.errorInfo);
//            }
//        }
//    };
//    // 更多已缴费缴费账单
//    public void getOrderPastMore(String token, String subdistrict_id, int page){
////        Call<PropertyPaymentInfo> call= NetClient.getInstance().getLeFUApi().getPropertyOrderPast(token,subdistrict_id,page);
////        call.enqueue(paymentPastMore);
//    }
}

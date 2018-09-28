package com.ovov.lfzj.home.payment.presenter;

import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.home.bean.PaymentDetailResult;
import com.ovov.lfzj.home.payment.view.PaymentDetailView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** 业主缴费账单详情请求数据
 * Created by Administrator on 2017/11/23.
 */

public class PaymentDetailPresenter {
    private PaymentDetailView paymentDetailView;

    public PaymentDetailPresenter(PaymentDetailView paymentDetailView) {
        this.paymentDetailView = paymentDetailView;
    }
    Callback<PaymentDetailResult> callback = new Callback<PaymentDetailResult>() {
        @Override
        public void onResponse(Call<PaymentDetailResult> call, Response<PaymentDetailResult> response) {
            PaymentDetailResult result = response.body();
            if (result!=null){
                paymentDetailView.setData(result);   // 获取账单详情
            }
        }

        @Override
        public void onFailure(Call<PaymentDetailResult> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                paymentDetailView.showMsg(dataResultException);
            }
        }
    };
    // 获取业主缴费账单详情
    public void getPaymentDetail(String token, String order_id, String sub_id){
//        Call<PaymentDetailResult> call = NetClient.getInstance().getLeFUApi().getPaymentDetail(token,order_id,sub_id);
//        call.enqueue(callback);
    }
}

package com.ovov.lfzj.home.payment.presenter;

import android.util.Log;

import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.home.bean.PayInfo;
import com.ovov.lfzj.home.bean.PayResult;
import com.ovov.lfzj.home.bean.PaymentPayView;
import com.ovov.lfzj.home.bean.WXPayInfo;
import com.ovov.lfzj.home.bean.WxPaySuccessResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 物业缴费、工单、跳蚤市场、家政服务支付方法
 * Created by Administrator on 2017/9/26.
 */

public class PaymentPayPresenter {
    private PaymentPayView paymentPayView;

    public PaymentPayPresenter(PaymentPayView paymentPayView) {
        this.paymentPayView = paymentPayView;
    }

    //  微信支付调起
    private Callback<WXPayInfo> WXPayCallback = new Callback<WXPayInfo>() {
        @Override
        public void onResponse(Call<WXPayInfo> call, Response<WXPayInfo> response) {

            WXPayInfo result = response.body();
            Log.e("t.getmessage",result.getData().getPrepayid()+"ccc");
            if (result != null) {
                paymentPayView.setWXPayInfo(result);
            }
        }

        @Override
        public void onFailure(Call<WXPayInfo> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                paymentPayView.showMsg(dataResultException);

            }
        }
    };
    //  微信支付结果回调
    private Callback<WxPaySuccessResult> WXPayResultCallback = new Callback<WxPaySuccessResult>() {
        @Override
        public void onResponse(Call<WxPaySuccessResult> call, Response<WxPaySuccessResult> response) {
            WxPaySuccessResult result = response.body();
            if (result != null) {
                paymentPayView.getWXPayResult(result);
            }
        }

        @Override
        public void onFailure(Call<WxPaySuccessResult> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                paymentPayView.showMsg(dataResultException);
            }
        }
    };

    //  支付宝支付调起
    Callback<PayInfo> aliPayInfoCallback = new Callback<PayInfo>() {
        @Override
        public void onResponse(Call<PayInfo> call, Response<PayInfo> response) {
            PayInfo result = response.body();
            if (result!=null){
                if (result.getData() != null) {
                    paymentPayView.setPayInfo(result);
                }
            }
        }

        @Override
        public void onFailure(Call<PayInfo> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                paymentPayView.showMsg(dataResultException);
            }
        }
    };
    //  支付宝支付结果回调
    Callback<PayResult> callbackAliResult = new Callback<PayResult>() {
        @Override
        public void onResponse(Call<PayResult> call, Response<PayResult> response) {
            PayResult result = response.body();
            if (result!=null){
                paymentPayView.getAliPayResult(result);
            }
        }

        @Override
        public void onFailure(Call<PayResult> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                paymentPayView.showMsg(dataResultException);
            }
        }
    };

//    //  调起微信支付（物业缴费）——————工单，家政订单
//    public void WXPayInfo(String token, String order_id, String order_number, String sub_id, String type, String version) {
//    version    Call<WXPayInfo> call = NetClient.getInstance().getLeFUApi().getWXPayInfo(token, order_id, order_number, type, sub_id,version);
//        call.enqueue(WXPayCallback);
//    }

    //  微信支付返回结果
    public void getWXPaResult(String token, String order_id, String order_number, String type, String sub_id) {
//        Call<WxPaySuccessResult> call = NetClient.getInstance().getLeFUApi().getWxPayResult(token, order_id, order_number, type, sub_id);
//        call.enqueue(WXPayResultCallback);
    }


//    //  支付宝调起支付宝支付
//    public void aliPayInfoOrder(String token, String order_id, String order_num, String type, String sub_id, String version) {
//        Call<PayInfo> call = null;
//        if (type!=null && type.equals("prototal")){      // 缴费账单合并支付
//            call = NetClient.getInstance().getLeFUApi().getAlipayCheckOrder(token,order_id,order_num,type,sub_id,version);
//        }
//        if (type!=null && type.equals("flea")){          //  跳蚤市场支付
//            call = NetClient.getInstance().getLeFUApi().getAlipayPayFleaOrder(token,order_id,order_num,type,sub_id);
//        }
//        if (type!=null && type.equals("work")){          //  工单支付
//            call = NetClient.getInstance().getLeFUApi().getAliPayPayOrder(token,order_id,order_num,type,sub_id);
//        }
//        if (type!=null && type.equals("domestic")){     //  家政服务订单支付
//            call = NetClient.getInstance().getLeFUApi().getAliPayPayOrder(token,order_id,order_num,type,sub_id);
//        }
//        call.enqueue(aliPayInfoCallback);
//    }
//
//    //  物业缴费调起支付宝支付
//    public void aliPayInfo(String token, String order_id, String order_num, String sub_id, String type, String version) {
//        Call<PayInfo> call = NetClient.getInstance().getLeFUApi().getAliPayPayOrder(token,order_id,order_num,type,sub_id);
//        call.enqueue(aliPayInfoCallback);
//    }
//
//    //  支付宝支付结果回调
//    public void getAlipayResult(String token, String order_id, String order_num, String sub_id, String type) {
//        Call<PayResult> call = NetClient.getInstance().getLeFUApi().getAliPayResult(token,order_id, order_num,sub_id,type);
//        call.enqueue(callbackAliResult);
//    }
}

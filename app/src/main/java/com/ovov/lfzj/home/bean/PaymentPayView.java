package com.ovov.lfzj.home.bean;

import com.ovov.lfzj.base.net.DataResultException;

/**
 *  支付接口
 * Created by Administrator on 2017/9/14.
 */

public interface PaymentPayView {
    void setPayInfo(PayInfo payInfo);                 // 调起支付宝支付
    void setWXPayInfo(WXPayInfo wxPayInfo);           // 调起微信支付
    void getAliPayResult(PayResult result);           // 支付宝支付结果
    void getWXPayResult(WxPaySuccessResult result);   // 微信支付结果
    void showMsg(DataResultException dataResult);
}

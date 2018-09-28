package com.ovov.lfzj.home.payment.view;

import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.home.bean.PaymentDetailResult;


/** 业主缴费账单详情接口
 * Created by Administrator on 2017/11/23.
 */

public interface PaymentDetailView {
    void setData(PaymentDetailResult result);   // 获取账单详情
    void showMsg(DataResultException dataResult);
}

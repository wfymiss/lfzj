package com.ovov.lfzj.home.payment.view;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;

import java.util.List;

import rx.Subscription;


/**
 * 物业缴费账单接口
 * Created by Administrator on 2017/9/9.
 */

public interface PropertyPaymentView {
    void getPropertyOrder(ListInfo<PropertyPaymentInfo> listInfoDataInfo);       // 缴费账单

    void getPropertyOrderPast(PropertyPaymentInfo infoData);   // 账单历史

    void getPropertyOrderMore(ListInfo<PropertyPaymentInfo> listInfoDataInfo);   // 更多账单信息

    void getDataNone(DataResultException dataResult);    // 刷新数据失败

    void finishRefresh(boolean s);

    void listRefresh();

    void listLoadmore(List<PropertyPaymentInfo> datas);

    void finishLoadmore(boolean s);

    void showMsg(String dataResult);

    void doFailed();

    void addSubscrebe(Subscription subscription);
}

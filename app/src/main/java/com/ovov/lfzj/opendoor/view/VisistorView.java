package com.ovov.lfzj.opendoor.view;

import com.ovov.lfzj.base.bean.VisistorRecordResult;
import com.ovov.lfzj.base.net.DataResultException;

import rx.Subscription;


/**
 * Created by Administrator on 2017/9/28.
 */

public interface VisistorView {
    void setVisistorData(VisistorRecordResult recordResult);
    void setNoData();
    void setMoreData(VisistorRecordResult recordResult);
    void setNoMore();
    void addSubscrebe(Subscription subscription);
    void showMsg(String dataResult);
}

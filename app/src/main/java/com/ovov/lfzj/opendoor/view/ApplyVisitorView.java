package com.ovov.lfzj.opendoor.view;

import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.net.DataResultException;

import rx.Subscription;

/**
 *  访客通行上传访客资料
 * Created by Administrator on 2017/12/19.
 */

public interface ApplyVisitorView {
    void setVisitorData(ServerFeedBackInfo infoData);   // 上传访客资料
    void showMsg(String dataResult);
    void addSubscrebe(Subscription dataResult);
}

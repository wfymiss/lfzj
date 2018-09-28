package com.ovov.lfzj.event;

import com.ovov.lfzj.base.bean.SquareDetailInfo;

import java.util.List;

/**
 * Created by kaite on 2018/9/18.
 */

public class SquareGoodEvent {
    public List<SquareDetailInfo.FabulousBean> goodBean;

    public SquareGoodEvent(List<SquareDetailInfo.FabulousBean> replyBean) {
        this.goodBean = replyBean;
    }
}

package com.ovov.lfzj.event;

import com.ovov.lfzj.base.bean.SquareDetailInfo;

import java.util.List;

/**
 * Created by kaite on 2018/9/18.
 */

public class SquareReplayEvent {
    public List<SquareDetailInfo.ForwardBean> replayBean;

    public SquareReplayEvent(List<SquareDetailInfo.ForwardBean> replyBean) {
        this.replayBean = replyBean;
    }
}

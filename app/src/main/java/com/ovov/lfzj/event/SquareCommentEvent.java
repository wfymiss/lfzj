package com.ovov.lfzj.event;

import com.ovov.lfzj.base.bean.SquareDetailInfo;

import java.util.List;

/**
 * Created by kaite on 2018/9/18.
 */

public class SquareCommentEvent {
    public List<SquareDetailInfo.ReplyBean> replyBean;

    public SquareCommentEvent(List<SquareDetailInfo.ReplyBean> replyBean) {
        this.replyBean = replyBean;
    }
}

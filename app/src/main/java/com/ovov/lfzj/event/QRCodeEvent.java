package com.ovov.lfzj.event;

/**
 * 没有钥匙，生成二维码失败监听事件
 * Created by Administrator on 2017/11/2.
 */

public class QRCodeEvent {
    private String msg;

    public QRCodeEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

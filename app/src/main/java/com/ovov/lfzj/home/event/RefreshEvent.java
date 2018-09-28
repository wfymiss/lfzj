package com.ovov.lfzj.home.event;

/**
 * 页面刷新监听
 * Created by Administrator on 2018/4/30.
 */

public class RefreshEvent {
    private String success;
    private String fail;

    public RefreshEvent(){
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }
}

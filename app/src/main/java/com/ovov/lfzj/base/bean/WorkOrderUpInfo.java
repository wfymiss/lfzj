package com.ovov.lfzj.base.bean;

/**
 * 工单提交返回实体类
 * Created by Administrator on 2017/9/4.
 */

public class WorkOrderUpInfo {

    /**
     * data : {"msg":"提交成功"}
     * status : 0
     */

    private int status;
    private int error_code;
    private String error_msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}

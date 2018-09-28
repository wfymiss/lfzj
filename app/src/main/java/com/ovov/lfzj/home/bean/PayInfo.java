package com.ovov.lfzj.home.bean;

/**
 * 支付宝支付调用实体类
 * Created by Administrator on 2017/9/14.
 */

public class PayInfo {
    private int status;
    private String data;

    private int error_code;
    private String error_msg;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

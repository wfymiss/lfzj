package com.ovov.lfzj.base.bean;

/**
 * 服务端信息反馈
 * Created by lyy on 2017/11/21.
 */

public class ServerFeedBackInfo {

    /**
     * data : {"msg":"删除成功！"}
     * status : 0
     */


    public void setCode(String code) {
        this.code = code;
    }

    public void setDatas(boolean datas) {
        this.datas = datas;
    }



    public String getCode() {
        return code;
    }

    public boolean isDatas() {
        return datas;
    }

    private int status;
    private int error_code;
    private String error_msg;
    private String code;
    private boolean datas;


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

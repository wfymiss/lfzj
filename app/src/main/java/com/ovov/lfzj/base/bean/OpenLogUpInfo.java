package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * 上传用户开门日志 ——-返回的结果信息
 * Created by lyy on 2017/9/1.
 */

public class OpenLogUpInfo {

    /**
     * data : []
     * status : 1
     * error_code : 10003
     * error_msg : 暂无权限申请
     */

    private int status;
    private int error_code;
    private String error_msg;
    private List<?> datas;

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

    public List<?> getData() {
        return datas;
    }

    public void setData(List<?> data) {
        this.datas = data;
    }
}

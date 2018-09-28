package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class UnitListResult {

    /**
     * data : [{"unit":"1"},{"unit":"2"},{"unit":"3"}]
     * status : 0
     */

    private int status;
    private String error_code;
    private String error_msg;
    private List<DataBean> data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * unit : 1
         */

        private String unit;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}

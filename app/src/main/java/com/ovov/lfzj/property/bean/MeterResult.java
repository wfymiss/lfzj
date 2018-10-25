package com.ovov.lfzj.property.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MeterResult {

    /**
     * data : [{"name":"水表","gid":"46"},{"name":"电表","gid":"45"},{"name":"煤气表","gid":"44"}]
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
         * name : 水表
         * gid : 46
         */

        private String name;
        private String gid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }
    }
}

package com.ovov.lfzj.property.bean;

/**
 * Created by Administrator on 2017/12/7.
 */

public class ReadMeterResult {

    /**
     * data : {"status":0,"msg":"抄表成功"}
     * status : 0
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * status : 0
         * msg : 抄表成功
         */

        private int status;
        private String msg;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

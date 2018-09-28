package com.ovov.lfzj.home.bean;

/**
 *  微信支付结果回调
 * Created by Administrator on 2017/9/22.
 */

public class WxPaySuccessResult {
    /**
     * data : {"order_number":"DS201801120012","order_price":"0.01","pay_type":"zfb","pay_time":"2018-01-12 16:17:12","status":"3"}
     * status : 0
     */

    private DataBean data;
    private int status;
    private int error_code;
    private String error_msg;    // 请求失败


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

    public static class DataBean {
        /**
         * order_number : DS201801120012
         * order_price : 0.01
         * pay_type : zfb
         * pay_time : 2018-01-12 16:17:12
         * status : 3
         */

        private String order_number;
        private String order_price;
        private String pay_type;
        private String pay_time;
        private String status;
        private String goods_status;
        private String total_price;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        private String msg;

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGoods_status() {
            return goods_status;
        }

        public void setGoods_status(String goods_status) {
            this.goods_status = goods_status;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }
    }
}

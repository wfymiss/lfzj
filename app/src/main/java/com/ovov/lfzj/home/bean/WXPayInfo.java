package com.ovov.lfzj.home.bean;

import com.google.gson.annotations.SerializedName;

/**
 *  微信调用实体类
 * Created by Administrator on 2017/9/19.
 */

public class WXPayInfo {

    /**
     * data : {"appid":"wx9f61a260082f46ff","noncestr":"vapu4rufvgs0w9aer4h5ql0yhzqpp1ti","package":"Sign=WXPay","partnerid":"1410954002","prepayid":"wx20180331185536c21eb64ced0271378399","timestamp":1522493737,"sign":"F2DFE2E05E3552FA281EEE1243432FDC"}
     * status : 0
     */

    private DataBean data;
    private int status;
    private int error_code;      // 失败错误代码
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
         * appid : wx9f61a260082f46ff
         * noncestr : vapu4rufvgs0w9aer4h5ql0yhzqpp1ti
         * package : Sign=WXPay
         * partnerid : 1410954002
         * prepayid : wx20180331185536c21eb64ced0271378399
         * timestamp : 1522493737
         * sign : F2DFE2E05E3552FA281EEE1243432FDC
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private int timestamp;
        private String sign;

        private String msg;          // 调起失败返回值

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}

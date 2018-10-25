package com.ovov.lfzj.property.bean;

/**
 * Created by Administrator on 2017/12/7.
 */

public class InformMeterResult {

    /**
     * data : {"username":"昝宏图        张凤喜","houses_id":"43","now_value":0,"meter_number":""}
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
         * username : 昝宏图        张凤喜
         * houses_id : 43
         * now_value : 0
         * meter_number :
         */

        private String username;
        private String houses_id;
        private String now_value;
        private String meter_number;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHouses_id() {
            return houses_id;
        }

        public void setHouses_id(String houses_id) {
            this.houses_id = houses_id;
        }

        public String getNow_value() {
            return now_value;
        }

        public void setNow_value(String now_value) {
            this.now_value = now_value;
        }

        public String getMeter_number() {
            return meter_number;
        }

        public void setMeter_number(String meter_number) {
            this.meter_number = meter_number;
        }
    }
}

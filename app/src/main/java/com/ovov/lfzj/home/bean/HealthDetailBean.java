package com.ovov.lfzj.home.bean;

/**
 * Created by kaite on 2018/10/10.
 */

public class HealthDetailBean {
    /**
     * code : 200
     * datas : {"id":18,"user_id":5174,"order_sn":"2018101010431656355174","time_id":14,"time":"2018-10-10 15:46\u201416:00","addtime":1539139396,"status":0,"remainingDays":0,"site":"凯德世家"}
     */

    private String code;
    private DatasBean datas;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * id : 18
         * user_id : 5174
         * order_sn : 2018101010431656355174
         * time_id : 14
         * time : 2018-10-10 15:46—16:00
         * addtime : 1539139396
         * status : 0
         * remainingDays : 0
         * site : 凯德世家
         */

        private String id;
        private String user_id;
        private String order_sn;
        private String time_id;
        private String time;
        private String addtime;
        private String status;
        private String remainingDays;
        private String site;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getTime_id() {
            return time_id;
        }

        public void setTime_id(String time_id) {
            this.time_id = time_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemainingDays() {
            return remainingDays;
        }

        public void setRemainingDays(String remainingDays) {
            this.remainingDays = remainingDays;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }
    }
}

package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 */

public class VisistorRecordResult {

    /**
     * data : [{"id":"28","subdistrict_id":"0","proprietor_id":"18636163349","visitor":"睡觉睡觉","pic":null,"visitor_tel":"17768373003","valid_num":"5","created_time":"2017-09-11 16:09:00","active_time":"1"}]
     * status : 0
     */

    private String code;
    private List<DataBean> datas;

    public String getCode() {
        return code;
    }

    public List<DataBean> getData() {
        return datas;
    }

    public void setData(List<DataBean> datas) {
        this.datas = datas;
    }

    public static class DataBean {
        /**
         * id : 28
         * subdistrict_id : 0
         * proprietor_id : 18636163349
         * visitor : 睡觉睡觉
         * pic : null
         * visitor_tel : 17768373003
         * valid_num : 5
         * created_time : 2017-09-11 16:09:00
         * active_time : 1
         *
         *   "id": 1,
         "subdistrict_id": 1,
         "proprietor_id": "18888888888",
         "from_user_id": 100,
         "qr_code": null,
         "visitor": "小B",
         "visitor_tel": "16666666666",
         "to_user_id": 17,
         "valid_num": 8,
         "active_time": 10,
         "created_at": "2018-09-25 12:20:20",
         "updated_at": "2018-09-25 12:20:20"
         */

        private String id;
        private String subdistrict_id;
        private String proprietor_id;
        private String visitor;
        private String pic;
        private String visitor_tel;
        private String valid_num;
        private String created_at;
        private String active_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubdistrict_id() {
            return subdistrict_id;
        }

        public void setSubdistrict_id(String subdistrict_id) {
            this.subdistrict_id = subdistrict_id;
        }

        public String getProprietor_id() {
            return proprietor_id;
        }

        public void setProprietor_id(String proprietor_id) {
            this.proprietor_id = proprietor_id;
        }

        public String getVisitor() {
            return visitor;
        }

        public void setVisitor(String visitor) {
            this.visitor = visitor;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getVisitor_tel() {
            return visitor_tel;
        }

        public void setVisitor_tel(String visitor_tel) {
            this.visitor_tel = visitor_tel;
        }

        public String getValid_num() {
            return valid_num;
        }

        public void setValid_num(String valid_num) {
            this.valid_num = valid_num;
        }

        public String getCreated_time() {
            return created_at;
        }

        public void setCreated_time(String created_time) {
            this.created_at = created_time;
        }

        public String getActive_time() {
            return active_time;
        }

        public void setActive_time(String active_time) {
            this.active_time = active_time;
        }
    }
}

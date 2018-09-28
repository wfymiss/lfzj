package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * 用户开门日志
 * Created by Administrator on 2017/9/1.
 */

public class OpenLogRecoder {

    /**
     * data : [{"id":"91","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"AE0B94DB52DB6","link":"摇一摇","result":"成功","created_time":"2017-09-14 13:44:44"},{"id":"92","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"AE0B94DB52DB6","link":"摇一摇","result":"成功","created_time":"2017-09-14 13:44:53"},{"id":"116","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"kkjkjj","link":"1","result":"2活动","created_time":"2004-00-00 00:00:00"},{"id":"117","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"kkjkjj","link":"1","result":"2活动","created_time":"2004-00-00 00:00:00"},{"id":"118","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"AE0B94DB52DB6","link":"蓝牙","result":"开门成功","created_time":"0000-00-00 00:00:00"},{"id":"119","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"AE0B94DB52DB6","link":"蓝牙","result":"开门失败","created_time":"0000-00-00 00:00:00"},{"id":"120","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"AE0B94DB52DB6","link":"蓝牙","result":"开门失败","created_time":"0000-00-00 00:00:00"},{"id":"121","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"kkjkjj","link":"1","result":"2活动","created_time":"2004-00-00 00:00:00"},{"id":"122","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"kkjkjj","link":"1","result":"2活动","created_time":"2004-00-00 00:00:00"},{"id":"123","subdistrict_id":"1","proprietor_id":"18735106410","location_id":"AE0B94DB52DB6","link":"蓝牙","result":"开门成功","created_time":"0000-00-00 00:00:00"}]
     * {"data":[{"created_time":"2017-09-15 19:56:53","sn_name":"\u5c0f\u533a\u5357\u95e8","key":"2DF482459021938071680A1275598C2DE19FEA29FB526B912AFC187FA6D89E4A2BE3C1516B50C07E5F044554232F38AC5324","link":["wifi","blue"]},{"created_time":"2017-09-15 19:56:53","sn_name":"\u6d4b\u8bd5","key":"D04174D1313F4736990C915777A86ED0BAB528774E9D799A93A21BCA07CED541AF9C4034B5998570EF67AB5B64D242D73042","link":["wifi","blue"]},{"created_time":"2017-09-15 19:56:53","sn_name":"\u6d4b\u8bd5\u95e8\u7981","key":"9EE11138AF54057CFA81FEAC7242DF323B4026D8721E65FCAB8254EDA6F0D63E1FC66B2B1C03DB4A5CA1705260A155CB8271","link":["wifi","blue"]},{"created_time":"2017-09-15 19:56:53","sn_name":"A28F366B88192","key":"C8D1BBAB8F346008C5EC26BA2420C6204E723BA78D394026F5E023F3D1CFF6D09923E72982A6BDEA7989E576BA5545964240","link":["wifi","blue"]},{"created_time":"2017-09-15 19:56:53","sn_name":"AE0B94DB52DB6","key":"98B23312C711FF619463C5EA225BF8BE1998B561F185D3372E731C97E8962823016DAF8562741D3869A8F06265AAB02B1155","link":["wifi","blue","code"]}],"status":0}
     * status : 0
     */

    private int status;
    private List<DataBean> data;

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
         * id : 91
         * subdistrict_id : 1
         * proprietor_id : 18735106410
         * location_id : AE0B94DB52DB6
         * link : 摇一摇
         * result : 成功
         * created_time : 2017-09-14 13:44:44
         */

        private String id;
        private String subdistrict_id;
        private String proprietor_id;
        private String location_id;
        private String link;
        private String result;
        private String created_time;

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

        public String getLocation_id() {
            return location_id;
        }

        public void setLocation_id(String location_id) {
            this.location_id = location_id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }
    }
}

package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * 用户首次申请钥匙
 * Created by Administrator on 2017/8/31.
 */

public class KeyReplyInfo {


    /**
     * data : [{"created_time":"2017-09-08 15:20:28","sn_name":"小区南门","key":"2DF482459021938071680A1275598C2DE19FEA29FB526B912AFC187FA6D89E4A2BE3C1516B50C07E5F044554232F38AC5324","link":["wifi","blue"]},{"created_time":"2017-09-08 15:20:28","sn_name":"测试","key":"D04174D1313F4736990C915777A86ED0BAB528774E9D799A93A21BCA07CED541AF9C4034B5998570EF67AB5B64D242D73042","link":["wifi","blue"]},{"created_time":"2017-09-08 15:20:28","sn_name":"测试门禁","key":"9EE11138AF54057CFA81FEAC7242DF323B4026D8721E65FCAB8254EDA6F0D63E1FC66B2B1C03DB4A5CA1705260A155CB8271","link":["wifi","blue"]},{"created_time":"2017-09-08 15:20:28","sn_name":"A28F366B88192","key":"C8D1BBAB8F346008C5EC26BA2420C6204E723BA78D394026F5E023F3D1CFF6D09923E72982A6BDEA7989E576BA5545964240","link":["wifi","blue"]}]
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
         * created_time : 2017-09-08 15:20:28
         * sn_name : 小区南门
         * key : 2DF482459021938071680A1275598C2DE19FEA29FB526B912AFC187FA6D89E4A2BE3C1516B50C07E5F044554232F38AC5324
         * link : ["wifi","blue"]
         */

        private String created_time;
        private String sn_name;
        private String key;
        private List<String> link;

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getSn_name() {
            return sn_name;
        }

        public void setSn_name(String sn_name) {
            this.sn_name = sn_name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getLink() {
            return link;
        }

        public void setLink(List<String> link) {
            this.link = link;
        }
    }
}

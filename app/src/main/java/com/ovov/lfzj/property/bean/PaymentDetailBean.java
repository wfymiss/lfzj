package com.ovov.lfzj.property.bean;

import java.util.List;

/**
 * Created by kaite on 2018/3/19.
 */

public class PaymentDetailBean {
    /**
     * room_number : 100栋1单元601
     * feeList : [{"billList":[{"category_name":"装修保证金","money":"3000"}],"date":"2018年-03月","sum":"3000"},{"billList":[{"category_name":"购买门禁卡","money":"40"}],"date":"2018年-02月","sum":"40"}]
     */



    private String house;
    private List<BillListBean> data;
    private String totalAmount;
    private String year;

    public String getRoom_number() {
        return house;
    }

    public void setRoom_number(String room_number) {
        this.house = room_number;
    }

    public List<BillListBean> getFeeList() {
        return data;
    }

    public void setFeeList(List<BillListBean> feeList) {
        this.data = feeList;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getHouse() {
        return house;
    }

    public List<BillListBean> getData() {
        return data;
    }

    public String getYear() {
        return year;
    }

    public static class BillListBean {
            /**
             * category_name : 装修保证金
             * money : 3000
             */

            private String name;
            private String money;

        public String getName() {
            return name;
        }

        public String getMoney() {
            return money;
        }
    }

}

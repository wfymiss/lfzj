package com.ovov.lfzj.property.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class ShopListResult {

    /**
     * data : [{"id":"3","door_number":"1001"},{"id":"5","door_number":"1002"},{"id":"7","door_number":"1004"}]
     * status : 0
     */

    private int status;
    private List<DataBean> datas;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return datas;
    }

    public void setData(List<DataBean> data) {
        this.datas = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * door_number : 1001
         */

        private String id;
        private String door_number;
        /**
         * street : 好运街
         * number : C10-C11(过道)
         * id : 2
         */

        private String street;
        private String number;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDoor_number() {
            return door_number;
        }

        public void setDoor_number(String door_number) {
            this.door_number = door_number;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

    }
}

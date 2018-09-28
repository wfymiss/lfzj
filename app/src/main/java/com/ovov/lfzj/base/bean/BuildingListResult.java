package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * 获取小区楼宇
 * Created by Administrator on 2017/12/7.
 */

public class BuildingListResult {

    /**
     * data : [{"building_id":"1"},{"building_id":"2"},{"building_id":"3"},{"building_id":"5"},{"building_id":"6"},{"building_id":"7"},{"building_id":"8"},{"building_id":"9"},{"building_id":"10"},{"building_id":"11"},{"building_id":"12"},{"building_id":"13"},{"building_id":"15"},{"building_id":"16"},{"building_id":"19"},{"building_id":"21"},{"building_id":"23"},{"building_id":"25"},{"building_id":"26"},{"building_id":"100"},{"building_id":"101"},{"building_id":"200"}]
     * status : 0
     */

    private int status;
    private String error_code;
    private String error_msg;
    private List<DataBean> data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

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
         * building_id : 1
         */

        private String building_id;

        public String getBuilding_id() {
            return building_id;
        }

        public void setBuilding_id(String building_id) {
            this.building_id = building_id;
        }
    }
}

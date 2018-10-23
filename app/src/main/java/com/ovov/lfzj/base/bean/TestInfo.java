package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/10/23.
 */

public class TestInfo {

    /**
     * repair_img : {"name":["1503049320_655696659.jpg","1503047888_860398976.jpg"],"type":["image/jpeg","image/jpeg"],"tmp_name":["C:\\Users\\admin\\AppData\\Local\\Temp\\php640F.tmp","C:\\Users\\admin\\AppData\\Local\\Temp\\php6410.tmp"],"error":[0,0],"size":[87350,118670]}
     */

    private RepairImgBean repair_img;

    public RepairImgBean getRepair_img() {
        return repair_img;
    }

    public void setRepair_img(RepairImgBean repair_img) {
        this.repair_img = repair_img;
    }

    public static class RepairImgBean {
        private List<String> name;
        private List<String> type;
        private List<String> tmp_name;
        private List<Integer> error;
        private List<Integer> size;

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<String> getType() {
            return type;
        }

        public void setType(List<String> type) {
            this.type = type;
        }

        public List<String> getTmp_name() {
            return tmp_name;
        }

        public void setTmp_name(List<String> tmp_name) {
            this.tmp_name = tmp_name;
        }

        public List<Integer> getError() {
            return error;
        }

        public void setError(List<Integer> error) {
            this.error = error;
        }

        public List<Integer> getSize() {
            return size;
        }

        public void setSize(List<Integer> size) {
            this.size = size;
        }
    }
}

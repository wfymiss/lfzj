package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/11/5.
 */

public class FamilyInfo {

    /**
     * houses : 16栋1单元101
     * info : [{"user_id":5827,"name":"张志军","mobile":"17734949109","relative_id":"","houses_id":"","subdistrict_id":"7"},{"name":null,"mobile":"15026830643","user_id":5827,"relative_id":6303,"houses_id":6093,"subdistrict_id":7}]
     */

    private String houses;
    private List<FailyBean> info;

    public String getHouses() {
        return houses;
    }

    public void setHouses(String houses) {
        this.houses = houses;
    }

    public List<FailyBean> getInfo() {
        return info;
    }

    public void setInfo(List<FailyBean> info) {
        this.info = info;
    }

    public static class FailyBean {
        /**
         * user_id : 5827
         * name : 张志军
         * mobile : 17734949109
         * relative_id :
         * houses_id :
         * subdistrict_id : 7
         */

        private String user_id;
        private String name;
        private String mobile;
        private String relative_id;
        private String houses_id;
        private String subdistrict_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRelative_id() {
            return relative_id;
        }

        public void setRelative_id(String relative_id) {
            this.relative_id = relative_id;
        }

        public String getHouses_id() {
            return houses_id;
        }

        public void setHouses_id(String houses_id) {
            this.houses_id = houses_id;
        }

        public String getSubdistrict_id() {
            return subdistrict_id;
        }

        public void setSubdistrict_id(String subdistrict_id) {
            this.subdistrict_id = subdistrict_id;
        }
    }
}

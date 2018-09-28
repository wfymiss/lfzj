package com.ovov.lfzj.home.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class SubdistrictsBean {

    private String subdistrict_id;
    private String subdistrict_name;
    private String description;
    private List<housesBean> houses;

    public String getSubdistrict_id() {
        return subdistrict_id;
    }

    public String getSubdistrict_name() {
        return subdistrict_name;
    }

    public String getDescription() {
        return description;
    }

    public List<housesBean> getHouses() {
        return houses;
    }

  public   class housesBean{
        private String  subdistrict_id;
        private String building_id;
        private String unit;
        private String floor;

        public String getSubdistrict_id() {
            return subdistrict_id;
        }

        public String getBuilding_id() {
            return building_id;
        }

        public String getUnit() {
            return unit;
        }

        public String getFloor() {
            return floor;
        }

        public String getNumber() {
            return number;
        }

        public String getOld_id() {
            return old_id;
        }

        public String getOld_subdistrict_id() {
            return old_subdistrict_id;
        }

        private String number;
        private String old_id;
        private String old_subdistrict_id;

    }
}

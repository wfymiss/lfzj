package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class UnitInfo {


    /**
     * id : 1
     * buildings : 1号楼
     * str_id : 1
     * building_sun : [{"building_unit_id":1,"building_unit_name":"1栋1单元","str_id":1},{"building_unit_id":2,"building_unit_name":"1栋2单元","str_id":2},{"building_unit_id":3,"building_unit_name":"1栋3单元","str_id":3}]
     */

    public int id;
    public String buildings;
    public String str_id;
    public List<BuildingSunBean> building_sun;


    public static class BuildingSunBean {
        /**
         * building_unit_id : 1
         * building_unit_name : 1栋1单元
         * str_id : 1
         */

        public int building_unit_id;
        public String building_unit_name;
        public int str_id;

    }
}


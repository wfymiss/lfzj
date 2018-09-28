package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class RoomInfo {

    /**
     * building_unit_name : 1栋1单元
     * houses_list : [{"id":1,"house_number":401,"str_id":401},{"id":2,"house_number":402,"str_id":402},{"id":3,"house_number":501,"str_id":501},{"id":4,"house_number":502,"str_id":502},{"id":5,"house_number":601,"str_id":601},{"id":6,"house_number":602,"str_id":602},{"id":7,"house_number":701,"str_id":701},{"id":8,"house_number":702,"str_id":702},{"id":9,"house_number":801,"str_id":801},{"id":10,"house_number":802,"str_id":802},{"id":11,"house_number":901,"str_id":901},{"id":12,"house_number":902,"str_id":902},{"id":13,"house_number":1001,"str_id":1001},{"id":14,"house_number":1002,"str_id":1002},{"id":15,"house_number":1101,"str_id":1101},{"id":16,"house_number":1102,"str_id":1102},{"id":17,"house_number":1201,"str_id":1201},{"id":18,"house_number":1202,"str_id":1202},{"id":19,"house_number":1301,"str_id":1301},{"id":20,"house_number":1302,"str_id":1302},{"id":21,"house_number":1401,"str_id":1401},{"id":22,"house_number":1402,"str_id":1402},{"id":23,"house_number":1501,"str_id":1501},{"id":24,"house_number":1502,"str_id":1502},{"id":25,"house_number":1601,"str_id":1601},{"id":26,"house_number":1602,"str_id":1602},{"id":27,"house_number":1701,"str_id":1701},{"id":28,"house_number":1702,"str_id":1702},{"id":29,"house_number":1801,"str_id":1801},{"id":30,"house_number":1802,"str_id":1802},{"id":31,"house_number":1901,"str_id":1901},{"id":32,"house_number":1902,"str_id":1902},{"id":33,"house_number":2001,"str_id":2001},{"id":34,"house_number":2002,"str_id":2002},{"id":35,"house_number":2101,"str_id":2101},{"id":36,"house_number":2102,"str_id":2102},{"id":37,"house_number":2201,"str_id":2201},{"id":38,"house_number":2202,"str_id":2202},{"id":39,"house_number":2301,"str_id":2301},{"id":40,"house_number":2302,"str_id":2302},{"id":41,"house_number":2401,"str_id":2401},{"id":42,"house_number":2402,"str_id":2402},{"id":43,"house_number":2501,"str_id":2501},{"id":44,"house_number":2502,"str_id":2502},{"id":45,"house_number":2601,"str_id":2601},{"id":46,"house_number":2602,"str_id":2602},{"id":47,"house_number":2701,"str_id":2701},{"id":48,"house_number":2702,"str_id":2702},{"id":49,"house_number":2801,"str_id":2801},{"id":50,"house_number":2802,"str_id":2802},{"id":151,"house_number":0,"str_id":0},{"id":152,"house_number":0,"str_id":0},{"id":157,"house_number":403,"str_id":403},{"id":3515,"house_number":401,"str_id":401},{"id":3516,"house_number":402,"str_id":402},{"id":3517,"house_number":501,"str_id":501},{"id":3518,"house_number":502,"str_id":502},{"id":3519,"house_number":601,"str_id":601},{"id":3520,"house_number":602,"str_id":602},{"id":3521,"house_number":701,"str_id":701},{"id":3522,"house_number":702,"str_id":702},{"id":3523,"house_number":801,"str_id":801},{"id":3524,"house_number":802,"str_id":802},{"id":3525,"house_number":901,"str_id":901},{"id":3526,"house_number":902,"str_id":902},{"id":3527,"house_number":1001,"str_id":1001},{"id":3528,"house_number":1002,"str_id":1002},{"id":3529,"house_number":1101,"str_id":1101},{"id":3530,"house_number":1102,"str_id":1102},{"id":3531,"house_number":1201,"str_id":1201},{"id":3532,"house_number":1202,"str_id":1202},{"id":3533,"house_number":1301,"str_id":1301},{"id":3534,"house_number":1302,"str_id":1302},{"id":3535,"house_number":1401,"str_id":1401},{"id":3536,"house_number":1402,"str_id":1402},{"id":3537,"house_number":1501,"str_id":1501},{"id":3538,"house_number":1502,"str_id":1502},{"id":3539,"house_number":1601,"str_id":1601},{"id":3540,"house_number":1602,"str_id":1602},{"id":3541,"house_number":1701,"str_id":1701},{"id":3542,"house_number":1702,"str_id":1702},{"id":3543,"house_number":1801,"str_id":1801},{"id":3544,"house_number":1802,"str_id":1802},{"id":3545,"house_number":1901,"str_id":1901},{"id":3546,"house_number":1902,"str_id":1902},{"id":3547,"house_number":2001,"str_id":2001},{"id":3548,"house_number":2002,"str_id":2002},{"id":3549,"house_number":2101,"str_id":2101},{"id":3550,"house_number":2102,"str_id":2102},{"id":3551,"house_number":2201,"str_id":2201},{"id":3552,"house_number":2202,"str_id":2202},{"id":3553,"house_number":2301,"str_id":2301},{"id":3554,"house_number":2302,"str_id":2302},{"id":3555,"house_number":2401,"str_id":2401},{"id":3556,"house_number":2402,"str_id":2402},{"id":3557,"house_number":2501,"str_id":2501},{"id":3558,"house_number":2502,"str_id":2502},{"id":3559,"house_number":2601,"str_id":2601},{"id":3560,"house_number":2602,"str_id":2602},{"id":3561,"house_number":2701,"str_id":2701},{"id":3562,"house_number":2702,"str_id":2702},{"id":3563,"house_number":2801,"str_id":2801},{"id":3564,"house_number":2802,"str_id":2802},{"id":3665,"house_number":0,"str_id":0},{"id":3666,"house_number":0,"str_id":0},{"id":3671,"house_number":403,"str_id":403}]
     */

    public String building_unit_name;
    public List<HousesListBean> houses_list;



    public static class HousesListBean {
        /**
         * id : 1
         * house_number : 401
         * str_id : 401
         */

        public int id;
        public String house_number;
        public int str_id;
        public boolean isSelect;

    }
}


package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class RoomListResult {

    /**
     * data : [{"number":"0"},{"number":"401"},{"number":"402"},{"number":"501"},{"number":"502"},{"number":"601"},{"number":"602"},{"number":"701"},{"number":"702"},{"number":"801"},{"number":"802"},{"number":"901"},{"number":"902"},{"number":"1001"},{"number":"1002"},{"number":"1101"},{"number":"1102"},{"number":"1201"},{"number":"1202"},{"number":"1301"},{"number":"1302"},{"number":"1401"},{"number":"1402"},{"number":"1501"},{"number":"1502"},{"number":"1601"},{"number":"1602"},{"number":"1701"},{"number":"1702"},{"number":"1801"},{"number":"1802"},{"number":"1901"},{"number":"1902"},{"number":"2001"},{"number":"2002"},{"number":"2101"},{"number":"2102"},{"number":"2201"},{"number":"2202"},{"number":"2301"},{"number":"2302"},{"number":"2401"},{"number":"2402"},{"number":"2501"},{"number":"2502"},{"number":"2601"},{"number":"2602"},{"number":"2701"},{"number":"2702"},{"number":"2801"},{"number":"2802"}]
     * status : 0
     */

    private int status;
    private String error_code;
    private String error_msg;
    private DataBean datas;

    public DataBean getDatas() {
        return datas;
    }

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


    public static class DataBean {
        /**
         * number : 0
         */
private List<Houses>houses_list;

        public List<Houses> getHouses_list() {
            return houses_list;
        }

        private String number;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
    public static class Houses {

        /**
         * id : 1
         * house_number : 401
         * str_id : 401
         */

        private String id;
        private String house_number;
        private String str_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHouse_number() {
            return house_number;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
        }

        public String getStr_id() {
            return str_id;
        }

        public void setStr_id(String str_id) {
            this.str_id = str_id;
        }
    }
}

package com.ovov.lfzj.event;

/**
 * 用户房间弹出框监听
 * Created by Administrator on 2017/10/28.
 */

public class HouseEvent {
    private String sub_id;
    private String sub_name;     // 小区名称
    private String house_id;
    private String house_name;   // 房间名称

    public HouseEvent(String houseId, String houseName) {
        this.house_id = houseId;
        this.house_name=houseName;
    }
    public HouseEvent(String houseId, String houseName, String subId, String subName) {
        this.house_id = houseId;
        this.house_name=houseName;
        this.sub_id=subId;
        this.sub_name=subName;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }
}

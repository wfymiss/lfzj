package com.ovov.lfzj.base.bean;

/**
 * 本地存储用户房间信息
 * Created by Administrator on 2017/11/15.
 */

public class UserHouseInfo {
    private int id;
    private String housetabname;  // 用户信息表名
    private String sub_id;  // 小区id
    private String sub_name;   // 小区名称
    private String user_name;  // 用户姓名
    private String user_phone;   // 用户电话
    private String building;   // 楼号
    private String unit;  // 单元号
    private String floor; // 楼层
    private String room;  // 房间
    private String token;
    private String role_id;  // 是否认证判断
    private String id_one;   //  房间id
    private String id_two;

    public UserHouseInfo() {
    }

    // 普通用户
    public UserHouseInfo(String housetabname, String role_id, String token){
        this.housetabname=housetabname;
        this.role_id=role_id;
        this.token=token;
    }

    // 认证或已认证用户
    public UserHouseInfo(String housetabname, String role_id, String sub_id, String sub_name, String room_id, String building,
                         String unit, String room){
        this.housetabname=housetabname;
        this.role_id=role_id;
        this.sub_id=sub_id;
        this.sub_name=sub_name;
        this.id_one=room_id;
        this.building=building;
        this.unit=unit;
        this.room=room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHousetabname() {
        return housetabname;
    }

    public void setHousetabname(String housetabname) {
        this.housetabname = housetabname;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getId_one() {
        return id_one;
    }

    public void setId_one(String id_one) {
        this.id_one = id_one;
    }

    public String getId_two() {
        return id_two;
    }

    public void setId_two(String id_two) {
        this.id_two = id_two;
    }
}

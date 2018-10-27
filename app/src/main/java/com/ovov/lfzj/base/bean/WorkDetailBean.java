package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/10/24.
 */

public class WorkDetailBean {

    /**
     * id : 1
     * order_number : GD2018102609173175176
     * houseId : 0
     * subdistrictId : 7
     * user_id : 5176
     * username : 原磊
     * repair : 0
     * phone : 1863616334
     * position : 0
     * address : 13-1-103
     * category : 2
     * repair_time : 2018-10-26 09:17:31
     * content : 内容体检
     * repair_img : ["http://catel-link.oss-cn-beijing.aliyuncs.com/work/5176_bt.jpg","http://catel-link.oss-cn-beijing.aliyuncs.com/work/5176_xz.jpg"]
     * repair_video : 0
     * receipt_staff : 0
     * household : 0
     * status : 0
     * status_wx : 1
     * status_pd : 2
     * status_jd : 1
     * category_name : 电
     * position_str : 家庭区域
     * worker_user : {"name":"沈叶","mobile":"18635576558","nickname":"沈叶"}
     * worker_offer : {}
     */

    public int id;
    public String order_number;
    public int houseId;
    public int subdistrictId;
    public int user_id;
    public String username;
    public String repair;
    public String phone;
    public String position;
    public String address;
    public int category;
    public String repair_time;
    public String content;
    public String repair_video;
    public int receipt_staff;
    public int household;
    public int status;
    public int status_wx;
    public int status_pd;
    public int status_jd;
    public String category_name;
    public String position_str;
    public WorkerUserBean worker_user;
    public WorkerOfferBean worker_offer;
    public WorkEvaluateBean work_evaluate;
    public WorkRevokeBean work_revoke;
    public List<String> repair_img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public int getSubdistrictId() {
        return subdistrictId;
    }

    public void setSubdistrictId(int subdistrictId) {
        this.subdistrictId = subdistrictId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getRepair_time() {
        return repair_time;
    }

    public void setRepair_time(String repair_time) {
        this.repair_time = repair_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRepair_video() {
        return repair_video;
    }

    public void setRepair_video(String repair_video) {
        this.repair_video = repair_video;
    }

    public int getReceipt_staff() {
        return receipt_staff;
    }

    public void setReceipt_staff(int receipt_staff) {
        this.receipt_staff = receipt_staff;
    }

    public int getHousehold() {
        return household;
    }

    public void setHousehold(int household) {
        this.household = household;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus_wx() {
        return status_wx;
    }

    public void setStatus_wx(int status_wx) {
        this.status_wx = status_wx;
    }

    public int getStatus_pd() {
        return status_pd;
    }

    public void setStatus_pd(int status_pd) {
        this.status_pd = status_pd;
    }

    public int getStatus_jd() {
        return status_jd;
    }

    public void setStatus_jd(int status_jd) {
        this.status_jd = status_jd;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPosition_str() {
        return position_str;
    }

    public void setPosition_str(String position_str) {
        this.position_str = position_str;
    }

    public WorkerUserBean getWorker_user() {
        return worker_user;
    }

    public void setWorker_user(WorkerUserBean worker_user) {
        this.worker_user = worker_user;
    }

    public WorkerOfferBean getWorker_offer() {
        return worker_offer;
    }

    public void setWorker_offer(WorkerOfferBean worker_offer) {
        this.worker_offer = worker_offer;
    }

    public List<String> getRepair_img() {
        return repair_img;
    }

    public void setRepair_img(List<String> repair_img) {
        this.repair_img = repair_img;
    }

    public static class WorkerUserBean {
        public String name;
        public String mobile;
    }

    public static class WorkerOfferBean {
        public String material_cost;
        public String failure_briefing;
    }

    public static class WorkEvaluateBean{
        public String evaluation_content;
        public String door_speed;
        public String service_attitude;
        public String repair_technology;
    }
    public static class WorkRevokeBean{
        public String content;
        public String remarks;
    }

}

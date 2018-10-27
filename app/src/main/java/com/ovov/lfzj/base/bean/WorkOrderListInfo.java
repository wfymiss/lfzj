package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * 工单列表 实体类
 */

public class WorkOrderListInfo {

    /**
     * id : 2
     * order_number : GD20181022
     * houseId : 0
     * subdistrictId : 7
     * user_id : 5176
     * username : 原磊
     * repair : 0
     * phone : 1863616337
     * position : 0
     * address : 13-1-103
     * category : 1
     * repair_time : 2018-10-22 07:07:02
     * content : 6666
     * repair_img : http://catel-link.oss-cn-beijing.aliyuncs.com/work/5176_ds.jpg
     * repair_video : 0
     * receipt_staff : 0
     * household : 0
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
    public List<String> repair_img;
    public String repair_video;
    public int receipt_staff;
    public int household;
    public String status_id;
    public int status;
    public int status_wx;
    public int status_pd;
    public int status_jd;
    public WorkDetailBean.WorkEvaluateBean work_evaluate;

    public static class WorkEvaluateBean{
        public String evaluation_content;
        public String door_speed;
        public String service_attitude;
        public String repair_technology;
    }

}

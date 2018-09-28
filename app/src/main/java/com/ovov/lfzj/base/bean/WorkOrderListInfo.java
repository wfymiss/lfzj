package com.ovov.lfzj.base.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 工单列表 实体类
 * Created by 刘永毅 on 2017/9/3.
 */

public class WorkOrderListInfo {
    /**
     * data : [{"id":"54","odd_numbers":"GD0001100010801201711060001","repair":"移动端","owner":"3086","house_id":"3088","content":"d经过反复发的对的风格还包括 v哦可观测打扮打扮打扮更不准备干嘛不发那条黑色vs 荒郊野外广告烦你无关啊","desc_img":["http://image.catel-link.com/image/59ffb632bf3a9.jpg","http://image.catel-link.com/image/59ffb63ce08e7.jpg"],"position":"","addr":"对的风格","category":"电","repair_time":"2017/11/06 09:09","make_time":"11/06 09:08:00","make_tel":"18635576558","make_person":"ccvcq","wo_type":"3","dispatch_time":"2017/11/06","dispatch_department":"10","dispatch_personnel":"48","multi-media":null,"flow":"3","household":"0","pay_status":"0","charge_time":"2017-11-06 09:09:18","go_home_time":"0000-00-00 00:00:00","wook_satisfied":null,"remark":null,"finish_at":"0000-00-00 00:00:00","res":[{"department_name":"维修部","staff_name":"深夜","staff_tel":"18635576558","remarks":"","complete_time":"0000-00-00 00:00:00"}],"appraise":[{"wook_service_quality":"4","other_suggest":"是中小学校的世世代代的阿萨德的风格 vv 下","created_at":"2017-11-13 15:08:23"}],"condition":[{"completion":"0000-00-00 00:00:00","make_time":"2017-11-06 09:08:00","remarks":"","material":"","service":"0","material_science":"0","household":"0","go_home_time":"0000-00-00 00:00:00","complete_time":"0000-00-00 00:00:00","work_note":""}]}]
     * status : 0
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 54
         * odd_numbers : GD0001100010801201711060001
         * repair : 移动端
         * owner : 3086
         * house_id : 3088
         * content : d经过反复发的对的风格还包括 v哦可观测打扮打扮打扮更不准备干嘛不发那条黑色vs 荒郊野外广告烦你无关啊
         * desc_img : ["http://image.catel-link.com/image/59ffb632bf3a9.jpg","http://image.catel-link.com/image/59ffb63ce08e7.jpg"]
         * position :
         * addr : 对的风格
         * category : 电
         * repair_time : 2017/11/06 09:09
         * make_time : 11/06 09:08:00
         * make_tel : 18635576558
         * make_person : ccvcq
         * wo_type : 3
         * dispatch_time : 2017/11/06
         * dispatch_department : 10
         * dispatch_personnel : 48
         * multi-media : null
         * flow : 3
         * household : 0
         * pay_status : 0
         * charge_time : 2017-11-06 09:09:18
         * go_home_time : 0000-00-00 00:00:00
         * wook_satisfied : null
         * remark : null
         * finish_at : 0000-00-00 00:00:00
         * res : [{"department_name":"维修部","staff_name":"深夜","staff_tel":"18635576558","remarks":"","complete_time":"0000-00-00 00:00:00"}]
         * appraise : [{"wook_service_quality":"4","other_suggest":"是中小学校的世世代代的阿萨德的风格 vv 下","created_at":"2017-11-13 15:08:23"}]
         * condition : [{"completion":"0000-00-00 00:00:00","make_time":"2017-11-06 09:08:00","remarks":"","material":"","service":"0","material_science":"0","household":"0","go_home_time":"0000-00-00 00:00:00","complete_time":"0000-00-00 00:00:00","work_note":""}]
         */

        private String id;
        private String odd_numbers;
        private String repair;
        private String owner;
        private String house_id;
        private String content;
        private String position;
        private String addr;
        private String category;
        private String repair_time;
        private String make_time;
        private String make_tel;
        private String make_person;
        private String wo_type;
        private String dispatch_time;
        private String dispatch_department;
        private String dispatch_personnel;
        @SerializedName("multi-media")
        private String multimedia;
        private String flow;
        private String household;
        private String pay_status;
        private String charge_time;
        private String go_home_time;
        private String wook_satisfied;
        private String remark;
        private String finish_at;
        private String reconciliation;
        private String reconciliation_time;
        private String head_img;
        private List<String> desc_img;
        private List<ResBean> res;
        private List<FirstSendBean> first_send;
        private List<SencondSendBean> sencond_send;
        private List<AppraiseBean> appraise;
        private List<ConditionBean> condition;

        public List<FirstSendBean> getFirst_send() {
            return first_send;
        }

        public void setFirst_send(List<FirstSendBean> first_send) {
            this.first_send = first_send;
        }

        public List<SencondSendBean> getSencond_send() {
            return sencond_send;
        }

        public void setSencond_send(List<SencondSendBean> sencond_send) {
            this.sencond_send = sencond_send;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOdd_numbers() {
            return odd_numbers;
        }

        public void setOdd_numbers(String odd_numbers) {
            this.odd_numbers = odd_numbers;
        }

        public String getRepair() {
            return repair;
        }

        public void setRepair(String repair) {
            this.repair = repair;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getHouse_id() {
            return house_id;
        }

        public void setHouse_id(String house_id) {
            this.house_id = house_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRepair_time() {
            return repair_time;
        }

        public void setRepair_time(String repair_time) {
            this.repair_time = repair_time;
        }

        public String getMake_time() {
            return make_time;
        }

        public void setMake_time(String make_time) {
            this.make_time = make_time;
        }

        public String getMake_tel() {
            return make_tel;
        }

        public void setMake_tel(String make_tel) {
            this.make_tel = make_tel;
        }

        public String getMake_person() {
            return make_person;
        }

        public void setMake_person(String make_person) {
            this.make_person = make_person;
        }

        public String getWo_type() {
            return wo_type;
        }

        public void setWo_type(String wo_type) {
            this.wo_type = wo_type;
        }

        public String getDispatch_time() {
            return dispatch_time;
        }

        public void setDispatch_time(String dispatch_time) {
            this.dispatch_time = dispatch_time;
        }

        public String getDispatch_department() {
            return dispatch_department;
        }

        public void setDispatch_department(String dispatch_department) {
            this.dispatch_department = dispatch_department;
        }

        public String getDispatch_personnel() {
            return dispatch_personnel;
        }

        public void setDispatch_personnel(String dispatch_personnel) {
            this.dispatch_personnel = dispatch_personnel;
        }

        public String getMultimedia() {
            return multimedia;
        }

        public void setMultimedia(String multimedia) {
            this.multimedia = multimedia;
        }

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }

        public String getHousehold() {
            return household;
        }

        public void setHousehold(String household) {
            this.household = household;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getCharge_time() {
            return charge_time;
        }

        public void setCharge_time(String charge_time) {
            this.charge_time = charge_time;
        }

        public String getGo_home_time() {
            return go_home_time;
        }

        public void setGo_home_time(String go_home_time) {
            this.go_home_time = go_home_time;
        }

        public String getWook_satisfied() {
            return wook_satisfied;
        }

        public void setWook_satisfied(String wook_satisfied) {
            this.wook_satisfied = wook_satisfied;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getFinish_at() {
            return finish_at;
        }

        public void setFinish_at(String finish_at) {
            this.finish_at = finish_at;
        }

        public String getReconciliation() {
            return reconciliation;
        }

        public void setReconciliation(String reconciliation) {
            this.reconciliation = reconciliation;
        }

        public String getReconciliation_time() {
            return reconciliation_time;
        }

        public void setReconciliation_time(String reconciliation_time) {
            this.reconciliation_time = reconciliation_time;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public List<String> getDesc_img() {
            return desc_img;
        }

        public void setDesc_img(List<String> desc_img) {
            this.desc_img = desc_img;
        }

        public List<ResBean> getRes() {
            return res;
        }

        public void setRes(List<ResBean> res) {
            this.res = res;
        }

        public List<AppraiseBean> getAppraise() {
            return appraise;
        }

        public void setAppraise(List<AppraiseBean> appraise) {
            this.appraise = appraise;
        }

        public List<ConditionBean> getCondition() {
            return condition;
        }

        public void setCondition(List<ConditionBean> condition) {
            this.condition = condition;
        }

        public static class ResBean implements Serializable {
            /**
             * department_name : 维修部
             * staff_name : 深夜
             * staff_tel : 18635576558
             * remarks :
             * complete_time : 0000-00-00 00:00:00
             */

            private String department_name;
            private String staff_name;
            private String staff_tel;
            private String remarks;
            private String complete_time;

            public String getDepartment_name() {
                return department_name;
            }

            public void setDepartment_name(String department_name) {
                this.department_name = department_name;
            }

            public String getStaff_name() {
                return staff_name;
            }

            public void setStaff_name(String staff_name) {
                this.staff_name = staff_name;
            }

            public String getStaff_tel() {
                return staff_tel;
            }

            public void setStaff_tel(String staff_tel) {
                this.staff_tel = staff_tel;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public String getComplete_time() {
                return complete_time;
            }

            public void setComplete_time(String complete_time) {
                this.complete_time = complete_time;
            }
        }

        public static class AppraiseBean implements Serializable {
            /**
             * wook_service_quality : 4
             * other_suggest : 是中小学校的世世代代的阿萨德的风格 vv 下
             * created_at : 2017-11-13 15:08:23
             */

            private String wook_service_quality;
            private String other_suggest;
            private String created_at;

            public String getWook_service_quality() {
                return wook_service_quality;
            }

            public void setWook_service_quality(String wook_service_quality) {
                this.wook_service_quality = wook_service_quality;
            }

            public String getOther_suggest() {
                return other_suggest;
            }

            public void setOther_suggest(String other_suggest) {
                this.other_suggest = other_suggest;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }
        }

        public static class ConditionBean implements Serializable {
            /**
             * completion : 0000-00-00 00:00:00
             * make_time : 2017-11-06 09:08:00
             * remarks :
             * material :
             * service : 0
             * material_science : 0
             * household : 0
             * go_home_time : 0000-00-00 00:00:00
             * complete_time : 0000-00-00 00:00:00
             * work_note :
             */

            private String completion;
            private String make_time;
            private String remarks;
            private String material;
            private String service;
            private String material_science;
            private String household;
            private String go_home_time;
            private String complete_time;
            private String work_note;

            public String getCompletion() {
                return completion;
            }

            public void setCompletion(String completion) {
                this.completion = completion;
            }

            public String getMake_time() {
                return make_time;
            }

            public void setMake_time(String make_time) {
                this.make_time = make_time;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public String getMaterial() {
                return material;
            }

            public void setMaterial(String material) {
                this.material = material;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String getMaterial_science() {
                return material_science;
            }

            public void setMaterial_science(String material_science) {
                this.material_science = material_science;
            }

            public String getHousehold() {
                return household;
            }

            public void setHousehold(String household) {
                this.household = household;
            }

            public String getGo_home_time() {
                return go_home_time;
            }

            public void setGo_home_time(String go_home_time) {
                this.go_home_time = go_home_time;
            }

            public String getComplete_time() {
                return complete_time;
            }

            public void setComplete_time(String complete_time) {
                this.complete_time = complete_time;
            }

            public String getWork_note() {
                return work_note;
            }

            public void setWork_note(String work_note) {
                this.work_note = work_note;
            }
        }
    }
    public static class FirstSendBean implements Serializable {
        /**
         * department_name : 客服部
         * staff_name : 贺姐姐
         * staff_tel : 18035177600
         * position : 前台接待
         * time : 1530171152
         * complete_time : 2018/06/28 15:32:32
         */

        private String department_name;
        private String staff_name;
        private String staff_tel;
        private String position;
        private String time;
        private String complete_time;

        public String getDepartment_name() {
            return department_name;
        }

        public void setDepartment_name(String department_name) {
            this.department_name = department_name;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }

        public String getStaff_tel() {
            return staff_tel;
        }

        public void setStaff_tel(String staff_tel) {
            this.staff_tel = staff_tel;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }
    }

    public static class SencondSendBean implements Serializable {
        /**
         * department_name : 维修部
         * staff_name : 李俊
         * staff_tel : 13513678729
         * position : 维修班长
         * time : 1530171152
         * complete_time : 2018/06/28 15:32:32
         */

        private String department_name;
        private String staff_name;
        private String staff_tel;
        private String position;
        private String time;
        private String complete_time;

        public String getDepartment_name() {
            return department_name;
        }

        public void setDepartment_name(String department_name) {
            this.department_name = department_name;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }

        public String getStaff_tel() {
            return staff_tel;
        }

        public void setStaff_tel(String staff_tel) {
            this.staff_tel = staff_tel;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }
    }

}

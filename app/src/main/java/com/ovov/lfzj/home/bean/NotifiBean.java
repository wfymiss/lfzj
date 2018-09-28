package com.ovov.lfzj.home.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class NotifiBean {
    private List<noticeBean> datas;
    private String code;

    public String getCode() {
        return code;
    }

    public List<noticeBean> getData() {
        return datas;
    }

    public class noticeBean {


        private String id;
        private String type_name;
        private String notice_number;
        private String title;
        private String content;
        private String pic;
        private String notice_time;
        private String department_name;
        private String staff_name;
        private String staff_phone;

        public String getId() {
            return id;
        }

        public String getType_name() {
            return type_name;
        }

        public String getNotice_number() {
            return notice_number;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getPic() {
            return pic;
        }

        public String getNotice_time() {
            return notice_time;
        }

        public String getDepartment_name() {
            return department_name;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public String getStaff_phone() {
            return staff_phone;
        }

    }


}

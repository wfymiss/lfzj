package com.ovov.lfzj.home.bean;

import com.ovov.lfzj.base.bean.TranspondInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class SubListBean {
    private String code;
    private SubdataBean datas;

    public String getCode() {
        return code;
    }

    public SubdataBean getDatas() {
        return datas;
    }

    public class SubdataBean {
        public UserInfoBean getUser() {
            return user;
        }

        public List<SubdistrictsBean> getSubdistricts() {
            return subdistricts;
        }

        private UserInfoBean user;
        private List<SubdistrictsBean> subdistricts;
        private AppPermission app_permission;
        public AdminRolesBean admin_roles;

        public AdminRolesBean getAdmin_roles() {
            return admin_roles;
        }

        public AppPermission getApp_permission() {
            return app_permission;
        }

        public class UserInfoBean implements Serializable {
            public String id;
            public String username;
            public String email;
            public String password;
            public String salt;
            public String ec_salt;
            public String nickname;
            public String sex;
            public String mobile;
            public String last_login;
            public String last_ip;
            public String address_id;
            public String status;
            public String user_type;
            public String id_card;
            public String old_user_id;
            public String old_subdistrict_id;
            public String created_at;
            public String updated_at;
            public String user_logo;
            public String birthday;
            public String signature;

        }
        public class AppPermission implements Serializable{
            public boolean gongdan_liebiao;
            public boolean gongdan_xiangqing;
            public boolean gongdan_jiedan;
            public boolean gongdan_quxiaodan;
            public boolean gongdan_baojia;
            public boolean gongdan_weixiugongliebiao;
            public boolean gongdan_paidan;
            public boolean gongdan_quxiaopaidan;
            public boolean gongdan_gaipai;
            public boolean jiaofei_chaxun;
        }
        public class AdminRolesBean implements Serializable{
            public String id;
            public String name;
            public String display_name;
            public String description;
            public String created_at;
            public String updated_at;
            public String type;
            public String pivot_user_id;
            public String pivot_role_id;
            public String subdistrict_id;
        }
    }



}
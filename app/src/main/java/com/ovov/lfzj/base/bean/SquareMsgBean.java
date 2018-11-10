package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/11/10.
 */

public class SquareMsgBean {

    public String id;
    public String message;
    public String time;
    public UserInfoBean userInfo;
    public InfoBean info;

    public class UserInfoBean{
        public String nickname;
        public String user_logo;
    }
    public class InfoBean{
        public String id;
        public String comment;
        public List<String> img;
    }
}

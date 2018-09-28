package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/19.
 */

public class TranspondInfo {

    /**
     * id : 279
     * user_id : 3588
     * comment : 说说呗
     * time : 2018-09-19 03:23
     * forward_id : 0
     * isfile : 1
     * userInfo : {"name":"王力军","user_logo":"http://catel-link.oss-cn-beijing.aliyuncs.com/comment/1_mq.jpg"}
     * imgUrl : []
     */

    public String id;
    public String user_id;
    public String comment;
    public String time;
    public int forward_id;
    public int isfile;
    public UserInfoBean userInfo;
    public List<String> imgUrl;

    public static class UserInfoBean {
        /**
         * name : 王力军
         * user_logo : http://catel-link.oss-cn-beijing.aliyuncs.com/comment/1_mq.jpg
         */

        public String nickname;
        public String user_logo;

    }
}

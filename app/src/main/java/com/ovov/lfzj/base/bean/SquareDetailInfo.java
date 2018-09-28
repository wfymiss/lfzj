package com.ovov.lfzj.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kaite on 2018/9/18.
 */

public class SquareDetailInfo implements Serializable {


    /**
     * id : 261
     * user_id : 11
     * comment : 什么
     * zan : 0
     * time : 2018-09-17 12:59
     * forward_id : null
     * isfile : 1
     * replyNum : 0
     * zanNum : 1
     * imgUrl : ["http://catel-link.oss-cn-beijing.aliyuncs.com/comment/11_qy.jpg"]
     * userInfo : {"name":null,"user_logo":null}
     * isZan : 1
     * reply : []
     * fabulous : [{"id":10,"comment_id":261,"user_id":11,"time":"2018-09-17 13:00","userInfo":{"name":null,"user_logo":null}}]
     * forward : []
     */

    public String id;
    public String user_id;
    public String comment;
    public int zan;
    public String time;
    public int forward_id;
    public int isfile;
    public int replyNum;
    public int zanNum;
    public UserInfoBean userInfo;
    public String forwardNum;
    public int isZan;
    public List<String> imgUrl;
    public List<ReplyBean> reply;
    public List<FabulousBean> fabulous;
    public List<ForwardBean> forward;
    public TranspondInfo transpondInfo;


    public static class UserInfoBean implements Serializable{
        /**
         * name : null
         * user_logo : null
         */

        public String nickname;
        public String user_logo;


    }

    public static class FabulousBean implements Serializable{
        /**
         * id : 10
         * comment_id : 261
         * user_id : 11
         * time : 2018-09-17 13:00
         * userInfo : {"name":null,"user_logo":null}
         */

        public int id;
        public String comment_id;
        public String user_id;
        public String time;
        public UserInfoBeanX userInfo;


        public static class UserInfoBeanX implements Serializable{
            /**
             * name : null
             * user_logo : null
             */

            public String nickname;
            public String user_logo;

        }
    }

    public static class ForwardBean implements Serializable{
        /**
         * id : 10
         * comment_id : 261
         * user_id : 11
         * time : 2018-09-17 13:00
         * userInfo : {"name":null,"user_logo":null}
         */

        public int id;
        public String comment_id;
        public String user_id;
        public String time;
        public String comment;
        public UserInfoBeanX userInfo;


        public static class UserInfoBeanX implements Serializable{
            /**
             * name : null
             * user_logo : null
             */

            public String nickname;
            public String user_logo;

        }
    }

    public static class ReplyBean implements Serializable{

        public int id;
        public String user_id;
        public String comment_id;
        public String content;
        public String time;
        public UserInfoBeanX userInfo;

        public static class UserInfoBeanX implements Serializable{
            /**
             * name : null
             * user_logo : null
             */

            public String nickname;
            public String user_logo;

        }
    }
}


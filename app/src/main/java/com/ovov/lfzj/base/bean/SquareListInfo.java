package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/14.
 */

public class SquareListInfo {


    /**
     * id : 124
     * user_id : 2
     * comment : "我发布的"
     * zan : 0
     * time : 1536908916
     * forward_id : null
     * isfile : 1
     * replyNum : 0
     * zanNum : 0
     * userInfo : {"name":"","user_logo":null}
     * imgUrl : []
     * forwardNum : 0
     * forwardInfo : {"id":121,"user_id":3,"comment":"转发测试","zan":0,"time":1536908894,"forward_id":98,"isfile":0,"userInfo":{"name":null,"user_logo":null}}
     */

    public String id;
    public String user_id;
    public String comment;
    public String zan;
    public String time;
    public String forward_id;
    public String isfile;
    public String replyNum;
    public int zanNum;
    public UserInfoBean userInfo;
    public String forwardNum;
    public ForwardInfoBean forwardInfo;
    public List<String> imgUrl;
    public int isZan;
    public TranspondInfo transpondInfo;

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    private boolean isSelect=false;     // 账单是否被选中

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public void setIszan(int iszan) {
        this.isZan = iszan;
    }

    public boolean getSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public static class UserInfoBean {
        /**
         * name :
         * user_logo : null
         */

        public String nickname;
        public String user_logo;


    }

    public static class ForwardInfoBean {
        /**
         * id : 121
         * user_id : 3
         * comment : 转发测试
         * zan : 0
         * time : 1536908894
         * forward_id : 98
         * isfile : 0
         * userInfo : {"name":null,"user_logo":null}
         */

        public String id;
        public String user_id;
        public String comment;
        public String zan;
        public String time;
        public String forward_id;
        public String isfile;
        public UserInfoBeanX userInfo;
        public static class UserInfoBeanX {
            /**
             * name : null
             * user_logo : null
             */

            public String nickname;
            public String user_logo;

        }
    }

}

package com.ovov.lfzj.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kaite on 2018/9/14.
 */

public class SquareListInfo implements Serializable {


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
    public String location_id;
    public String created_at;
    private String uid;
    private String nickname;
    private String user_logo;

    public String getNickname() {
        return nickname;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public String getUid() {
        return uid;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String brief;

    public String getBrief() {
        return brief;
    }

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
    public ReplyBean reply;
    /**
     * uid : 5174
     * question : 反馈反馈反馈反馈
     * answer :
     * reply_time :
     */

    private String question;
    private String answer;
    private String reply_time;

    public String getId() {
        return id;
    }

    public List<String> imgUrl;
    public int isZan;
    public TranspondInfo transpondInfo;
    /**
     * title : 乐福智慧社区
     * info : 乐福智慧社区乐福智慧社区乐福智慧社区乐福智慧社区乐福智慧社区乐福智慧社区
     * str : Q1
     */

    private String title;
    private String info;
    private String str;

    public String getTime() {
        return time;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    private boolean isSelect = false;     // 账单是否被选中

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getReply_time() {
        return reply_time;
    }

    public static class UserInfoBean implements Serializable{
        /**
         * name :
         * user_logo : null
         */

        public String nickname;
        public String user_logo;
        public String signature;

    }

    public static class ForwardInfoBean implements Serializable{
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

        public static class UserInfoBeanX implements Serializable{
            /**
             * name : null
             * user_logo : null
             */

            public String nickname;
            public String user_logo;
            public String signature;

        }
    }

    public static class ReplyBean implements Serializable{
        public String id;
        public String reply_id;
        public String user_id;
        public String comment_id;
        public String content;
        public String time;
        public String nickname;
        public String mobile;
    }

}

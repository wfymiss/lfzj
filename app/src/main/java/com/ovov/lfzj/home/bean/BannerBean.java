package com.ovov.lfzj.home.bean;

/**
 * Created by kaite on 2018/9/20.
 */

public class BannerBean {

    private String img;
    private String code;
    private String datas;
    /**
     * id : 1
     * title : 论坛消息
     * message :
     * time :
     */

    private String id;
    private String title;
    private String message;
    private String time;

    public String getCode() {
        return code;
    }

    public String getDatas() {
        return datas;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

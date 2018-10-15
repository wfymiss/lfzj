package com.ovov.lfzj.user.bean;

/**
 * Created by kaite on 2018/10/9.
 */

public class HealthBean {
    String id;
    String time;
    String checkbox;
    String setSeclct;

    public String getCheckbox() {
        return checkbox;
    }


    public String getId() {
        return id;
    }

    public String getSetSeclct() {
        return setSeclct;
    }

    public void setSetSeclct(String setSeclct) {

        this.setSeclct = setSeclct;
    }

    public String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/5/4.
 */

public class RevieverEvent {
    private String type;

    public RevieverEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/5/4.
 */

public class RevieverEvent {
    private String type;
    private String id;

    public RevieverEvent(String type, String id) {

        this.id = id;
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

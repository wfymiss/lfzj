package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/19.
 */

public class SubselectEvent {
    private String sub_id;
    private String sub_name;

    public SubselectEvent(String sub_id, String sub_name) {

        this.sub_id = sub_id;
        this.sub_name = sub_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }


}

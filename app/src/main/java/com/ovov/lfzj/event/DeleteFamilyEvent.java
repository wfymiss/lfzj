package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/11/7.
 */

public class DeleteFamilyEvent {
    public String user;
    public String house;
    public String relative_id;
    public int pos;

    public DeleteFamilyEvent(String user, String house, String relative_id,int pos) {
        this.user = user;
        this.house = house;
        this.relative_id = relative_id;
        this.pos = pos;
    }
}

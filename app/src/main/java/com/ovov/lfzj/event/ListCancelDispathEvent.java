package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/26.
 */

public class ListCancelDispathEvent {
    public String remarks;
    public int posistion;

    public ListCancelDispathEvent(String remarks, int posistion) {
        this.remarks = remarks;
        this.posistion = posistion;
    }
}

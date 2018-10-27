package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/26.
 */

public class ListWorkerCancelEvent {
    public String remarks;
    public String reason;
    public int posistion;

    public ListWorkerCancelEvent(String remarks, String reason, int posistion) {
        this.remarks = remarks;
        this.reason = reason;
        this.posistion = posistion;
    }
}

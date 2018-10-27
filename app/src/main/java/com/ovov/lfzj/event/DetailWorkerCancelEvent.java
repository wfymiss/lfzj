package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/26.
 */

public class DetailWorkerCancelEvent {
    public String remarks;
    public String reason;

    public DetailWorkerCancelEvent(String remarks, String reason) {
        this.remarks = remarks;
        this.reason = reason;
    }
}

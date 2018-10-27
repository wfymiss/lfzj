package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/27.
 */

public class OwnerCancelEvent {
    public String reason;
    public String remarks;

    public OwnerCancelEvent(String reason, String remarks) {
        this.reason = reason;
        this.remarks = remarks;
    }
}

package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/9/20.
 */

public class PayResultEvent {
    public String order_id;
    public String type;

    public PayResultEvent(String order_id, String type) {
        this.order_id = order_id;
        this.type = type;
    }
}

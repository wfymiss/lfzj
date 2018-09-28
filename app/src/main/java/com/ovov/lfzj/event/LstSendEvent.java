package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/9/18.
 */

public class LstSendEvent {
    public String content;
    public int i;

    public LstSendEvent(String content, int i) {
        this.content = content;
        this.i = i;
    }
}

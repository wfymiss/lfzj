package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/12.
 */

public class DeleteImageEvent {
    public  int posistion;

    public DeleteImageEvent(int posistion) {
        this.posistion = posistion;
    }

    public void setPosistion(int posistion) {
        this.posistion = posistion;
    }
}

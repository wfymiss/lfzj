package com.ovov.lfzj.home.event;

/**
 * 传值监听
 * Created by lyy on 2018/4/25.
 */

public class PassValueEvent {
    private String id;
    private float price;
    private boolean isSelect;
    private boolean status;
    private String value_one;
    private String value_two;

    public PassValueEvent(boolean isSelect){
        this.isSelect=isSelect;
    }
    public PassValueEvent(boolean state, String value){
        this.status=state;
        this.value_one=value;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean getSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getValue_one() {
        return value_one;
    }

    public void setValue_one(String value_one) {
        this.value_one = value_one;
    }

    public String getValue_two() {
        return value_two;
    }

    public void setValue_two(String value_two) {
        this.value_two = value_two;
    }
}

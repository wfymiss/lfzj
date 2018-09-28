package com.ovov.lfzj.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 物业缴费账单
 * Created by Administrator on 2017/9/9.
 */

public class PropertyPaymentInfo implements Serializable {

    /**
     * id : 10
     * name : 宽带费
     * charge_from : 2017-09-01
     * charge_end : 2018-09-01
     * money : 700
     */

    public String id;
    public String name;
    public String room_number;
    public String category_name;
    public String charge_from;
    public String charge_end;
    public double money;
    public String order_number;
    public boolean isSelect = false;     // 账单是否被选中

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

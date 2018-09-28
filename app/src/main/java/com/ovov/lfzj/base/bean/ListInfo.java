package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by jzxiang on 13/01/2018.
 */

public class ListInfo<T> {

    /**
     * list : [{"id":"5","device_name":"ggg","name":"ggg","address":"ggv","type":"0","status":true,"state":false,"created_at":"2018-01-13 16:44:04"}]
     * offset : 0
     * size : 1
     */
    List<T> datas;
    int code;

    public boolean success(){
        return code == 200;
    }
    public int code(){
        return code;
    }

    public List<T> datas(){
        return datas;
    }

}

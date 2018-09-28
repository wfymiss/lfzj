package com.ovov.lfzj.base.bean;

/**
 * Created by jzxiang on 7/6/17.
 */

public class DataInfo<T> {
    T datas;
    int code;

    public boolean success(){
        return code == 200;
    }
    public int code(){
        return code;
    }

    public T datas(){
        return datas;
    }

}

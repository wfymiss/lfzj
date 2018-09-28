package com.ovov.lfzj.base.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/21.
 */

public class ShopListBean<T> {

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

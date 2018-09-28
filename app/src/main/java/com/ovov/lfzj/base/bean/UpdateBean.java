package com.ovov.lfzj.base.bean;

/**
 * Created by kaite on 2018/9/26.
 */

public class UpdateBean {
    public int type;
    public String apk_url;
    public String upgrade_point;

    public boolean needUpdate(){
        if (type == 0)
            return false;
        return true;
    }
}

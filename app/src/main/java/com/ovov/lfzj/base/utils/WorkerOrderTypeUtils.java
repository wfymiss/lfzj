package com.ovov.lfzj.base.utils;

/**
 * Created by kaite on 2018/10/26.
 */

public class WorkerOrderTypeUtils {
    public static String getStatusName(int status,int status_wx,int status_jd,int status_pd){
        if (status == 0 && status_wx == 0 && status_jd == 0 && status_pd == 0){
            return "待派单";
        }
        if (status == 0 && status_wx == 0 && status_jd == 0 && status_pd == 1){
            return "派单中";
        }
        if (status == 0 && status_wx == 0 && status_jd == 1){
            return "维修中";
        }
        if (status == 0 && status_wx == 0 && status_jd == 2){
            return "已拒单";
        }
        if (status == 0 && status_wx == 1){
            return "维修中";
        }
        if (status == 0 && status_wx == 2){
            return "待验收";
        }
        if (status == 1){
            return "已完成";
        }
        return "已取消";

    }

    public static int getStatus(int status,int status_wx,int status_jd,int status_pd){
        if (status == 0 && status_wx == 0 && status_jd == 0 && status_pd == 0){
            return 1;
        }
        if (status == 0 && status_wx == 0 && status_jd == 0 && status_pd == 1){
            return 2;
        }
        if (status == 0 && status_wx == 0 && status_jd == 1){
            return 3;
        }
        if (status == 0 && status_wx == 0 && status_jd == 2){
            return 4;
        }
        if (status == 0 && status_wx == 1){
            return 3;
        }
        if (status == 0 && status_wx == 2){
            return 5;
        }
        if (status == 1){
            return 6;
        }
        return 7;
    }
}

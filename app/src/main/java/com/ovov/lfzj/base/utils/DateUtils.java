package com.ovov.lfzj.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kaite on 2018/9/14.
 */

public class DateUtils {
    /**
     6      * 时间戳转换成日期格式字符串
     7      * @param seconds 精确到秒的字符串
     8      * @param formatStr
     9      * @return
     10      */
     /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}

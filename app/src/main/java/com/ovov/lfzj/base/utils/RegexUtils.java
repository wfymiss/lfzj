package com.ovov.lfzj.base.utils;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具，用来校验账号和密码等是否符合基本规则
 */
public class RegexUtils {

    public static final int VERIFY_SUCCESS = 0;

    private static final int VERIFY_LENGTH_ERROR = 1;

    private static final int VERIFY_TYPE_ERROR = 2;

    /**
     * 长度在6~18之间，只能包含字符、数字和下划线
     *
     * @param password 密码
     * @return {@link #VERIFY_SUCCESS}, {@link #VERIFY_TYPE_ERROR}, {@link #VERIFY_LENGTH_ERROR}
     */
    public static int verifyPassword(@NonNull String password) {

        int length = password.length();
        if (length < 6 || length > 18) {
            return VERIFY_LENGTH_ERROR;
        }

        String regex = "^\\w+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) return VERIFY_TYPE_ERROR;
        return VERIFY_SUCCESS;
    }

    /**
     * 将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**）。
     * 然后再获取长度。
     */
    private static int countLength(@NonNull String string) {
        string = string.replaceAll("[^\\x00-\\xff]", "**");
        return string.length();
    }

    //判断字符串是否为数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    //判断字符串是否为数字
    public static boolean isNoText(String str) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 验证手机号码
     * 用户名只能是11位手机号码
     * 不能出现特殊字符，下划线，以及英文字母
     */
    public static int isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        if (str.length() != 11){
            return VERIFY_LENGTH_ERROR;
        }
        p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        if (!b)return VERIFY_TYPE_ERROR;
        return VERIFY_SUCCESS;
    }
    public static boolean verifyMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        if (!b)return false;
        return true;
    }


}

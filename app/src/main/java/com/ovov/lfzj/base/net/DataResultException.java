package com.ovov.lfzj.base.net;

import com.ovov.lfzj.base.bean.ErrorInfo;

import java.io.IOException;

/**
 * Created by kaite on 2018/5/7.
 */

public class DataResultException extends IOException {
    public String errorInfo;
    public int code;

    public DataResultException( String errorInfo, int code) {
        this.errorInfo = errorInfo;
        this.code = code;
    }

}

package com.ovov.lfzj.http;

import com.google.gson.Gson;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ErrorInfo;
import com.ovov.lfzj.base.net.DataResultException;


import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by kaite on 2018/5/7.
 */

public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody,T> {
    private Gson gson;
    private Type type;

    public MyGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            DataInfo baseBean = gson.fromJson(response,DataInfo.class);
            if (!baseBean.success()) {
                //throw new DataResultException(baseBean.datas(),baseBean.code());
                ErrorInfo dataResultException =  gson.fromJson(response,ErrorInfo.class);
                throw new DataResultException(dataResultException.datas.error,dataResultException.code);
            }
            return gson.fromJson(response,type);
        }finally {
            value.close();
        }
    }
}

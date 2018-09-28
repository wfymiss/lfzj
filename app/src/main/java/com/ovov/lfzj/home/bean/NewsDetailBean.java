package com.ovov.lfzj.home.bean;

/**
 * Created by kaite on 2018/9/22.
 */

public class NewsDetailBean {
    private String code;
    private NewsDetail datas;


    public String getCode() {
        return code;
    }

    public NewsDetail getDatas() {
        return datas;
    }

    public class NewsDetail{

        public String getContent() {
            return content;
        }

        private String content;
    }
}

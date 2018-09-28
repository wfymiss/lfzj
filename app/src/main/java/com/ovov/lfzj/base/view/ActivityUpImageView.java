package com.ovov.lfzj.base.view;


import com.ovov.lfzj.base.bean.ActivityUpImageInfo;

/**
 * 上传图片
 * Created by Administrator on 2017/8/24.
 */

public interface ActivityUpImageView {
    void setImage(ActivityUpImageInfo data);               //  提交图片
    void showMsg(String msg);
}

package com.ovov.lfzj.base.bean;


import android.util.Log;


import com.ovov.lfzj.CatelApplication;
import com.ovov.lfzj.base.utils.SDCardUtils;
import com.ovov.lfzj.home.bean.SubListBean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Nate on 2016年8月16日
 * 用户登录实体类
 */
public class LoginUserBean implements Serializable, Cloneable {
    /**
     * @Fields: serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public final static String TAG = "LoginUserBean";
    public final static String BASE_CACHE_PATH = CatelApplication.getInstance().getFilesDir().getParent() + "/";//缓存的路径;;

    private static LoginUserBean instance;

    private LoginUserBean() {

    }
    public static LoginUserBean getInstance() {
        if (instance == null) {
            Object object = SDCardUtils.restoreObject(BASE_CACHE_PATH + TAG);
            if (object == null) { // App第一次启动，文件不存在，则新建之
                object = new LoginUserBean();
                SDCardUtils.saveObject(BASE_CACHE_PATH + TAG, object);

            }
            instance = (LoginUserBean) object;
        }
        return instance;
    }


    //用户属性
    private boolean isLogin;
    private String access_token;
    private String subname;
    private int newMsg = 0;

    public int getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(int newMsg) {
        this.newMsg = newMsg;
    }

    private List<SquareDetailInfo.ForwardBean> forwardBean;
    private SubListBean.SubdataBean.AppPermission appPermission;

    public SubListBean.SubdataBean.AppPermission getAppPermission() {
        return appPermission;
    }

    public void setAppPermission(SubListBean.SubdataBean.AppPermission appPermission) {
        this.appPermission = appPermission;
    }

    public List<SquareDetailInfo.ForwardBean> getForwardBean() {
        return forwardBean;
    }

    public void setForwardBean(List<SquareDetailInfo.ForwardBean> forwardBean) {
        this.forwardBean = forwardBean;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubname() {

        return subname;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_id() {

        return sub_id;
    }

    private String userId;
    private boolean is_auth;
    private String phone;
    private String sub_id;
    private SubListBean.SubdataBean.UserInfoBean userInfoBean;
    private String Neighbourid;
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getId() {
        return Neighbourid;
    }

    public void setId(String Neighbourid) {
        this.Neighbourid = Neighbourid;
    }

    public SubListBean.SubdataBean.UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(SubListBean.SubdataBean.UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isIs_auth() {
        return is_auth;
    }

    public void setIs_auth(boolean is_auth) {

        this.is_auth = is_auth;
    }

    public String getUserId() {
        Log.e(TAG, "getUserId: " + userId);
        return userId;
    }

    public void setUserId(String userId) {
        Log.e(TAG, "getUserId: " + userId);
        this.userId = userId;
    }

    public String getAccess_token() {
        Log.e(TAG, "getAccess_token: " + access_token);
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void reset() {
        isLogin = false;
        access_token = "";
        userId = "";
        phone = "";
        is_auth = false;
        userInfoBean = null;
        sub_id = "";
        subname = "";
        Neighbourid = "";
        forwardBean = null;
        loginType = "";
        appPermission = null;
        newMsg = 0;
    }

    public void save() {
        instance = this;
        SDCardUtils.saveObject(BASE_CACHE_PATH + TAG, this);
    }

    // -----------以下3个方法用于序列化-----------------
    public LoginUserBean readResolve() throws ObjectStreamException, CloneNotSupportedException {
        instance = (LoginUserBean) this.clone();
        return instance;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

    public Object Clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

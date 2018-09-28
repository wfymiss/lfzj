package com.ovov.lfzj.base.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 购物车数据库表
 * Created by lyy on 2017/7/22.
 */

public class ShoptoreDataHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;

    public ShoptoreDataHelper(Context context){
        super(context,"lefudata",null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 下标id  商店 标识号id   商店 id  商店名称-name  商品ID  商品名称  商品介绍-简介  商品价格  商品图片路径 商品数量
        db.execSQL("create table goodsData(_id integer primary key autoincrement,storeId varchar(20),shopId int,shopName varchar(100),goodsId int,goodsName varchar(100),goodsjianjie varchar(100),goodsPrice varchar(100),goodsUri varchar(100),goodsCount int)");
        db.execSQL("create table userAddress(_id integer primary key autoincrement,addressId varchar(200),address varchar(200),area varchar(200),phone varchar(200),username varchar(200),status varchar(200))");
        db.execSQL("create table userInfo(_id integer primary key autoincrement,tabName varchar(100),subId varchar(100),subName varchar(200),userName varchar(200),userPhone varchar(200),building varchar(100),unit varchar(100),floor varchar(100)," +
                "room varchar(100),token varchar(600),roleId varchar(100),uid varchar(100),loginWord varchar(200),birthday varchar(200),sex varchar(100),headImage varchar(200)," +
                "nickname varchar(100),signature varchar(200),payWord varchar(200))");
        db.execSQL("create table userhouse(_id integer primary key autoincrement,tabName varchar(50),sub_id varchar(20),sub_name varchar(200),user_name varchar(50)," +
                "user_phone varchar(50),building varchar(20),unit varchar(20),floor varchar(20),room varchar(20),token varchar(500),role_id varchar(20),id_one varchar(20)," +
                "id_two varchar(20))");
    }

    // onUpgrade方法是数据库版本号发生改变时会被调用，适合做表结构的修改
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final int data_verson = db.getVersion();   // 当前数据库版本
        //清加 一个 phone字段
        //db.execSQL("alter table infos add phone varchar(11)");
        if (data_verson ==2){
            switch (oldVersion){
                case 1:
                    if (newVersion <=1){
                        return;
                    }
                    db.beginTransaction();
                    try {
//                        upgradeDatabaseToVersion(db);
                        db.setTransactionSuccessful();
                    } catch (Throwable ex) {
                        break;
                    } finally {
                        db.endTransaction();
                    }
            }
        }
    }

//    private void upgradeDatabaseToVersion(SQLiteDatabase db) {
//        db.execSQL("create table userhouse(_id integer primary key autoincrement,tabName varchar(50),sub_id varchar(20),sub_name varchar(200),user_name varchar(50)," +
//                "user_phone varchar(50),building varchar(20),unit varchar(20),floor varchar(20),room varchar(20),token varchar(500),role_id varchar(20),id_one varchar(20)," +
//                "id_two varchar(20))");
//        Log.i("sql","数据库更新");
//    }
}

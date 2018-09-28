package com.ovov.lfzj.base.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.ovov.lfzj.base.bean.UserHouseInfo;

import java.util.ArrayList;

/**
 * 用户房间 dao 方法
 * Created by Administrator on 2017/11/15.
 */

public class UserHouseDao {
    private ShoptoreDataHelper sql;  // 定义用户信息数据库
    private ArrayList<UserHouseInfo> list;  // 用户房间信息
    private UserHouseInfo info;     // 用户房间信息实体类
    private Context mcontext;    // 上下文参数
//    private SQLiteDatabase database;

    /**
     * 初始化数据库表
     * @param context
     */
    public UserHouseDao(Context context){
        this.mcontext=context;
        sql= new ShoptoreDataHelper(mcontext);
//        database=sql.getReadableDatabase();
//        sql.onUpgrade(database,1,2);
    }

    /**
     * 未认证普通用户向数据库添加用户房间信息
     * @param info
     * @return
     */
    public long addHouseNoIndentify(UserHouseInfo info){
        long i=0;
        //通过按ID条件查询，判断有无重复数据,没有重复数据，执行添加
        UserHouseInfo houseInfo=Select(info.getRole_id());  // 查询role_id 对应的表数据
        //开始读写操作
        SQLiteDatabase db=sql.getReadableDatabase();
        if (houseInfo==null || houseInfo.getRole_id().equals(info.getRole_id())){
            ContentValues values=new ContentValues();
            values.put("tabName",info.getHousetabname());
            values.put("token",info.getToken());
            values.put("role_id",info.getRole_id());
            i= db.insert("userhouse",null,values);       // 向数据库添加数据
        }
        db.close();  // 关闭数据库
        return i;
    }

    /**
     * 业主登录或业主认证向数据库添加用户房间信息
     * @param info
     * @return
     */
    public long addUserHouse(UserHouseInfo info){
        long i=0;
        //通过按ID条件查询，判断有无重复数据,没有重复数据，执行添加
        UserHouseInfo houseInfo=Select(info.getRole_id());  // 查询role_id 对应的表数据
        //开始读写操作
        SQLiteDatabase db=sql.getReadableDatabase();
        if (houseInfo==null || houseInfo.getRole_id().equals(info.getRole_id())){
            ContentValues values=new ContentValues();
            values.put("tabName",info.getHousetabname());
            values.put("token",info.getToken());
            values.put("sub_id",info.getSub_id());
            values.put("sub_name",info.getSub_name());
            values.put("building",info.getBuilding());
            values.put("unit",info.getUnit());
            values.put("room",info.getRoom());
            values.put("role_id",info.getRole_id());
            values.put("id_one",info.getId_one());        // 房间id
            i= db.insert("userhouse",null,values);       // 向数据库添加数据
        }
        db.close();  // 关闭数据库
        return i;
    }


    /**
     * 根据role_id 查看数据
     * @param id
     * @return
     */
    public UserHouseInfo Select(String id){
        SQLiteDatabase db=sql.getWritableDatabase();
        //查询数据，返回cursor 结果集
        if (id!=null){
            Cursor cursor=db.rawQuery("select * from userhouse where role_id = ? ",new String[]{id});
            if (cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()){
                    info=new UserHouseInfo();
                    info.setId(cursor.getInt(0));
                    info.setHousetabname(cursor.getString(1));
                    info.setSub_id(cursor.getString(2));
                    info.setSub_name(cursor.getString(3));
                    info.setUser_name(cursor.getString(4));
                    info.setUser_phone(cursor.getString(5));
                    info.setBuilding(cursor.getString(6));
                    info.setUnit(cursor.getString(7));
                    info.setFloor(cursor.getString(8));
                    info.setRoom(cursor.getString(9));
                    info.setToken(cursor.getString(10));
                    info.setRole_id(cursor.getString(11));
                    info.setId_one(cursor.getString(12));   // 房间id
                    info.setId_two(cursor.getString(13));
                }
            }
            cursor.close();
            return info;
        }
        db.close();
        return null;
    }

    /**
     * 查询所有房间信息
     */
    public ArrayList<UserHouseInfo> SelectHouseAll(){
        list=new ArrayList<>();
        SQLiteDatabase db=sql.getWritableDatabase();
        //查询数据，返回cursor 结果集
        Cursor cursor=db.rawQuery("select * from userhouse",null);
        if (cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                UserHouseInfo infoAll=new UserHouseInfo();
                infoAll.setId(cursor.getInt(0));
                infoAll.setHousetabname(cursor.getString(1));
                infoAll.setSub_id(cursor.getString(2));
                infoAll.setSub_name(cursor.getString(3));
                infoAll.setUser_name(cursor.getString(4));
                infoAll.setUser_phone(cursor.getString(5));
                infoAll.setBuilding(cursor.getString(6));
                infoAll.setUnit(cursor.getString(7));
                infoAll.setFloor(cursor.getString(8));
                infoAll.setRoom(cursor.getString(9));
                infoAll.setToken(cursor.getString(10));
                infoAll.setRole_id(cursor.getString(11));
                infoAll.setId_one(cursor.getString(12));   // 房间id
                infoAll.setId_two(cursor.getString(13));
                list.add(infoAll);
            }
            cursor.close();
            return list;
        }
        db.close();
        return null;
    }

    // 删除用户房间信息
    public void deleteAll(){
        SQLiteDatabase db=sql.getReadableDatabase();
        db.execSQL("delete from userhouse"); //清空数据库
        db.execSQL("update sqlite_sequence set seq=0 where name='userhouse'");   // 主键id 重置，从0开始
        db.close();
    }
}

package com.ovov.lfzj.base.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.LoginOutEvent;
import com.ovov.lfzj.event.NewMsgEvent;
import com.ovov.lfzj.event.Recievertype;
import com.ovov.lfzj.event.RevieverEvent;
import com.ovov.lfzj.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/8/18.
 */

public class JpushReceiver extends BroadcastReceiver {
    private String type = null;    // 通知消息分类

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("dadada", "" + bundle.get(JPushInterface.EXTRA_EXTRA));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            // RxBus.getDefault().post(new LoginOutEvent());
            RxBus.getDefault().post(new NewMsgEvent());
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            String extar = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonExtra = new JSONObject(extar);
                String type = jsonExtra.getString("content_type");
                String id = jsonExtra.getString("id");
                Log.d("dadada", "" + type);
                if (type.equals(Recievertype.CREATE_FEE)) {
                   /* Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.CREATE_FEE, id));

                } else if (type.equals(Recievertype.OWNER_WORK_ORDER)) {
                    /*Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.OWNER_WORK_ORDER, id));
                } else if (type.equals(Recievertype.PROPERTY_WORK_ORDER)) {
                   /* Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.PROPERTY_WORK_ORDER, id));
                } else if (type.equals(Recievertype.PROPERTY_SCHDELUE)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.PROPERTY_SCHDELUE, id));
                } else if (type.equals(Recievertype.WORKERINFO)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.WORKERINFO, id));
                } else if (type.equals(Recievertype.sellerorderlist)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.sellerorderlist, id));
                }  else if (type.equals(Recievertype.DELECTORDER)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.DELECTORDER, id));
                } else if (type.equals(Recievertype.CARPTERDISS)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.CARPTERDISS, id));
                } else if (type.equals(Recievertype.OWNERDISS)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.OWNERDISS, id));
                }else if (type.equals(Recievertype.ORDER_WORK)) {
                    /*Intent i = new Intent(context, LFGJMainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    EventBus.getDefault().post(new RevieverEvent(Recievertype.ORDER_WORK, id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   Log.e("nActionExtra", nActionExtra);
        } else {
            // Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}

package com.ovov.lfzj.opendoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.KeyReplyInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.widget.BuildingPopup;
import com.ovov.lfzj.event.BuildingEvent;
import com.ovov.lfzj.http.api.CatelApiService;
import com.ovov.lfzj.opendoor.adapter.KeyListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 钥匙列表
 */
public class KeyListingActivity extends BaseActivity {
    private Unbinder unbinder;

    @BindView(R.id.key_list_recycler)
    RecyclerView recyclerView;
    private BuildingPopup popup = null;  // 小区楼宇弹出框
    private KeyListAdapter adapter = new KeyListAdapter(this);  // 钥匙列表适配器
    private LinearLayoutManager linearLayoutManager;
    private List<KeyReplyInfo.DataBean> list = new ArrayList<>();
    //    private OpenDoorPresent present;   // 定义更新钥匙列表方法
    private String build_id = null;   // 楼宇ID
    private String key_gaintime = null;  // 获取钥匙系统本地时间
    private String token = null;   // 用户token
    private String sub_id = null;   // 小区id
    //private UserInfo infoData=null;                    // 定义用户信息
    private String role_id = null;                     // 身份判断 未认证0，已认证1，家人2
 //   private String keyPath= "http://app.catel-link.com/v1/entrance/applyKey";       //  更新钥匙列表
    private String keyPath= CatelApiService.HOST+"v1/entrance/applyKey";       // 更新钥匙列表
    private String build_keyPath="https://api.catel-link.com/front/Entrance/property/getKeys";   // 获取选定的楼宇钥匙
    private boolean upKey = false;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            keyListJson(result);        //  解析钥匙列表
        }
    };


    public static void toActivity(Context context){
        Intent intent = new Intent(context,KeyListingActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_key_listing;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_keylist);
        setRightText(R.string.text_update_key);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EventBus.getDefault().register(this);       // 小区楼宇列表点击 popup监听事件
//        present=new OpenDoorPresent(this);    // 初始化更新钥匙方法
        token=LoginUserBean.getInstance().getAccess_token();
        sub_id="1";
     //   storageTokenRead();    //  获取token
//        postKeyList();        // 从本地解析钥匙
        upDataKeyList();      // 更新钥匙列表———保存在本地
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);    //recycler 竖向滑动
        recyclerView.setAdapter(adapter);
    }

//
//    // 获取token
//    private void storageTokenRead() {
//
//        String tabname="usertable";
//        infoData=new UserInfoDao(this).SelectCommenUse(tabname);     // 检索数据库user 信息
//        if (infoData!=null) {
//            role_id = infoData.getRole_id();                        //  用户身份ID
//        }
//        SharedPreferences spf=this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
//        token=spf.getString("token","");
//        sub_id=spf.getString("subId","");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //获取当前时间
//        Date curDate = new Date(System.currentTimeMillis());
//        key_gaintime= formatter.format(curDate);
//        //  如果role_id = 5 （管家登录）
//        if (role_id != null && role_id.equals("5")){
//            key_update.setText("钥匙");
//        }
//    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (role_id != null && role_id.equals("5")) {             //  role_id = 3 管家身份登录——拥有获取各楼宇钥匙权利
                    // 管家获取楼宇钥匙
                    if (popup != null) {         // 如果小区楼宇弹出框存在，关闭弹出框初始化为空
                        popup.dismiss();
                        popup = null;
                    }
                    popup = new BuildingPopup(this, token);
                    // 开启 popup 时界面透明
                    WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                    lp.alpha = 0.7f;
//                if (bgAlpha == 1) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//                } else {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//                }
                    this.getWindow().setAttributes(lp);
                    // popupwindow 第一个参数指定popup 显示页面
                    popup.showAtLocation(this.findViewById(R.id.key_list), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);     // 第一个参数popup显示activity页面
                    popup.update();
                    // popup 退出时界面恢复
                    popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            getWindow().setAttributes(lp);
                            popup.dismiss();
                        }
                    });
                } else {
//                    present.getOpenReplyKey(user_token);
                    upDataKeyList();   // 更新钥匙列表———保存在本地
                    upKey = true;    // 更新钥匙upKey = true  标记
                }
                break;
        }
    }

//    // 获取本地钥匙
//    private void postKeyList() {
//        SharedPreferences spf = this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
//        String keyjson = spf.getString("keyjson", "");
//        keyListJson(keyjson);     // 解析钥匙
//    }

    //   解析钥匙key
    private void keyListJson(String keyjson) {
        if (list != null) {
            list.clear();
            list = new ArrayList<>();
        }
        KeyReplyInfo.DataBean keyinfo;
        String keyName = null;
        String key = null;
        String distributeTime = null;
        String keyType = null;
        try {
            JSONObject object = new JSONObject(keyjson);
            JSONArray array = object.getJSONArray("datas");
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    keyinfo = new KeyReplyInfo.DataBean();
                    JSONObject object2 = array.getJSONObject(i);
                    key = object2.getString("key");
                    keyName = object2.getString("sn_name");
//                    distributeTime=object2.getString("created_time");
                    distributeTime = key_gaintime;  //  获取钥匙本地时间
                    keyinfo.setSn_name(keyName);
                    keyinfo.setKey(key);
                    keyinfo.setCreated_time(distributeTime);
                    list.add(keyinfo);     // 钥匙归入集合
                }
                if (upKey) {
                    Toast.makeText(this, "钥匙更新成功！", Toast.LENGTH_SHORT).show();
                }
            } else {
                String result_msg = null;
                try {
                    result_msg = object.getString("error_msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result_msg != null) {
                    Toast.makeText(this, result_msg, Toast.LENGTH_SHORT).show();
                }
            }
            adapter.setKeydata(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //  更新钥匙钥匙列表———保存在本地
    private void upDataKeyList() {
        okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", token);
        FormBody formBody = builder.build();
        request = new Request.Builder().url(keyPath).post(formBody).build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String keyResult = response.body().string();
                Log.e("okhttp",keyResult);
                //  用户钥匙 保存在本地
                if (keyResult != null) {
                    SharedPreferences spf = KeyListingActivity.this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("keyjson", keyResult);    // 保存更新的钥匙字符串
                    editor.commit();      // 保存用户信息到本地
                    Message msg = new Message();
                    msg.obj = keyResult;
                    handler.sendMessage(msg);
                }
            }
        });
    }

//    // 获取选定的楼宇钥匙
//    private void getBuildingKeyList() {
//        okHttpClient = new OkHttpClient();
//        FormBody.Builder builder = new FormBody.Builder();
//        builder.add("token", token);
//        builder.add("subdistrict_id", sub_id);
//        builder.add("building_number", build_id);
//        FormBody formBody = builder.build();
//        request = new Request.Builder().url(build_keyPath).post(formBody).build();
//        call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String keyResult = response.body().string();
//                //  用户钥匙 保存在本地
//                if (keyResult != null) {
//                    SharedPreferences spf = KeyListingActivity.this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = spf.edit();
//                    editor.putString("keyjson", keyResult);    // 保存更新的钥匙字符串
//                    editor.commit();      // 保存用户信息到本地
//                    Message msg = new Message();
//                    msg.obj = keyResult;
//                    handler.sendMessage(msg);
//                }
//            }
//        });//   }


    @Subscribe
    public void onEventMainThread(BuildingEvent event) {
        if (event != null) {
            build_id = event.getBuild_id();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //获取当前时间
            Date curDate = new Date(System.currentTimeMillis());
            key_gaintime = formatter.format(curDate);
         //   getBuildingKeyList();   // 楼宇弹出框，获取选定楼宇钥匙———保存在本地
        }
    }


}

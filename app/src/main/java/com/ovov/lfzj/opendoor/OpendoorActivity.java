package com.ovov.lfzj.opendoor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izhihuicheng.api.lling.LLingOpenDoorConfig;
import com.izhihuicheng.api.lling.LLingOpenDoorHandler;
import com.izhihuicheng.api.lling.LLingOpenDoorStateListener;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.OpenLogUpInfo;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.StatusBarUtils;
import com.ovov.lfzj.base.widget.FeedbackDialog;
import com.ovov.lfzj.event.QRCodeEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.opendoor.capture.CaptureActivity;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * 令令门禁页面  2017/7/15
 */
public class OpendoorActivity extends BaseActivity {
    private final int PHOTO_REQUEST_CODE = 111;     // 是否开启相机权限
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;    //回调
    private static final int SENSOR_SHAKE = 3;      // 调用蓝牙开门
    private static final int OPEN_DOOR_BACK = 2;   // 开门返回信息结果
    private static final int OPEN_DOOR_fail = 5;   // 开门失败标识
    @BindView(R.id.opendoor_anim)        // 开门动画
            ImageView opendoorAnim;
    @BindView(R.id.open_top)
    ImageView openTop;
    @BindView(R.id.open_bottom)
    ImageView openBottom;
    @BindView(R.id.icon_result)         // 开门结果图片（ 成功—失败 ）
            ImageView iconResult;
    @BindView(R.id.open_door_title)    // 顶部标题
            TextView sub_title;

    private ActivityUtils activityUtils;
    private SensorManager sensorManager;           //传感器
    private Vibrator vibrator;                      //振动器

    private long lastUpdateTime = 0;
    private String[] arrayKey = null;                 // 解析钥匙数组
    private SoundPool soundPool = null;              // 声明开门声音对象
    private String token, sub_id, sub_name = null;               // 用户token ， 小区 id ，小区名称
    private String sn_name = null;                    //设备名称
    private String retrieve_key = null;              // key json字符串
    private String open_type = null;                 // 开门方式
    private String open_status = null;               // 开门结果
    private int open_num = 0;                        // 用户开门失败次数

    private Message msg = new Message();              // 开门反馈结果
    private boolean sound_con, shake_con;
    private FeedbackDialog dialog;                   // 开门反馈信息弹出框
    private FeedbackDialog.BuilderLog backLog;       // 展示开门反馈信息
    private Animation operatingAnim;


    /**
     * 蓝牙开门动作执行
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message open_msg) {
            super.handleMessage(open_msg);
            switch (open_msg.what) {
                case SENSOR_SHAKE:
                    if (arrayKey != null && arrayKey.length > 0)
                        checkBluetoothPermission();       // 蓝牙开门
                    else
                        feedback("请在钥匙列表更新钥匙");
                    break;
                case OPEN_DOOR_BACK:                // 开门成功返回信息结果
                    iconResult.setVisibility(View.VISIBLE);
                    iconResult.setImageResource(R.mipmap.icon_sucess);
                    opendoorAnim.clearAnimation();
                    opendoorAnim.invalidate();
                    opendoorAnim.setVisibility(View.GONE);

                    postOpenData();                  // 开门结果上传数据
//                    String back = (String) open_msg.obj;
//                    feedback(back);                     // 开门返回信息结果 ——二维码 ？
                    break;
                case OPEN_DOOR_fail:    //  蓝牙开门失败
                    iconResult.setVisibility(View.VISIBLE);
                    iconResult.setImageResource(R.mipmap.icon_faile);
                    opendoorAnim.clearAnimation();
                    opendoorAnim.invalidate();
                    opendoorAnim.setVisibility(View.GONE);
                    postOpenData();                 // 开门结果上传数据
                    String back_fail = (String) open_msg.obj;
                    feedback(back_fail);                  // 开门失败返回信息结果
                    break;
            }
        }
    };

    public static void toActivity(Context context) {
        Intent intent = new Intent(context,OpendoorActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_opendoor;
    }

    @Override
    public void init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setStatusBar(mActivity,false,false);
        EventBus.getDefault().register(this);                                            //   钥匙生成二维码监听事件

        postKeyList();          //  进入开门页面解析本地钥匙解析本地钥匙
        storageTokenRead();     //  获取token
        initShake();            // 初始化摇动传感器
        soundLoading();         // 初始化声音设置
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.opendoor_rotate);    // 定义开门动画
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }


    // 进入开门页面解析本地钥匙解析本地钥匙
    private void postKeyList() {
        SharedPreferences spf = this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
        String keyjson = spf.getString("keyjson", "");
        retrieve_key = keyjson;
        if (keyjson != null && !keyjson.trim().equals("")) {
            Json(keyjson);     // 解析钥匙
        }
    }

    //  从本地解析获取key
    private void Json(String keyjson) {
        try {
            JSONObject object = new JSONObject(keyjson);
            JSONArray array = object.getJSONArray("datas");
            arrayKey = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject object2 = array.getJSONObject(i);
                arrayKey[i] = object2.getString("key");    // 将钥匙转化成数组
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = token= LoginUserBean.getInstance().getAccess_token();
        sub_id = "1"  ;   // 小区 id
        sub_name = spf.getString("subName", "");   // 小区名称
        sound_con = spf.getBoolean("soundControl", true);     // 声音设置
        shake_con = spf.getBoolean("shakeControl", true);     // 震动设置
        sub_title.setText(sub_name);   // 显示开门页面名称
    }

    // 初始化传感器
    private void initShake() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);          //传感器管理服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);                //震动服务
    }

    // 初始化声音设置
    private void soundLoading() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);     // 初始化soundpool
        if (sound_con) {
            soundPool.load(this, R.raw.kaka, 1);                                   // 加载声音
            soundPool.load(this, R.raw.open_success, 1);                          // 加载声音
        }
    }

    @OnClick({R.id.re_scan, R.id.re_code, R.id.re_apply_key, R.id.icon_finish, R.id.icon_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_scan:         //  扫码开门
                requestPermission();
                break;
            case R.id.re_code:
                Intent intent = new Intent(OpendoorActivity.this, QRCodeActivity.class);             // 扫描二维码界面
                startActivity(intent);
                break;
            case R.id.re_apply_key:    // 钥匙列表
                 intent = new Intent(OpendoorActivity.this, KeyListingActivity.class);             // 扫描二维码界面
                startActivity(intent);
                break;
            case R.id.icon_finish:    //  退出页面
                finish();
                break;
            case R.id.icon_setting:   // 开门声音设置
                if (activityUtils == null) {
                    activityUtils = new ActivityUtils(this);
                }
                activityUtils.startActivity(OpenSettingActivity.class);
                break;
        }
    }

    /**
     * 判断是否开启权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                //请求获取录音权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PHOTO_REQUEST_CODE);
            } else {
                Intent intent = new Intent(OpendoorActivity.this, CaptureActivity.class);             // 扫描二维码界面
                startActivity(intent);
            }
        } else {
            //系统不高于6.0直接执行
            Intent intent = new Intent(OpendoorActivity.this, CaptureActivity.class);                 // 扫描二维码界面
            startActivity(intent);
        }
    }

    @Override     // 蓝牙，相机开启权限判断回调
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BlueToothNext(requestCode, grantResults);                  // 设备调用蓝牙权限判定——回调
        photoNext(requestCode, grantResults);
    }

    /**
     * 拍照权限回调
     *
     * @param requestCode
     * @param grantResults
     */
    private void photoNext(int requestCode, int[] grantResults) {
        if (requestCode == PHOTO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(OpendoorActivity.this, CaptureActivity.class);             // 扫描二维码界面
                startActivity(intent);
            } else {
                return;
            }
        }
    }

    /**
     * 如果同意，执行蓝牙开门 （蓝牙开门权限回调）
     *
     * @param requestCode
     * @param grantResults
     */
    private void BlueToothNext(int requestCode, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限——开门
                connectBluetooth();
            } else {
                // 权限拒绝
                Toast.makeText(this, "需要开启蓝牙才能开门！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    // 重力感应监听 传感器改变判断——————————————————去调用蓝牙开门
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (System.currentTimeMillis() - lastUpdateTime > 1000) {
                lastUpdateTime = System.currentTimeMillis();
                // 传感器信息改变时执行该方法
                float[] values = event.values;
                float x = values[0]; // x轴方向的重力加速度，向右为正 float[] values = event.values; float x = values[0]; // x轴方向的重力加速度，向右为正
                float y = values[1]; // y轴方向的重力加速度，向前为正
                float z = values[2]; // z轴方向的重力加速度，向上为正
                // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
                int medumValue = 13; // 三星 i9250怎么晃都不会超过20，
                int sensorType = event.sensor.getType();
                if (sensorType == Sensor.TYPE_ACCELEROMETER) {     // 如果摇动
                    if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {

                        //设置震动时长
                        if (shake_con) {
                            vibrator.vibrate(500);
                        }
                        Message msg_open = new Message();
                        msg_open.what = SENSOR_SHAKE;
                        handler.sendMessage(msg_open);       // 开门
                        //设置震动时长
                    }
                }
            }
        }

        // 当传感器精度的改变时  进行的操作
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * 开门蓝牙判断权限
     */
    private void checkBluetoothPermission() {
        soundPool.play(1, 1, 1, 0, 0, 1);      // 摇一摇开门声音
        initOpendoorAnimation();
        if (retrieve_key == null) {
            feedback("请更新钥匙");
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            } else {
                //具有权限
                connectBluetooth();
            }
        } else {
            //系统不高于6.0直接执行蓝牙开门
            connectBluetooth();
        }
    }

    private void initOpendoorAnimation() {
//动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;
        boolean isBack = true;
        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -1f;
            topToY = 0;
            bottomFromY = 1f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -1f;
            bottomFromY = 0;
            bottomToY = 1f;
        }
        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(1000);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);
        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(1000);
        bottomAnim.setFillAfter(true);
        //大家一定不要忘记, 当要回来时, 我们中间的两根线需要GONE掉
        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
                    opendoorAnim.setVisibility(View.VISIBLE);
                    opendoorAnim.setAnimation(operatingAnim);
                }
            });
        }
        //设置动画
        openTop.startAnimation(topAnim);
        openBottom.startAnimation(bottomAnim);
    }

    /**
     * 执行蓝牙开门
     */
    private void connectBluetooth() {
        if (arrayKey != null && arrayKey.length > 0) {                     // 钥匙存在时，开门
            LLingOpenDoorConfig config = new LLingOpenDoorConfig(1, arrayKey);          // 钥匙以数组形式调用令令SDK 开门
            LLingOpenDoorHandler open_handler = LLingOpenDoorHandler
                    .getSingle(OpendoorActivity.this);
            open_handler.doOpenDoor(config, listener);       // 开门方法调用
            open_type = "蓝牙";    // 用蓝牙方式开门（上传开门方式）
        } else {
            feedback("请在钥匙列表更新钥匙");
        }
    }

    //  开门结果————回调 状态定义
    public static final int RS_OD_SUCCESS = 1;
    public static final int RS_OD_FAILD = 2;
    public static final int RS_OD_ERROR = 3;
    public static final int RS_CONN_ERROR = 4;
    public static final int RS_CONN_NOFOUND = 5;
    private LLingOpenDoorStateListener listener = new LLingOpenDoorStateListener() {
        // 开门成功
        public void onOpenSuccess(String deviceKey, String sn, int openType) {
//            deviceKey——本次开门使用的钥匙 sn——设备SN码 openKey——开门方式
            try {
                JSONObject object = new JSONObject(retrieve_key);
                JSONArray array = object.getJSONArray("datas");
                String keykey = null;
                arrayKey = new String[array.length()];   // 钥匙数组
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    keykey = object2.getString("key");
                    arrayKey[i] = object2.getString("key");
                    if (deviceKey != null && deviceKey.equals(keykey)) {
                        sn_name = object2.getString("sn_name");     //  根据开门的钥匙检索钥匙名称
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            open_status = "成功";      // 提交开门成功参数值
            open_num = 0;    // 开门成功时，开门失败次数归零
            soundPool.play(2, 1, 1, 0, 0, 1);      // 摇一摇开门成功声音
            initMsg();        // 初始化message
            msg.what = OPEN_DOOR_BACK;   // 开门反馈结果
            msg.obj = "开门成功";
            handler.sendMessage(msg);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iconResult.setVisibility(View.GONE);    // 开门动画结束后，隐藏开门结果图片
                }
            }, 3000);
        }

        // 开门失败
        public void onOpenFaild(int errCode, int openType, String deviceKey, String sn, String desc) {
//           errCode——开门失败结果码 deviceKey——本次开门使用的钥匙
//           sn——设备SN码 openKey——开门方式 desc——开门结果信息反馈
            switch (errCode) {
                case RS_CONN_ERROR:
                    initMsg();        // 初始化message
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    msg.obj = "设备连接失败";
                    handler.sendMessage(msg);
                    break;
                case RS_CONN_NOFOUND:
                    initMsg();        // 初始化message
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    msg.obj = "设备未找到";
                    handler.sendMessage(msg);
                    break;
                case RS_OD_ERROR:
                    initMsg();        // 初始化message
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    msg.obj = "开门异常";
                    handler.sendMessage(msg);
                    break;
                case RS_OD_FAILD:
                    initMsg();        // 初始化message
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    open_num++;     // 开门失败次数，超过两次提醒用户更新钥匙
                    if (open_num > 2) {
                        msg.obj = "开门失败,请在钥匙列表更新钥匙 ！";
                    } else {
                        msg.obj = "开门失败";
                    }
                    handler.sendMessage(msg);
                    break;
                default:
                    break;
            }
            open_status = "失败";
            if (deviceKey != null) {            // 开门使用钥匙不为空时上传数据
                try {
                    JSONObject object = new JSONObject(retrieve_key);
                    JSONArray array = object.getJSONArray("datas");
                    String keykey = null;
                    arrayKey = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object2 = array.getJSONObject(i);
                        keykey = object2.getString("key");
                        arrayKey[i] = object2.getString("key");
                        if (deviceKey.equals(keykey)) {
                            sn_name = object2.getString("sn_name");      //  根据开门的钥匙key检索钥匙名称
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            soundPool.play(1, 1, 1, 0, 0, 1);      // 摇一摇开门声音——开门失败
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iconResult.setVisibility(View.GONE);    // 开门动画结束后，隐藏开门结果图片
                }
            }, 3000);
        }

        public void onConnectting(String deviceKey, String sn, int openType) {
            Log.i("BORTURN", "开始连接");
        }

        public void onFoundDevice(String deviceKey, String sn, int openType) {
            Log.i("BORTURN", "找到可用的设备");
        }

        public void onRunning() {
            Log.i("BORTURN", "onRunning");
        }
    };

    // 初始化message
    private void initMsg() {
        if (msg != null) {
            msg = null;
            msg = new Message();
        }
    }

    // 开门结果信息弹出
    public void feedback(String msg) {
        backLog = new FeedbackDialog.BuilderLog(OpendoorActivity.this);
        backLog.setContent(msg);
        dialog = backLog.feedbackDialog();
        dialog.setCanceledOnTouchOutside(true); // 点击外部区域关闭
        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager windowManager = getWindowManager();//为获取屏幕宽、高
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
//        lp.width = (int)(display.getWidth()); //宽度设置为全屏
//        lp.width = (int) (display.getWidth() * 0.4); //宽度设置为屏幕的0.8   //弹出框宽度
//        lp.width = 400; // 宽度
//        lp.height = 240; // 高度
        //设置背景透明度 背景透明
        lp.alpha = 0.85f;//参数为0到1之间。0表示完全透明，1就是不透明
        dialog.getWindow().setAttributes(lp);   //设置生效
        if (!OpendoorActivity.this.isFinishing()) {         // 在显示之前调用activity的isFinishing方法判断,如果页面存在，弹出
            dialog.show();
        } else {
            return;
        }
        // 2 秒后关闭提醒弹框
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    //  开门成功上传开门数据
    private void postOpenData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        String open_time = formatter.format(curDate);
        if (sn_name != null) {
            getUpDoorLog(token, sub_id, sn_name, open_type, open_status, open_time);      // 上传开门日志
        }
    }

    private void getUpDoorLog(String token, String sub_id, String sn_name, String open_type, String open_status, String open_time) {

        Subscription subscription = RetrofitHelper.getInstance().openLogUp(token, sub_id, sn_name, open_type, open_status, open_time)
                .compose(RxUtil.<OpenLogUpInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<OpenLogUpInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(OpenLogUpInfo openLogUpInfo) {
                        super.onNext(openLogUpInfo);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override     // 页面重新开始时监听
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {     // 注册传感监听器
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        postKeyList();                     //  进入开门页面解析本地钥匙解析本地钥匙
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        sound_con = spf.getBoolean("soundControl", true);     // 声音设置
        shake_con = spf.getBoolean("shakeControl", true);     // 震动设置
        soundLoading();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {// 取消传感监听器
            sensorManager.unregisterListener(sensorEventListener); //退出界面后把传感器释放
        }
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();  // 关闭开门信息反馈弹出框
        }
        EventBus.getDefault().unregister(this);   // 注销
        super.onDestroy();
    }

    //  钥匙生成二维码失败监听事件
    @Subscribe
    public void onEvenMainThread(QRCodeEvent codeEvent) {
        feedback(codeEvent.getMsg());     //  二维码生成失败，提示更新钥匙
    }

}
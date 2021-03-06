package com.ovov.lfzj.opendoor;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.izhihuicheng.api.lling.LLingOpenDoorConfig;
import com.izhihuicheng.api.lling.LLingOpenDoorHandler;
import com.izhihuicheng.api.lling.LLingOpenDoorStateListener;
import com.lingyun.qr.handler.QRUtils;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.OpenLogUpInfo;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.StatusBarUtils;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.FeedbackDialog;
import com.ovov.lfzj.base.widget.WaveView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.opendoor.capture.CaptureActivity;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 令令门禁页面  2017/7/15
 */
public class OpendoorActivity extends BaseActivity {
    private final int PHOTO_REQUEST_CODE = 111;     // 是否开启相机权限
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;    //回调
    private static final int SENSOR_SHAKE = 3;      // 调用蓝牙开门
    private static final int OPEN_DOOR_BACK = 2;   // 开门返回信息结果
    private static final int OPEN_DOOR_fail = 5;   // 开门失败标识
    //    @BindView(R.id.opendoor_anim)        // 开门动画
//            ImageView opendoorAnim;
//    @BindView(R.id.open_top)
//    ImageView openTop;
//    @BindView(R.id.open_bottom)
//    ImageView openBottom;
//    @BindView(R.id.icon_result)         // 开门结果图片（ 成功—失败 ）
//            ImageView iconResult;
    @BindView(R.id.open_door_title)    // 顶部标题
            TextView sub_title;
    @BindView(R.id.wave)
    WaveView wave;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.head1)
    ImageView head1;
    @BindView(R.id.lela)
    RelativeLayout lela;
    @BindView(R.id.refresh_tv)
    TextView refresh_tv;
    private static int QRC_WIDTH = 800;    //定义二维码长度
    private static int QRC_HEIGHT = 700;   //定义二维码宽度
    @BindView(R.id.but)
    Button but;
    private ActivityUtils activityUtils;
    private SensorManager sensorManager;           //传感器
    private Vibrator vibrator;                      //振动器
    private List<String> keys = new ArrayList<String>();
    private String keykey = null;    // 门禁钥匙
    private long lastUpdateTime = 0;
    private String[] arrayKey = null;                 // 解析钥匙数组
    private SoundPool soundPool = null;              // 声明开门声音对象
    private String token, sub_id, sub_name = null;               // 用户token ， 小区 id ，小区名称
    private String sn_name = "";                    //设备名称
    private String retrieve_key = null;              // key json字符串
    private int open_type = 1;                 // 开门方式
    private int open_status;               // 开门结果
    private int open_num = 0;                        // 用户开门失败次数
    private boolean sound_con, shake_con;
    private FeedbackDialog dialog;                   // 开门反馈信息弹出框
    private FeedbackDialog.BuilderLog backLog;       // 展示开门反馈信息
    private Animation operatingAnim;
    private boolean flag = true;
    private RxPermissions rxPermission;
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    String open;

    /**
     * 蓝牙开门动作执行
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message open_msg) {
            super.handleMessage(open_msg);
            switch (open_msg.what) {
                case SENSOR_SHAKE:
                    if (arrayKey != null && arrayKey.length > 0) {
                        //wave.start();
                        checkBluetoothPermission();       // 蓝牙开门

                    } else {
                        feedback("请在钥匙列表更新钥匙");
                        wave.stop();
                        wave.clearAnimation();
                    }

                    break;
                case OPEN_DOOR_BACK:                // 开门成功返回信息结果
                    Log.e("aaaaaaaaa", "111111111");
                    head.setImageResource(R.mipmap.lock_success);
                    wave.stop();
                    wave.clearAnimation();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            head.setImageResource(R.mipmap.lock_im);
                        }
                    }, 2000);
                    open_status = 1;
                    postOpenData(open_status);
                    // 开门结果上传数据
//                    String back = (String) open_msg.obj;
//                    feedback(back);                     // 开门返回信息结果 ——二维码 ？
                    break;
                case OPEN_DOOR_fail:
                    Log.e("dadadadada", OPEN_DOOR_fail + "");// 上传开门日
                    wave.stop();
                    String back_fail = (String) open_msg.obj;
                    feedback(back_fail);                  // 开门失败返回信息
                    break;
            }
        }
    };


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, OpendoorActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_opendoor;
    }

    @Override
    public void init() {
        rxPermission = new RxPermissions(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setStatusBar(mActivity, false, false);
//        EventBus.getDefault().register(this);                                            //   钥匙生成二维码监听事件

        postKeyList();          //  进入开门页面解析本地钥匙解析本地钥匙
        storageTokenRead();     //  获取token

        SharedPreferences spf = this.getSharedPreferences("opendoor", Context.MODE_PRIVATE);
        open = spf.getString("type", "1");
        Log.e("22222", open);
        if (open.equals("1")) {
            type();
        }
//        initShake();            // 初始化摇动传感器
//        soundLoading();         // 初始化声音设置
//        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.opendoor_rotate);    // 定义开门动画
//        LinearInterpolator lin = new LinearInterpolator();
//        operatingAnim.setInterpolator(lin);
        //wave.setColor(R.color.color_81b4ff);

        wave.setDuration(6000);
        wave.setStyle(Paint.Style.FILL);
        wave.setColor(Color.parseColor("#ffffff"));
        wave.setInitialRadius(159f);
        wave.setInterpolator(new LinearOutSlowInInterpolator());

    }


    // 进入开门页面解析本地钥匙解析本地钥匙
    private void postKeyList() {
        SharedPreferences spf = this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
        String keyjson = spf.getString("keyjson", "");
        retrieve_key = keyjson;
        Log.e("dada", retrieve_key);
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
                keykey = object2.getString("key");      // 钥匙集合列表
                keys.add(keykey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = token = LoginUserBean.getInstance().getAccess_token();
        sub_id = "1";   // 小区 id
        sub_name = spf.getString("subName", "");   // 小区名称
        sound_con = spf.getBoolean("soundControl", true);     // 声音设置
        shake_con = spf.getBoolean("shakeControl", true);     // 震动设置
        sub_title.setText(sub_name);   // 显示开门页面名称
    }


    @OnClick({R.id.re_scan, R.id.re_code, R.id.re_apply_key, R.id.icon_setting, R.id.head, R.id.but, R.id.refresh_tv})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.re_scan:         //  钥匙列表
                Intent intent = new Intent(OpendoorActivity.this, KeyListingActivity.class);             // 扫描二维码界面
                startActivity(intent);
                break;
            case R.id.re_code:
                intent = new Intent(OpendoorActivity.this, QRCodeActivity.class);             // 扫描二维码界面
                startActivity(intent);
                break;
            case R.id.re_apply_key:    // 房客通行
                intent = new Intent(OpendoorActivity.this, ApplyVisitorActivity.class);             // 扫描二维码界面
                startActivity(intent);
                break;
            case R.id.icon_setting:
                finish();
                break;
            case R.id.refresh_tv:
                //生成二维码
                initQrCode();
                break;

            case R.id.head:
                //防止重复点击 出现不一样的圆形
                if (UIUtils.isFastClick()) {
                    Log.e("4444", "44444");
                    wave.setVisibility(View.VISIBLE);
                    Message msg_open = new Message();
                    msg_open.what = SENSOR_SHAKE;
                    handler.sendMessage(msg_open);// 开门
                }

                break;
            case R.id.but:
                if (flag) {
                    wave.stop();
                    wave.clearAnimation();
                    wave.setVisibility(View.GONE);
                    type();

                } else {
                    blutoos();
                }
                break;
        }
    }

    private void blutoos() {
        SharedPreferences spf = this.getSharedPreferences("opendoor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("type", "2");
        editor.commit();
        lela.setVisibility(View.GONE);
        head.setVisibility(View.VISIBLE);
        refresh_tv.setVisibility(View.INVISIBLE);
        but.setText("切换至二维码开门");
        head.setImageResource(R.mipmap.lock_im);
        flag = true;
    }

    private void type() {
        SharedPreferences spf = this.getSharedPreferences("opendoor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("type", "1");
        editor.commit();
        if (keys.size() > 0) {
            but.setText("切换至蓝牙开门");
            //生成二维码
            initQrCode();
        } else {
            feedback("请在钥匙列表更新钥匙");
        }
        refresh_tv.setVisibility(View.VISIBLE);
        flag = false;
    }

    private void initQrCode() {
        if (keys != null && keys.size() > 0) {
            Log.e("keysssssss", keys.toString());
            QRUtils.loadConfig(this.getApplicationContext());
            String qrStr = null;
            qrStr = QRUtils.createDoorControlQR(this,
                    "0123456788", keys, 10, 1, 0, "12341234");    //  生成钥匙字符串
            Bitmap contentbitmap = ceateBitmap(qrStr);
            if (contentbitmap != null) {
                head.setVisibility(View.GONE);
                lela.setVisibility(View.VISIBLE);
                head1.setImageBitmap(contentbitmap);
                // 显示二维码
            }
//        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang);
//        Bitmap codebitmap = logoCodeBitmap(contentbitmap, logoBitmap);
//        if (codebitmap != null) {
//            ivQrcode.setImageBitmap(codebitmap);       // 显示二维码（包含logo）
//        }
        } else {
            Toast.makeText(mActivity, "请在钥匙列表更新钥匙", Toast.LENGTH_SHORT).show();
        }
    }


    // 生成钥匙二维码
    private Bitmap ceateBitmap(String qrStr) {
        Bitmap bitmap = null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix matrix = new QRCodeWriter().encode(qrStr, BarcodeFormat.QR_CODE, QRC_WIDTH, QRC_HEIGHT, hints);      //文本，编码格式，长度，宽度，第五个参数可选择，生成文本编码
            int[] pixs = new int[QRC_WIDTH * QRC_HEIGHT];      // 二维码int 数组
            for (int y = 0; y < QRC_HEIGHT; y++) {
                for (int x = 0; x < QRC_WIDTH; x++) {
                    if (matrix.get(x, y)) {
                        pixs[y * QRC_WIDTH + x] = 0xff000000;
                    } else {
                        pixs[y * QRC_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QRC_WIDTH, QRC_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixs, 0, QRC_WIDTH, 0, 0, QRC_WIDTH, QRC_HEIGHT);         // 二维码像素
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 向二维码中心添加logo 图片
     *
     * @param contentbitmap
     * @param logoBitmap
     * @return
     */
    private Bitmap logoCodeBitmap(Bitmap contentbitmap, Bitmap logoBitmap) {
        int cBitmapWidth = contentbitmap.getWidth();    //  内容生成二维码的宽度
        int cBitmapHtight = contentbitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();     // 二维码中间图标的高度
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(cBitmapWidth, cBitmapHtight, Bitmap.Config.ARGB_8888);   // 创建空的画板图片
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(contentbitmap, 0, 0, null);   //将内容生成的二维码画到画板上
        canvas.save(Canvas.ALL_SAVE_FLAG);   //保存二维码图片
        float scaleSize = 1.0f;
        // 获取logo图标的绘制缩放比例
        while ((logoBitmapWidth / scaleSize) > (cBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (cBitmapHtight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;    //logo 缩放比例
        canvas.scale(sx, sx, cBitmapWidth / 2, cBitmapHtight / 2);  //前两个参数表示宽高的缩放比例，大于1表示放大，小于1表示缩小，后两个参数表示缩放的中心点
        canvas.drawBitmap(logoBitmap, (cBitmapWidth - logoBitmapWidth) / 2, (cBitmapHtight - logoBitmapHeight) / 2, null);
        ;
        canvas.restore();  // 调用canvas的restore方法将画布恢复为原来的状态
        return blankBitmap;
    }


//    @Override     // 蓝牙，相机开启权限判断回调
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        BlueToothNext(requestCode, grantResults);                  // 设备调用蓝牙权限判定——回调
//        photoNext(requestCode, grantResults);
//    }
//
//    /**
//     * 拍照权限回调
//     *
//     * @param requestCode
//     * @param grantResults
//     */
//    private void photoNext(int requestCode, int[] grantResults) {
//        if (requestCode == PHOTO_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(OpendoorActivity.this, CaptureActivity.class);             // 扫描二维码界面
//                startActivity(intent);
//            } else {
//                return;
//            }
//        }
//    }
//
//    /**
//     * 如果同意，执行蓝牙开门 （蓝牙开门权限回调）
//     *
//     * @param requestCode
//     * @param grantResults
//     */
//    private void BlueToothNext(int requestCode, int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限——开门
//                connectBluetooth();
//            } else {
//                // 权限拒绝
//                Toast.makeText(this, "需要开启蓝牙才能开门！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//    }


    /**
     * 开门蓝牙判断权限
     */
    private void checkBluetoothPermission() {
        //  soundPool.play(1, 1, 1, 0, 0, 1);      // 摇一摇开门声音
        //  initOpendoorAnimation();
        if (retrieve_key == null) {
            feedback("请更新钥匙");
            return;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            //做一些处理
            LocationManager systemService = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
            boolean ok = systemService.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ok) {
                rxPermission.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {
                                Log.e("permissions", Manifest.permission.ACCESS_FINE_LOCATION + "：" + permission.granted);

                                if (permission.name.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    connectBluetooth();
                                }


                            }
                        });
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("定位服务")
                        .setMessage("暂未开启定位服务")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, PRIVATE_CODE);
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        } else {
            //在版本低于此的时候，做一些处理
            connectBluetooth();
        }





    //校验是否已具有模糊定位权限
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//            } else {
    //具有权限
//
//            }
//        } else {
//            //系统不高于6.0直接执行蓝牙开门
//            connectBluetooth();
//        }
}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PRIVATE_CODE:
                //开启GPS，重新添加地理监听
                connectBluetooth();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 执行蓝牙开门
     */
    private void connectBluetooth() {
        wave.start();
        if (arrayKey != null && arrayKey.length > 0) {                     // 钥匙存在时，开门
            Log.e("arrayKey", arrayKey + "");
            LLingOpenDoorConfig config = new LLingOpenDoorConfig(1, arrayKey);          // 钥匙以数组形式调用令令SDK 开门
            LLingOpenDoorHandler open_handler = LLingOpenDoorHandler
                    .getSingle(OpendoorActivity.this);
            open_handler.doOpenDoor(config, listener);       // 开门方法调用
            //        open_type = "蓝牙";    // 用蓝牙方式开门（上传开门方式）
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
            Message msg = new Message();              // 开门反馈结果
            msg.what = OPEN_DOOR_BACK;   // 开门反馈结果
            msg.obj = "开门成功";
            handler.sendMessage(msg);
//            Log.e("eeeeeeee", "开门成功");
//            try {
//                JSONObject object = new JSONObject(retrieve_key);
//                JSONArray array = object.getJSONArray("datas");
//                String keykey = null;
//                arrayKey = new String[array.length()];   // 钥匙数组
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object2 = array.getJSONObject(i);
//                    keykey = object2.getString("key");
//                    arrayKey[i] = object2.getString("key");
////                    if (deviceKey != null && deviceKey.equals(keykey)) {
////                        sn_name = object2.getString("sn_name");     //  根据开门的钥匙检索钥匙名称
////                    }
//                    sn_name = sn;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            sn_name = sn;
        }

        // 开门失败
        public void onOpenFaild(int errCode, int openType, String deviceKey, String sn, String desc) {
//           errCode——开门失败结果码 deviceKey——本次开门使用的钥匙
//           sn——设备SN码 openKey——开门方式 desc——开门结果信息反馈
            Message msg = new Message();

            switch (errCode) {
                case RS_CONN_ERROR:
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    msg.obj = "设备连接失败";
                    handler.sendMessage(msg);
                    break;
                case RS_CONN_NOFOUND:
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    msg.obj = "设备未找到";
                    handler.sendMessage(msg);
                    break;
                case RS_OD_ERROR:
                    msg.what = OPEN_DOOR_fail;    // 开门反馈结果
                    msg.obj = "开门异常";
                    handler.sendMessage(msg);
                    break;
                case RS_OD_FAILD:

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
////            //open_status = "失败";
//            if (deviceKey != null) {            // 开门使用钥匙不为空时上传数据
//                try {
//                    JSONObject object = new JSONObject(retrieve_key);
//                    JSONArray array = object.getJSONArray("datas");
//                    String keykey = null;
//                    arrayKey = new String[array.length()];
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object2 = array.getJSONObject(i);
//                        keykey = object2.getString("key");
//                        arrayKey[i] = object2.getString("key");
//                        if (deviceKey.equals(keykey)) {
//                            sn_name = object2.getString("sn_name");      //  根据开门的钥匙key检索钥匙名称
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

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


    // 开门结果信息弹出
    public void feedback(String msg) {
        head.setImageResource(R.mipmap.lock_loser);
        wave.setVisibility(View.INVISIBLE);
        wave.stop();
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
                head.setImageResource(R.mipmap.lock_im);
                dialog.dismiss();
            }
        }, 3000);
    }

    //  开门成功上传开门数据
    private void postOpenData(int open_status) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 获取当前时间
//        Date curDate = new Date(System.currentTimeMillis());
//        String open_time = formatter.format(curDate);
        if (sn_name != null) {
            getUpDoorLog(sn_name, open_type, open_status);

        }
    }

    private void getUpDoorLog(String sn_name, int open_type, int open_status) {

        Subscription subscription = RetrofitHelper.getInstance().openLogUp(sn_name, open_type, open_status)
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
        postKeyList();                     //  进入开门页面解析本地钥匙解析本地钥匙
        SharedPreferences spf = this.getSharedPreferences("opendoor", Context.MODE_PRIVATE);
        sound_con = spf.getBoolean("soundControl", true);     // 声音设置
        shake_con = spf.getBoolean("shakeControl", true);     // 震动设置

        if (open.equals("2")) {
            lela.setVisibility(View.GONE);
            head.setVisibility(View.VISIBLE);
            refresh_tv.setVisibility(View.INVISIBLE);
            but.setText("切换至二维码开门");
            head.setImageResource(R.mipmap.lock_im);
            flag = true;
        }
        //     soundLoading();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (sensorManager != null) {// 取消传感监听器
//            sensorManager.unregisterListener(sensorEventListener); //退出界面后把传感器释放
//        }
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();  // 关闭开门信息反馈弹出框
        }
//        EventBus.getDefault().unregister(this);   // 注销
        super.onDestroy();
    }
//    //  钥匙生成二维码失败监听事件
//    @Subscribe
//    public void onEvenMainThread(QRCodeEvent codeEvent) {
//        feedback(codeEvent.getMsg());     //  二维码生成失败，提示更新钥匙
//    }

}
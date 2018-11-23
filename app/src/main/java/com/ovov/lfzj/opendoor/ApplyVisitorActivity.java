package com.ovov.lfzj.opendoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lingyun.qr.handler.QRUtils;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.bean.SubdistrictsBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.api.CatelApiService;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.opendoor.adapter.HouseListAdapter;
import com.ovov.lfzj.opendoor.lockutil.CrashHandler;
import com.ovov.lfzj.opendoor.present.ApplyVisitorPresent;
import com.ovov.lfzj.opendoor.view.ApplyVisitorView;
import com.ovov.lfzj.opendoor.view.InviterKeyDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscription;

/**
 * 生成访客通行二维码页面
 * 原磊  2017/07/15
 */
public class ApplyVisitorActivity extends BaseActivity implements OnDateSetListener, ApplyVisitorView {
    @BindView(R.id.lock_invite_name)
    EditText invite_name;                       // 被邀请人姓名
    @BindView(R.id.lock_invite_phone)
    TextView phone_number;                     // 被邀请人电话
    @BindView(R.id.lock_invite_validConunt)
    EditText invite_validCount;               // 最多可用次数
    @BindView(R.id.lock_invite_startTime)
    TextView invite_startTime;                // 开始时间
    @BindView(R.id.lock_invite_validTime)
    EditText invite_vlidTime;                 // 有效时间
    @BindView(R.id.lock_invite_button)
    TextView invite_button;                   // 生成二维码
    @BindView(R.id.iv_house_photo)            // 个人信息头像
            CircleImageView ivHousePhoto;
    @BindView(R.id.tv_house_title)
    TextView tvHouseTitle;
    @BindView(R.id.tv_adress_house)
    TextView tvAdressHouse;

    private static int QRC_WIDTH = 500;     //定义二维码长度
    private static int QRC_HEIGHT = 500;    //定义二维码宽度
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.user_relative)
    RelativeLayout userRelative;

    private TimePickerDialog mDialogAll;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm");      // 设置时间格式
    private String inviteName = null;      // 访客姓名
    private String invitePhone = null;     // 访客电话
    private String useCount = null;        // 有效次数
    private String vaildTime = null;       // 二维码有效时间
    private String createTime = null;      // 访客二维码生成时间
    private ApplyVisitorPresent present;
    private String token = null;               // token
    private String sub_id = "1";               // 小区 id
    private ActivityUtils activityUtil;
    private List<String> keys = new ArrayList<String>();
    private GregorianCalendar cal1 = new GregorianCalendar();    // 进入页面日历转化
    private GregorianCalendar cal2 = new GregorianCalendar();    // 选择开始日历毫秒
    private long days_num = 0;                                    // 选择开始日期与当前时间间隔
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Bitmap bitmap;
    private String keyPath = CatelApiService.HOST + "v1/entrance/applyKey";       // 更新钥匙列表

    private List<String> list;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ApplyVisitorActivity.class);
        context.startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            Json(result);        //  解析钥匙列表
        }
    };

    @Override
    public void init() {
        list = new ArrayList();
        gethomeList();
        initUserInfo();           // 用户信息标签
        postKeyList();           //  解析本地钥匙
        present = new ApplyVisitorPresent(this);       // 初始化访客信息提交方法
        Date curDate = new Date(System.currentTimeMillis());     // 获取当前时间
        String str = formatter.format(curDate);

        tvTitle.setText("访客通行");
        tvRight.setText("访客记录");
        if (!TextUtils.isEmpty(LoginUserBean.getInstance().getUserInfoBean().user_logo)) {
            Picasso.with(this).load(LoginUserBean.getInstance().getUserInfoBean().user_logo).into(ivHousePhoto);
        } else {
            Picasso.with(this).load(R.mipmap.ic_default_head).into(ivHousePhoto);
        }

        tvHouseTitle.setText(LoginUserBean.getInstance().getUserInfoBean().nickname);
        tvAdressHouse.setText(LoginUserBean.getInstance().getSubname());
        createTime = str;                                      //  当前时间
        invite_startTime.setText(createTime);                        //  显示当前的时间
        cal1.setTime(curDate);                                  //  当前时间转化为毫秒
    }


    // 用户信息标签
    private void initUserInfo() {
        String phone = LoginUserBean.getInstance().getPhone();
//        String username = LoginUserBean.getInstance().getUserInfoBean().username;
//       String pic= LoginUserBean.getInstance().getUserInfoBean().user_logo;

//        if (pic != null && !pic.equals("") && !pic.equals(" "))
//            Picasso.with(this).load(pic).error(R.drawable.banner_default).into(ivHousePhoto);
//        tvHouseTitle.setText(username);
//        tvAdressHouse.setText(sunName);
    }

    //解析本地钥匙  解析本地钥匙
    private void postKeyList() {
        SharedPreferences spf = this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
        String keyjson = spf.getString("keyjson", "");
        Json(keyjson);     // 解析钥匙
    }

    //  从本地获取key
    private void Json(String keyjson) {
        try {
            JSONObject object = new JSONObject(keyjson);
            JSONArray array = object.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                String keykey = null;
                JSONObject object2 = array.getJSONObject(i);
                keykey = object2.getString("key");
                keys.add(keykey);       // 生成二维码钥匙集合
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_right, R.id.lock_invite_button, R.id.lock_invite_startTime, R.id.iv_back, R.id.user_relative})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:         // 访客通行记录
                if (activityUtil == null)
                    activityUtil = new ActivityUtils(this);
                activityUtil.startActivity(VistitorActivity.class);
                break;
            case R.id.lock_invite_button:
                if (invite_name.getText().toString().isEmpty()){
                    showToast("请输入姓名");
                    return;
                }
                if (!RegexUtils.isNoText(invite_name.getText().toString())){
                    showToast("请输入正确的姓名");
                    return ;
                }
                boolean tf = decideFormate();      //判断输入信息的格式
                if (tf) {
                    if (keys != null && keys.size() > 0) {
                        present.setVisitorUpInfo(sub_id, inviteName, invitePhone, useCount, vaildTime);
                    } else {
                        upDataKeyList();
                    }
                } else {
                    return;
                }
                break;
            case R.id.lock_invite_startTime:    // 访客二维码生成时间
                showDatePicker();                   // 时间选择器
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.user_relative:
                initpopuwindow(tvAdressHouse);
                break;
        }
    }


    private void initpopuwindow(final TextView textView) {
        View view = View.inflate(this, R.layout.popup_activity_title1, null);
        ListView lv_appointment = (ListView) view.findViewById(R.id.activity_title_recy);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HouseListAdapter adapter = new HouseListAdapter(list, this);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        lv_appointment.setAdapter(adapter);

        lv_appointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(list.get(position));
                popupWindow.dismiss();

            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });


        // 使其聚集,可点击
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        //  popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    //  更新钥匙钥匙列表———保存在本地
    private void upDataKeyList() {
        okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", LoginUserBean.getInstance().getAccess_token());
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
                //  用户钥匙 保存在本地
                if (keyResult != null) {
                    SharedPreferences spf = ApplyVisitorActivity.this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
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

    //展示时间选择器
    private void showDatePicker() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("开始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(19)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    //  获取时间选择器选择时间
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        if (days_num > 4) {
            Toast.makeText(this, "只限定4天时间", Toast.LENGTH_SHORT).show();
            showDatePicker();                   // 时间选择器
        } else {
            createTime = text;                     // 选择的时间
            invite_startTime.setText(createTime);     //  显示选择的时间
        }
    }

    //时间转换
    public String getDateToString(long time) {
        Date d = new Date(time);
        cal2.setTime(d);             //  选择时间转化为毫秒
        days_num = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);
        return formatter.format(d);
    }

    //判断输入信息的格式
    private boolean decideFormate() {
        inviteName = invite_name.getText().toString();
        invitePhone = phone_number.getText().toString();
        useCount = invite_validCount.getText().toString();
        vaildTime = invite_vlidTime.getText().toString();
        if (TextUtils.isEmpty(inviteName) || TextUtils.isEmpty(invitePhone) || TextUtils.isEmpty(useCount)
                || TextUtils.isEmpty(vaildTime)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (RegexUtils.verifyMobile(invitePhone)) {
                if (RegexUtils.isNumeric(useCount)) {
                    int useCountInt = Integer.parseInt(useCount);
                    if (useCountInt < 1) {
                        Toast.makeText(this, "最小使用次数为1次", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (useCountInt > 6) {
                        Toast.makeText(this, "最大使用次数为6次", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "请输入正确的使用次数", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (RegexUtils.isNumeric(vaildTime)) {
                    int vaildTimeInt = Integer.parseInt(vaildTime);
                    if (vaildTimeInt < 1) {
                        Toast.makeText(this, "最短有效期为1分钟", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (vaildTimeInt > 120) {
                        Toast.makeText(this, "最长有效期为120分钟", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "请输入正确的有效期", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    //——————————————————————————————服务端返回数据————————————————————————
    //  提交访客信息返回数据
    @Override
    public void setVisitorData(ServerFeedBackInfo infoData) {

        if (infoData.isDatas() && infoData.getCode().equals("200")) {
            qRQcode();             //  信息提交成功，生成访客二维码
        } else {
            Toast.makeText(this, "请求错误", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void showMsg(String dataResult) {
        Toast.makeText(this, dataResult, Toast.LENGTH_SHORT).show();
    }


    // 生成访客二维码
    private void qRQcode() {
        int time = Integer.parseInt(invite_vlidTime.getText().toString());          //  二维码有效时间
        int count = Integer.parseInt(invite_validCount.getText().toString());      //  二维码有效次数
        CrashHandler.getInstance().init(getApplicationContext());
        QRUtils.loadConfig(getApplicationContext());
        String qrStr = null;
        qrStr = QRUtils.createDoorControlQR(ApplyVisitorActivity.this,
                "0123456788", keys, time, count, 0, "12341234");
        Log.i("kkkk", "ggg" + qrStr);
        bitmap = ceateBitmap(qrStr);

        InviterKeyDialog.Builder dialogbuild = new InviterKeyDialog.Builder(this);
        dialogbuild.setBitmap(bitmap);
        //  分享访客二维码点击
        dialogbuild.setVisitorShareListener(new InviterKeyDialog.Builder.VisitorDialogListener() {
            @Override
            public void visitorShareClick() {
                GetandSaveCurrentImage();
            }
        });

        InviterKeyDialog dialog = dialogbuild.creeate();
        dialog.setCanceledOnTouchOutside(false); // 点击外部区域（true 关闭 ，false 不关闭）
        dialog.show();

        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager windowManager = getWindowManager();//为获取屏幕宽、高
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
//                lp.width = (int)(display.getWidth()); //宽度设置为全屏
        lp.width = (int) (display.getWidth() * 0.88); //宽度设置为屏幕的0.8
        //设置背景透明度 背景透明
        lp.alpha = 0.9f;//参数为0到1之间。0表示完全透明，1就是不透明
        dialog.getWindow().setAttributes(lp);   //设置生效
    }

    /**
     * 生成内容二维码
     *
     * @param qrStr
     * @return
     */
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
     * 获取和保存当前屏幕的截图
     */
    private void GetandSaveCurrentImage() {
        //1.构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

//        Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888 );
//        //2.获取屏幕截屏
//        View decorview = this.getWindow().getDecorView();
//        decorview.setDrawingCacheEnabled(true);
//        Bmp = decorview.getDrawingCache();

        String SavePath = getSDCardPath() + "/AndyDemo/ScreenImage";

        //3.保存Bitmap
        try {
            File path = new File(SavePath);
            //文件
            String filepath = SavePath + "/yaoShi.png";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
//                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SDCard的目录路径功能
     *
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    public void gethomeList() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().gethomeList()
                .compose(RxUtil.<SubListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SubListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            dismiss();
                            DataResultException dataResultException = (DataResultException) e;
                            showMsg(dataResultException.errorInfo);
                        } else {

                            //     homeView.doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SubListBean listInfoDataInfo) {
                        list.clear();
                        dismiss();
                        if (listInfoDataInfo.getCode().equals("200")) {
                            for (SubdistrictsBean s : listInfoDataInfo.getDatas().getSubdistricts()) {
                                list.add(s.getSubdistrict_name());
                            }

                        }
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_visitor;
    }

}

package com.ovov.lfzj.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.PhotoUtils;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.CustompopupWindow;
import com.ovov.lfzj.event.ReviseNicknameEvent;
import com.ovov.lfzj.event.ReviseSignEvent;
import com.ovov.lfzj.event.UpdateEvent;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 我的页面
 * Created by 袁磊 on 2017/7/28.
 */
public class UserInfoActivity extends BaseActivity implements CustompopupWindow.OnPopItemClickListener, OnDateSetListener {
    private final int PHOTO_REQUEST_CODE = 111;                    // 是否开启相机权限
    private final int STORAGE_PERMISSIONS_REQUEST_CODE = 222;    //  获取SDK权限
    private static final int RESULT_CODE_CAMEA = 001;           //拍照
    private static final int RESULT_CODE_PHOTO = 002;           //相册
    private static final int RESULT_CUT_PHOTO = 003;            //裁剪图片

    @BindView(R.id.user_info_photo)               //  显示头像
            ImageView userInfoPhoto;
    @BindView(R.id.tv_birthday)                   // 生日
            TextView tvBirthday;
    @BindView(R.id.mine_personal_tv_neekname)   // 昵称
            TextView tvNeekname;
    @BindView(R.id.tvsign)                         // 签名
            TextView tvsign;
    @BindView(R.id.sex_spinner)                   //  性别选择
            Spinner sexSpinner;


    private TimePickerDialog birthdayTimePicker;   // 时间选择器
    private int sexPosistion = 0;                // 性别下标
    private String user_imageUi = null;       // 上传头像后服务端返回的头像路径
    private String token = null;
    private String user_name = null;          // 上传用户信息
    private String user_sex = null;           // 用户性别    0 男    1 女
    private String user_birthday = null;     // 用户生日
    private String user_signature = null;    // 用户个性签名
    private String imagepath, birth, nickname, sex, signature = null;   // 用户头像网络路径 昵称 签名（获取数据）

    private CustompopupWindow pop;          // popupWindow 弹出框
    private File path = new File(Environment.getExternalStorageDirectory().getPath());
    private File outputImage = new File(path + "/photo.jpg");                      // 拍照———图片缓存转换路径
    private Uri imageUri;                  //定义的图片路径
    private Uri cropImageUri;             // 拍照、相册选择定义最终路
    private File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/touxiang.jpg");  // 最终转换生成路径
    private File touxiangFile;
    private MultipartBody.Part part;

    public static void toActivity(Context context, SubListBean.SubdataBean.UserInfoBean userInfoBean) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("userinfo", userInfoBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void init() {
        setTitleText("个人信息");
        setRightText("更新");
        getUserMessage();         // 获取用户个人信息——显示

        requestPermission();      // 6.0申请拍照权限
        autoObtainStoragePermission();        //  自动获取sdk权限
        initSpinner();           // 性别选择
        addRxBusSubscribe(ReviseNicknameEvent.class, new Action1<ReviseNicknameEvent>() {
            @Override
            public void call(ReviseNicknameEvent reviseNicknameEvent) {
                tvNeekname.setText(reviseNicknameEvent.nickname);
            }
        });
        addRxBusSubscribe(ReviseSignEvent.class, new Action1<ReviseSignEvent>() {
            @Override
            public void call(ReviseSignEvent reviseSignEvent) {
                tvsign.setText(reviseSignEvent.nickname);
            }
        });
    }


    // 获取用户个人信息——显示
    private void getUserMessage() {
        SubListBean.SubdataBean.UserInfoBean infoData = (SubListBean.SubdataBean.UserInfoBean) getIntent().getSerializableExtra("userinfo");
        if (infoData != null) {
            imagepath = infoData.user_logo;
            birth = infoData.birthday;
            nickname = infoData.nickname;
            sex = infoData.sex;
            signature = infoData.signature;
        }
        //  显示头像
        if (imagepath != null && !imagepath.equals("") && !imagepath.equals(" ")) {
            Picasso.with(this).load(imagepath).placeholder(R.mipmap.ic_default_head).error(R.mipmap.ic_default_head).into(userInfoPhoto);
        }
        user_name = nickname;                    //  昵称
        user_sex = sex;                          //  性别标注
        sexPosistion = Integer.parseInt(sex);            // 性别下标
        user_birthday = birth;                 //  生日
        user_signature = signature;           //  签名
        tvBirthday.setText(birth);                      // 显示生日
        tvNeekname.setText(nickname);                  // 显示昵称
        tvsign.setText(signature);                     //  显示签名
    }

    // 页面点击事件
    @OnClick({R.id.re_photo, R.id.re_address, R.id.re_birthday, R.id.re_neekname,
            R.id.re_sign, R.id.tv_right, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_photo:        // 更新用户头像
                RxPermissions rxPermission = new RxPermissions(this);
                rxPermission.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    setPhoto();                               // 拍照、相册弹出框
                                }
                            }
                        });

                break;
            case R.id.re_address:                               // 用户地址
               /* if (activityUtils == null) {
                    activityUtils = new ActivityUtils(this);
                }
                activityUtils.startActivity(AddressActivity.class);*/
                break;
            case R.id.re_birthday:     // 更新用户生日
                selectBirthday();
                break;
            case R.id.re_neekname:    // 更新用户昵称
                NeeknameActivity.toActivity(mActivity);
                break;
            case R.id.re_sign:         // 更新用户个性签名
                SignActivity.toActivity(mActivity);
                break;
            case R.id.tv_right:   // 上传更新用户信息
                // present.getUpUserMessage(token, user_name, user_sex, user_birthday, user_signature, user_imageUi);   // 上传修改后的个人用户信息
                updateInfo();
                break;
            case R.id.iv_back:
                finish();
                break;

            default:
        }
    }


    private void updateInfo() {
        showLoadingDialog();
        if (touxiangFile != null)
            part = MultipartBody.Part.createFormData("user_logo", touxiangFile.getName(), RequestBody.create(null, touxiangFile));
        Subscription subscription = RetrofitHelper.getInstance().userInfoUpdate(tvNeekname.getText().toString(), tvBirthday.getText().toString(), tvsign.getText().toString(), user_sex, part)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            dismiss();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("更新成功");
                        RxBus.getDefault().post(new UpdateEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 判断是否开启拍照权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                //请求获取录音权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PHOTO_REQUEST_CODE);
            } else {
                if (hasSdcard()) {
                    initPhoto();   //  调用拍照弹出框获取图片
                } else {
                    Toast.makeText(this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            //系统不高于6.0直接执行
            initPhoto();   //获取图片
        }
    }

    /**
     * 自动获取sdk权限
     */
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            initPhoto();   //  调用拍照弹出框获取图片
        }
    }

    // 相机开启权限判断回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case PHOTO_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        initPhoto();   //获取图片
                    } else {
                        Toast.makeText(this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请允许打开相机", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        initPhoto();   //获取图片
                    } else {
                        Toast.makeText(this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请允许打操作SDCard", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    // 拍照、相册弹出框
    private void initPhoto() {
        // 选择图片——拍照、相册
        pop = new CustompopupWindow(this);
        pop.setPopItemClickListener(this);
    }

    //设置头像
    private void setPhoto() {
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
        pop.showAtLocation(this.findViewById(R.id.mine_person_info), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);     // 第一个参数popup显示activity页面
        // popup 退出时界面恢复
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
    }

    // （调用拍照弹出框）选择图片方式
    @Override
    public void setPopItemClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_take_photo:
                imageUri = Uri.fromFile(outputImage);    //将file 转化为uri路径——————————图片缓存转换路径
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(this, "com.leFu.fileProvider", outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);   //指定图片输出地址   将拍照结果保存至photo_file的Uri中，不保留在相册中
                startActivityForResult(intent, RESULT_CODE_CAMEA);   //启动相机  startActivityForResult() 结果返回onActivityResult()函数   第2个参数——拍照返回参数
                pop.dismiss();
                break;
            case R.id.id_btn_select:
                //调用相册
                Intent photo = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photo.setType("image/*");
                startActivityForResult(photo, RESULT_CODE_PHOTO);
                pop.dismiss();
                break;
            case R.id.id_btn_cancel:
                pop.dismiss();
                break;
        }
    }

    /**
     * 拍照、调用相册回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CODE_CAMEA:      // 拍照后裁剪图片
                    cropImageUri = Uri.fromFile(filepath);
                    Intent intent_pho = new Intent("com.android.camera.action.CROP"); //剪裁
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent_pho.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent_pho.setDataAndType(imageUri, "image/*");     //设置"image/*" 的数据
                    intent_pho.putExtra("crop", "true");
                    //设置宽高比例
                    intent_pho.putExtra("aspectX", 1);      //裁剪方框宽的比例
                    intent_pho.putExtra("aspectY", 1);
                    //设置裁剪图片宽高
                    intent_pho.putExtra("outputX", 200);    // 裁剪图片宽
                    intent_pho.putExtra("outputY", 200);
                    intent_pho.putExtra("scale", true);    //是否保持比例
                    intent_pho.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);            //将剪切的图片保存到目标Uri中
                    intent_pho.putExtra("return-data", false);   //是否返回bitmap
                    intent_pho.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent_pho.putExtra("noFaceDetection", true);
//                //广播刷新相册  Intent启动MediaScanner服务扫描指定的文件
//                Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                intentBc.setData(imageUri);
//                this.sendBroadcast(intentBc);
                    startActivityForResult(intent_pho, RESULT_CUT_PHOTO); // 设置裁剪参数显示图片至ImageView
                    break;
                case RESULT_CODE_PHOTO:    // 相册裁剪图片
                    if (data != null) {
                        cropImageUri = Uri.fromFile(filepath);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));      // 图片缓存转换路径
                        // 内容提供者标示
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.leFu.fileProvider", new File(newUri.getPath()));
                        }
                        cutBitmap(newUri);    // 获取相册图片路径
                    }
                    break;
                case RESULT_CUT_PHOTO:     // 显示裁剪后图片
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserInfoActivity.this.getContentResolver(), cropImageUri);
                        if (bitmap != null) {
                            userInfoPhoto.setImageBitmap(bitmap);              //  显示头像
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 上传头像
                    try {
                        Bitmap bitmap_up = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(cropImageUri));
                        try {             //保存头像到本地
                            saveBitmap(bitmap_up);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 相册裁剪图片
     *
     * @param uri
     */
    private void cutBitmap(Uri uri) {
        Intent intent1 = new Intent("com.android.camera.action.CROP"); //剪裁
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent1.setDataAndType(uri, "image/*");     //设置"image/*" 的数据
        intent1.putExtra("crop", "true");
        //设置宽高比例
        intent1.putExtra("aspectX", 1);
        intent1.putExtra("aspectY", 1);
        //设置裁剪图片宽高
        intent1.putExtra("outputX", 200);
        intent1.putExtra("outputY", 200);
        intent1.putExtra("scale", true);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        startActivityForResult(intent1, RESULT_CUT_PHOTO); //设置裁剪参数显示图片至ImageView
    }

    /**
     * 保存头像到本地
     * 上传头像图片
     *
     * @param bitmap
     * @return
     * @throws IOException
     */
    private File saveBitmap(Bitmap bitmap) throws IOException {
        String path_head = Environment.getExternalStorageDirectory().toString();   // 生成头像路径
        File dirFile = new File(path_head);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        // 0-100 100为不压缩
        int img_quality = 80;
        touxiangFile = new File(path_head + "/touxiang.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(touxiangFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, img_quality, bos);
        bos.flush();
        bos.close();
        //present.getUpImageUi(touxiangFile);
        return touxiangFile;
    }


    // 性别判断
    private void initSpinner() {
        List<String> sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sexSpinner.setAdapter(adapter);
        sexSpinner.setSelection(sexPosistion -1);
        adapter.notifyDataSetChanged();
        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //sexPosistion = i;
                /*if (sexPosistion == 0) {
                    user_sex = "1";
                }
                if (sexPosistion == 1) {
                    user_sex = "2";
                }*/
                if (l == 0){
                    user_sex = "1";
                }else {
                    user_sex = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // 选择出生日期（时间选择器）
    private void selectBirthday() {
        long years = 100L * 365 * 1000 * 60 * 60 * 24L;
        birthdayTimePicker = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("出生日期")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - years)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(19)
                .build();
        birthdayTimePicker.show(getSupportFragmentManager(), "year_month_day");
    }

    //  时间选择器回调接口
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        user_birthday = text;             //获取用户生日
        tvBirthday.setText(text);
    }

    //  转化为设置时间格式
    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(time);
        return sf.format(d);
    }


}
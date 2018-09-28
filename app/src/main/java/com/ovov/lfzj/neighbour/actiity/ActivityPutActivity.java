package com.ovov.lfzj.neighbour.actiity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ovov.lfzj.R;

import com.ovov.lfzj.base.BaseActivity;

import com.ovov.lfzj.base.utils.Uri2Pathutil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.neighbour.adapter.GridPopupAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import rx.functions.Action1;

public class ActivityPutActivity extends BaseActivity implements OnDateSetListener {

    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_price)
    EditText mEtPrice;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.put_square_image)
    NoScrollGridView mPutSquareImage;
    private int REQUEST_CODE_CHOOSE = 101;
    private GridPopupAdapter mGridAdapter;
    private File compressedImageFile;
    private TimePickerDialog mDialogAll;      // 日期选择器控件
    private String time;                 // 时间类型标注（开始时间、结束时间）
    List<Uri> list = new ArrayList<Uri>();
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ActivityPutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_put;
    }

    @Override
    public void init() {
        setRightText(R.string.text_put);
        setTitleText(R.string.text_put_activity);
        initgrid();
        addRxBusSubscribe(ClickEvent.class, new Action1<ClickEvent>() {
            @Override
            public void call(ClickEvent clickEvent) {
                RxPermissions rxPermission = new RxPermissions(ActivityPutActivity.this);
                rxPermission.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    setPhoto();
                                }
                            }
                        });
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.lin_start_time, R.id.lin_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.lin_start_time:
                time = "开始时间";
                showDatePicker();
                break;
            case R.id.lin_end_time:
                time = "结束时间";
                showDatePicker();
                break;
        }
    }
    private void setPhoto() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.leFu.fileProvider"))
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_130))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);

    }
    private void initgrid() {
        mGridAdapter = new GridPopupAdapter(this,9);
        mPutSquareImage.setAdapter(mGridAdapter);
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
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            list.clear();
            list.addAll(Matisse.obtainResult(data));
            List<File> listImg = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                //File file = new File("/sdcard",mSelected.get(i).getPath() + ".png");
                ContentResolver contentResolver = getContentResolver();
                String path = Uri2Pathutil.getFromMediaUri(this,contentResolver,list.get(i));
                Log.e("yri", path );
                File file = new File(path);
                try {
                    compressedImageFile = new Compressor(this).compressToFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showGridPhoto(compressedImageFile);
                listImg.add(compressedImageFile);

            }
            for (int i = 0; i < listImg.size(); i++) {

                if (!listImg.get(i).exists()) {
                    try {
//                    listImg.get(i).mkdirs();
                        listImg.get(i).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //presentImage.getUploadImage(toke, listImg.get(i));
                /*MultipartBody.Part part = MultipartBody.Part.createFormData("file", listImg.get(i).getName(), RequestBody.create(null, listImg.get(i)));
                uploadPhoto(part);*/
            }

        }
    }
    public void showGridPhoto(File bmppath) {
        mGridAdapter.setGridImagePath(bmppath);
        //gridView.setAdapter(mGridAdapter);
        mGridAdapter.notifyDataSetChanged();
    }
    //展示时间选择器
    private void showDatePicker() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId(time)
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
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                .setWheelItemTextSize(19)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String timeStr = getDateToString(millseconds);
        if (time.equals("开始时间"))
            mTvStartTime.setText(timeStr);
        else
            mTvEndTime.setText(timeStr);
    }
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }


}

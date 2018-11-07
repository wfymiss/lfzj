package com.ovov.lfzj.home.repair;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.RoomListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.utils.Uri2Pathutil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.RoomListDialog;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.event.DeleteImageEvent;
import com.ovov.lfzj.event.RefreshEvent;
import com.ovov.lfzj.event.RepairSuccessEvent;
import com.ovov.lfzj.event.RoomSelectEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.neighbour.adapter.GridPopupAdapter;
import com.ovov.lfzj.neighbour.square.PutSquareActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;

public class RepairActivity extends BaseActivity {

    @BindView(R.id.tv_family)
    TextView mTvFamily;
    @BindView(R.id.tv_common)
    TextView mTvCommon;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_location)
    EditText mEtLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.gridView)
    NoScrollGridView mGridView;
    @BindView(R.id.repair_category_water)
    TextView mRepairCategoryWater;
    @BindView(R.id.repair_category_electric)
    TextView mRepairCategoryElectric;
    @BindView(R.id.repair_category_gas)
    TextView mRepairCategoryGas;
    @BindView(R.id.repair_category_hot)
    TextView mRepairCategoryHot;
    @BindView(R.id.repair_category_other)
    TextView mRepairCategoryOther;
    @BindView(R.id.re_repair_item)
    LinearLayout mReRepairItem;
    private int repairType = 1;
    private GridPopupAdapter mGridAdapter;
    private int REQUEST_CODE_CHOOSE = 101;
    private MultipartBody.Part part;
    List<MultipartBody.Part> parts = new ArrayList<>();
    private File compressedImageFile;
    List<File> mImage = new ArrayList<>();
    private List<File> listImg;
    List<Uri> list = new ArrayList<Uri>();

    private int TYPE_WATER = 1;
    private int TYPE_ELE = 2;
    private int TYPE_AIR = 3;
    private int TYPE_HOT = 4;
    private int TYPE_OTHER = 5;


    private int AREA_FAMILY = 0;
    private int AREA_COMMON = 1;
    private int area;
    private String house_path;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RepairActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repair;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_property_repair);
        familySelect();
        initgrid();
        mRepairCategoryWater.setSelected(true);
        area = AREA_FAMILY;
        addRxBusSubscribe(RoomSelectEvent.class, new Action1<RoomSelectEvent>() {
            @Override
            public void call(RoomSelectEvent roomSelectEvent) {
                mTvLocation.setText(roomSelectEvent.building_name + "号楼" + roomSelectEvent.unit + "单元" + roomSelectEvent.name);
                house_path = roomSelectEvent.building_name + "-" + roomSelectEvent.unit + "-" + roomSelectEvent.name;
            }
        });
        addRxBusSubscribe(RepairSuccessEvent.class, new Action1<RepairSuccessEvent>() {
            @Override
            public void call(RepairSuccessEvent repairSuccessEvent) {
                finish();
            }
        });
    }

    private void initgrid() {
        mGridAdapter = new GridPopupAdapter(this, 9);
        mGridView.setAdapter(mGridAdapter);

        addRxBusSubscribe(ClickEvent.class, new Action1<ClickEvent>() {
            @Override
            public void call(ClickEvent clickEvent) {
                RxPermissions rxPermission = new RxPermissions(RepairActivity.this);
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
        addRxBusSubscribe(DeleteImageEvent.class, new Action1<DeleteImageEvent>() {
            @Override
            public void call(DeleteImageEvent deleteImageEvent) {
                mImage.remove(deleteImageEvent.posistion);
            }
        });
    }

    private void setPhoto() {

        int size = mImage.size() >= 9 ? 9 : 9 - mImage.size();
        Matisse.from(this)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.leFu.fileProvider"))
                .countable(true)
                .maxSelectable(size)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_130))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);

    }

    private void familySelect() {
        mTvFamily.setSelected(true);
        mTvFamily.setTextColor(getResources().getColor(R.color.color_ffffff));
        mTvCommon.setSelected(false);
        mTvCommon.setTextColor(getResources().getColor(R.color.colorPrimary));
        mEtLocation.setVisibility(View.GONE);
        mTvLocation.setVisibility(View.VISIBLE);
        mReRepairItem.setVisibility(View.VISIBLE);
        area = AREA_FAMILY;
    }

    private void commonSelect() {
        mTvFamily.setSelected(false);
        mTvFamily.setTextColor(getResources().getColor(R.color.colorPrimary));
        mTvCommon.setSelected(true);
        mTvCommon.setTextColor(getResources().getColor(R.color.color_ffffff));
        mEtLocation.setVisibility(View.VISIBLE);
        mTvLocation.setVisibility(View.GONE);
        mReRepairItem.setVisibility(View.GONE);
        area = AREA_COMMON;
    }

    @OnClick({R.id.iv_back, R.id.tv_family, R.id.tv_common, R.id.tv_location, R.id.tv_commit, R.id.repair_category_water, R.id.repair_category_electric, R.id.repair_category_gas, R.id.repair_category_hot, R.id.repair_category_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_family:
                familySelect();
                break;
            case R.id.tv_common:
                commonSelect();
                break;
            case R.id.tv_location:
                getUserHouse();
                break;
            case R.id.tv_commit:
                if (TextUtils.isEmpty(mEtName.getText().toString()) && TextUtils.isEmpty(mEtPhone.getText().toString()) && TextUtils.isEmpty(mEtContent.getText().toString())) {
                    showToast(R.string.text_input_all_message);
                    return;
                }
                if (area == AREA_FAMILY && TextUtils.isEmpty(mTvLocation.getText())) {
                    showToast("请选择房间");
                    return;
                }
                if (area == AREA_COMMON && TextUtils.isEmpty(mEtLocation.getText().toString())) {
                    showToast("请填写位置");
                    return;
                }
                if (RegexUtils.isMobile(mEtPhone.getText().toString().trim()) != RegexUtils.VERIFY_SUCCESS) {
                    showToast("请输入正确的手机号码");
                    return;
                }
                RepairContent repairContent = new RepairContent();
                repairContent.setContent(mEtContent.getText().toString());
                repairContent.setmGrid(mImage);
                repairContent.setMobile(mEtPhone.getText().toString());
                repairContent.setName(mEtName.getText().toString());
                if (area == AREA_FAMILY) {
                    repairContent.setRepairArea("家庭区域");
                    repairContent.setRepairLocation(mTvLocation.getText().toString());
                    repairContent.setRepairType(repairType);
                    repairContent.setAreaType(AREA_FAMILY);
                    repairContent.setHouse_path(house_path);
                }
                if (area == AREA_COMMON) {
                    repairContent.setRepairArea("公共设施");
                    repairContent.setRepairLocation(mEtLocation.getText().toString());
                    repairContent.setAreaType(AREA_COMMON);
                }
                WorkOrderConfirmActivity.toActivity(mActivity, repairContent);
                break;
            case R.id.repair_category_water:
                mRepairCategoryWater.setSelected(true);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                repairType = TYPE_WATER;
                break;
            case R.id.repair_category_electric:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(true);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                repairType = TYPE_ELE;
                break;
            case R.id.repair_category_gas:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(true);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                repairType = TYPE_AIR;
                break;
            case R.id.repair_category_hot:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(true);
                mRepairCategoryOther.setSelected(false);
                repairType = TYPE_HOT;
                break;
            case R.id.repair_category_other:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(true);
                repairType = TYPE_OTHER;
                break;
        }
    }

    private void getUserHouse() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getUserHouse()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<RoomListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ListInfo<RoomListInfo> dataInfo) {
                        RoomListDialog roomListDialog = new RoomListDialog(mActivity, dataInfo.datas());
                        roomListDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.5));
                        roomListDialog.show();
                        roomListDialog.setData(dataInfo.datas());
                        dismiss();
                    }
                });
        addSubscrebe(subscription);
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
            listImg = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                //File file = new File("/sdcard",mSelected.get(i).getPath() + ".png");
                ContentResolver contentResolver = getContentResolver();
                String path = Uri2Pathutil.getFromMediaUri(this, contentResolver, list.get(i));

                if (path != null && !path.equals("") && !getAutoFileOrFilesSize(path).equals("0B")) {
                    File file = new File(path);
                    Log.e("sizeeee",getAutoFileOrFilesSize(path));
                    try {
                        compressedImageFile = new Compressor(this).compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showGridPhoto(compressedImageFile);
                    listImg.add(compressedImageFile);
                } else {
                    showToast("您选择的图片包含已删除图片，请重新选择");
                }

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
                /*part = MultipartBody.Part.createFormData("img"+i, listImg.get(i).getName(), RequestBody.create(null, listImg.get(i)));
                parts.add(part);*/
                //uploadPhoto(part);
                mImage.add(listImg.get(i));

            }

        }
    }

    public void showGridPhoto(File bmppath) {
        mGridAdapter.setGridImagePath(bmppath);
        //gridView.setAdapter(mGridAdapter);
        mGridAdapter.notifyDataSetChanged();
    }

    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


}

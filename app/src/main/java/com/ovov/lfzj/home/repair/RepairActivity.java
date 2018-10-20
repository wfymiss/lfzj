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
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.Uri2Pathutil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.event.DeleteImageEvent;
import com.ovov.lfzj.event.RefreshEvent;
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
import java.io.IOException;
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
    private int flag = 1;
    private GridPopupAdapter mGridAdapter;
    private int REQUEST_CODE_CHOOSE = 101;
    private MultipartBody.Part part;
    List<MultipartBody.Part> parts = new ArrayList<>();
    private File compressedImageFile;
    List<File> mImage = new ArrayList<>();
    private List<File> listImg;
    List<Uri> list = new ArrayList<Uri>();
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

    private void familySelect() {
        mTvFamily.setSelected(true);
        mTvFamily.setTextColor(getResources().getColor(R.color.color_ffffff));
        mTvCommon.setSelected(false);
        mTvCommon.setTextColor(getResources().getColor(R.color.colorPrimary));
        mEtLocation.setVisibility(View.GONE);
        mTvLocation.setVisibility(View.VISIBLE);
        mReRepairItem.setVisibility(View.VISIBLE);
    }

    private void commonSelect() {
        mTvFamily.setSelected(false);
        mTvFamily.setTextColor(getResources().getColor(R.color.colorPrimary));
        mTvCommon.setSelected(true);
        mTvCommon.setTextColor(getResources().getColor(R.color.color_ffffff));
        mEtLocation.setVisibility(View.VISIBLE);
        mTvLocation.setVisibility(View.GONE);
        mReRepairItem.setVisibility(View.GONE);
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
                break;
            case R.id.tv_commit:
                if (!TextUtils.isEmpty(mEtName.getText()) && !TextUtils.isEmpty(mEtPhone.getText()) && !TextUtils.isEmpty(mEtLocation.getText())) {

                    if (!TextUtils.isEmpty(mEtContent.getText())) {

                   //     addNeighbour();



                    } else {
                        showToast("请描述故障情况");
                        return;
                    }

                } else {
                    showToast("请完善资料");
                    return;
                }
                WorkOrderConfirmActivity.toActivity(mActivity);
                break;
            case R.id.repair_category_water:
                mRepairCategoryWater.setSelected(true);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                flag = 1;
                break;
            case R.id.repair_category_electric:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(true);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                flag = 2;
                break;
            case R.id.repair_category_gas:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(true);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                flag = 3;
                break;
            case R.id.repair_category_hot:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(true);
                mRepairCategoryOther.setSelected(false);
                flag = 4;
                break;
            case R.id.repair_category_other:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(true);
                flag = 5;
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
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            list.clear();
            list.addAll(Matisse.obtainResult(data));
            listImg = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                //File file = new File("/sdcard",mSelected.get(i).getPath() + ".png");
                ContentResolver contentResolver = getContentResolver();
                String path = Uri2Pathutil.getFromMediaUri(this, contentResolver, list.get(i));
                Log.e("yri", path);
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


//    private void addNeighbour() {
//        // RequestBody firstBody = RequestBody.create(MediaType.parse("multipart/form-data"), "img");
//        RequestBody mContent = RequestBody.create(MediaType.parse("multipart/form-data"), mEtContent.getText().toString());
//        /*if (listImg.size() > 0)
//            part = MultipartBody.Part.createFormData("img", listImg.get(0).getName(), RequestBody.create(null, listImg.get(0)));*/
//        for (int i = 0; i<mImage.size();i++){
//            part = MultipartBody.Part.createFormData("img"+i, mImage.get(i).getName(), RequestBody.create(null, mImage.get(i)));
//            parts.add(part);
//        }
//
//        showLoadingDialog();
//        Subscription subscription = RetrofitHelper.getInstance().addNeighbour(mContent, parts)
//                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
//                .subscribe(new CommonSubscriber<DataInfo>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        dismiss();
//                        if (e instanceof DataResultException) {
//                            DataResultException dataResultException = (DataResultException) e;
//                            showToast(dataResultException.errorInfo);
//                        } else {
//
//                            doFailed();
//                            showError(e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onNext(DataInfo dataInfo) {
//                        dismiss();
//                        showToast(R.string.text_put_success);
//                        RxBus.getDefault().post(new RefreshEvent());
//                        finish();
//                    }
//                });
//        addSubscrebe(subscription);
//    }
}

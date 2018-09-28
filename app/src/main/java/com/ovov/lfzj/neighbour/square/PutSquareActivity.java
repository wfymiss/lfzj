package com.ovov.lfzj.neighbour.square;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ovov.lfzj.R;

import com.ovov.lfzj.base.BaseActivity;

import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.Uri2Pathutil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.event.RefreshEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.neighbour.adapter.GridPopupAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;

public class PutSquareActivity extends BaseActivity {


    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.put_square_image)
    NoScrollGridView mPutSquareImage;
    private int REQUEST_CODE_CHOOSE = 101;
    private GridPopupAdapter mGridAdapter;
    private File compressedImageFile;
    List<Uri> list = new ArrayList<Uri>();
    private List<File> listImg;
    private MultipartBody.Part part;
    private byte[] img;
    private Bitmap bitmap;
    private byte[] byteArray;
    List<MultipartBody.Part> parts = new ArrayList<>();
    List<File> mImage = new ArrayList<>();
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, PutSquareActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_put_square;
    }

    @Override
    public void init() {

        setTitleText(R.string.text_put_square);
        setRightText(R.string.text_put);
        initgrid();
        addRxBusSubscribe(ClickEvent.class, new Action1<ClickEvent>() {
            @Override
            public void call(ClickEvent clickEvent) {
                RxPermissions rxPermission = new RxPermissions(PutSquareActivity.this);
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
        mGridAdapter = new GridPopupAdapter(this, 9);
        mPutSquareImage.setAdapter(mGridAdapter);
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(mEtContent.getText().toString())) {
                    showToast(R.string.please_input_content);
                    return;
                }
                addNeighbour();
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

    private void addNeighbour() {
        // RequestBody firstBody = RequestBody.create(MediaType.parse("multipart/form-data"), "img");
        RequestBody mContent = RequestBody.create(MediaType.parse("multipart/form-data"), mEtContent.getText().toString());
        /*if (listImg.size() > 0)
            part = MultipartBody.Part.createFormData("img", listImg.get(0).getName(), RequestBody.create(null, listImg.get(0)));*/
        for (int i = 0; i<mImage.size();i++){
            part = MultipartBody.Part.createFormData("img"+i, mImage.get(i).getName(), RequestBody.create(null, mImage.get(i)));
            parts.add(part);
        }

        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().addNeighbour(mContent, parts)
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
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast(R.string.text_put_success);
                        RxBus.getDefault().post(new RefreshEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }



}

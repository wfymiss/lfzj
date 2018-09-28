package com.ovov.lfzj.user.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.prensenter.ActivityUpImagePresent;
import com.ovov.lfzj.base.utils.Uri2Pathutil;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.user.adapter.GridPopupAdapter;
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
import rx.functions.Action1;

public class EvaluateActivity extends BaseActivity {

    @BindView(R.id.tv_right)
    TextView tvRight;
    private GridView gridView;
    private GridPopupAdapter adapter;
    private Toolbar mtoobar;
    private int REQUEST_CODE_CHOOSE = 101;
    List<Uri> list = new ArrayList<Uri>();
    private File compressedImageFile;
    private ActivityUpImagePresent presenterImage;
//    @BindView(R.id.tv_send)
//    TextView mSend;


    private void initGridView() {
        //设置图片可发布最大数量
        gridView = (GridView) findViewById(R.id.gv);
        adapter = new GridPopupAdapter(this, 3);
        gridView.setAdapter(adapter);
        gridView.setOverScrollMode(View.OVER_SCROLL_NEVER); // 不显示滑动到最后的边框
        //设置监听
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EvaluateActivity.this, "3秒后删除", Toast.LENGTH_SHORT).show();
                adapter.setDelectPath(i);
                return true;
            }
        });
    }

    private void setOnclik() {
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

                        }
                    }
                });
        addRxBusSubscribe(ClickEvent.class, new Action1<ClickEvent>() {
            @Override
            public void call(ClickEvent clickEvent) {
                setPhoto();
            }
        });
    }

    private void setPhoto() {
        Matisse.from(EvaluateActivity.this)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.leFu.fileProvider"))
                .countable(true)
                .maxSelectable(3)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_130))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);

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
                ContentResolver contentResolver = getContentResolver();
                String path = Uri2Pathutil.getFromMediaUri(this, contentResolver, list.get(i));
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
                //     presenterImage.getUploadImage(token, listImg.get(i));

                /*MultipartBody.Part part = MultipartBody.Part.createFormData("file", listImg.get(i).getName(), RequestBody.create(null, listImg.get(i)));
                uploadPhoto(part);*/
            }

        }
    }


    /**
     * 添加图片到适配器中
     *
     * @param bmppath 要添加的图片
     */
    public void showGridPhoto(File bmppath) {
        adapter.setGridImagePath(bmppath);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void init() {
        tvRight.setText("发送");
        initGridView();
        // 接口回调
        setOnclik();
    }


    @OnClick(R.id.tv_right)
    public void onViewClicked() {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}

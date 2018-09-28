//package com.ovov.lfzj.home.ui;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.github.chrisbanes.photoview.OnPhotoTapListener;
//import com.github.chrisbanes.photoview.PhotoView;
//import com.github.chrisbanes.photoview.PhotoViewAttacher;
//import com.ovov.lfzj.R;
//import com.ovov.lfzj.base.BaseActivity;
//import com.squareup.picasso.Picasso;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class ImageDetailActivity extends BaseActivity {
//
//
//    @BindView(R.id.photoview)
//    PhotoView photoview;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_image_detail;
//    }
//
//    @Override
//    public void init() {
//        String id = getIntent().getExtras().getString("id");
//        Picasso.with(this).load(id).into(photoview);
//        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoview);
//        photoViewAttacher.update();
//        photoview.setOnPhotoTapListener(new OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(ImageView view, float x, float y) {
//                finish();
//            }
//        });
//
//    }
//
//
//}

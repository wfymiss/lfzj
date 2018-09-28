package com.ovov.lfzj.opendoor.capture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.DocumentUtil;
import com.ovov.lfzj.opendoor.decode.DecodeListener;
import com.ovov.lfzj.opendoor.decode.DecodeThread;
import com.ovov.lfzj.opendoor.decode.LuminanceSource;
import com.ovov.lfzj.opendoor.decode.PlanarYUVLuminanceSource;
import com.ovov.lfzj.opendoor.decode.RGBLuminanceSource;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码扫描
 */
public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, PreviewFrameShotListener, DecodeListener,
        OnCheckedChangeListener, OnClickListener {

    private static final long VIBRATE_DURATION = 200L;
    private static final int REQUEST_CODE_ALBUM = 0;
    public static final String EXTRA_RESULT = "result";
    public static final String EXTRA_BITMAP = "bitmap";

    private SurfaceView previewSv;
    private CaptureView captureView;
    private CheckBox flashCb;
    private ImageButton backBtn;
    private Button albumBtn;

    private CameraManager mCameraManager;
    private DecodeThread mDecodeThread;
    private Rect previewFrameRect = null;
    private boolean isDecoding = false;

    public void toActivity(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    public void init() {
        previewSv = (SurfaceView) findViewById(R.id.sv_preview);
        captureView = (CaptureView) findViewById(R.id.cv_capture);
        flashCb = (CheckBox) findViewById(R.id.cb_capture_flash);
        flashCb.setOnCheckedChangeListener(this);
        flashCb.setEnabled(false);
        albumBtn = (Button) findViewById(R.id.btn_album);
        albumBtn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            albumBtn.setVisibility(View.GONE);
        }
        previewSv.getHolder().addCallback(this);
        mCameraManager = new CameraManager(this);
        mCameraManager.setPreviewFrameShotListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCameraManager.initCamera(holder);
        if (!mCameraManager.isCameraAvailable()) {
            Toast.makeText(CaptureActivity.this, "失败哦", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (mCameraManager.isFlashlightAvailable()) {
            flashCb.setEnabled(true);
        }
        mCameraManager.startPreview();
        if (!isDecoding) {
            mCameraManager.requestPreviewFrameShot();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        mCameraManager.release();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPreviewFrame(byte[] data, Size dataSize) {
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        if (previewFrameRect == null) {
            previewFrameRect = mCameraManager.getPreviewFrameRect(captureView.getFrameRect());
        }
        PlanarYUVLuminanceSource luminanceSource = new PlanarYUVLuminanceSource(data, dataSize, previewFrameRect);
        mDecodeThread = new DecodeThread(luminanceSource, CaptureActivity.this);
        isDecoding = true;
        mDecodeThread.execute();
    }

    @Override
    public void onDecodeSuccess(Result result, LuminanceSource source, Bitmap bitmap) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
        isDecoding = false;
        if (bitmap.getWidth() > 100 || bitmap.getHeight() > 100) {
            Matrix matrix = new Matrix();
            matrix.postScale(100f / bitmap.getWidth(), 100f / bitmap.getHeight());
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = resizeBmp;
        }
        Intent resultData = new Intent();
        resultData.putExtra(EXTRA_RESULT, result.getText());
        resultData.putExtra(EXTRA_BITMAP, bitmap);
        setResult(RESULT_OK, resultData);
        finish();
    }

    @Override
    public void onDecodeFailed(LuminanceSource source) {
        if (source instanceof RGBLuminanceSource) {
            Toast.makeText(CaptureActivity.this, "失败", Toast.LENGTH_SHORT).show();
        }
        isDecoding = false;
        mCameraManager.requestPreviewFrameShot();
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        captureView.addPossibleResultPoint(point);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mCameraManager.enableFlashlight();
        } else {
            mCameraManager.disableFlashlight();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_album:
                Intent intent = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
                break;
            case R.id.iv_back:

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK && data != null) {
            Bitmap cameraBitmap = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String path = DocumentUtil.getPath(CaptureActivity.this, data.getData());
                cameraBitmap = DocumentUtil.getBitmap(path);
            } else {
                // Not supported in SDK lower that KitKat
            }
            if (cameraBitmap != null) {
                if (mDecodeThread != null) {
                    mDecodeThread.cancel();
                }
                int width = cameraBitmap.getWidth();
                int height = cameraBitmap.getHeight();
                int[] pixels = new int[width * height];
                cameraBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                RGBLuminanceSource luminanceSource = new RGBLuminanceSource(pixels, new Size(width, height));
                mDecodeThread = new DecodeThread(luminanceSource, CaptureActivity.this);
                isDecoding = true;
                mDecodeThread.execute();
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        setTransitionAnimation(true);
    }

    private void setTransitionAnimation(boolean isNewActivity) {
        if (isNewActivity) {
            // 新页面从右边进入
            overridePendingTransition(R.anim.anim_open_up, R.anim.anim_open_exit);
        } else {
            // 回到上个页面
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }



    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

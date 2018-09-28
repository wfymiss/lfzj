package com.ovov.lfzj.home.repair.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *  工单视频录像——surfaceView
 * Created by lyy on 2017/7/31.
 */

public class VideoRecordActivity extends BaseActivity {
    private Unbinder unbinder;
    private static final String TAG = "VideoRecordActivity";
    public static final int CONTROL_CODE = 1;
    //视图界面
    @BindView(R.id.record_toolbar)
    Toolbar toolbar;
    @BindView(R.id.record_control)
    ImageView mRecordControl;       //开始
    @BindView(R.id.record_pause)
    ImageView mPauseRecord;         // 暂停
    @BindView(R.id.record_time)
    Chronometer mRecordTime;       // 录制时间
    @BindView(R.id.record_surfaceView)
    SurfaceView surfaceView;

    private SurfaceHolder mSurfaceHolder;    // 录像支持实例化
    private boolean isRecording;// 标记，判断当前是否正在录制
    private boolean isPause; // 暂停标识
    private long mPauseTime=0;           //录制暂停时间间隔
    // 存储文件
//    private File mVecordFile;
//    private String saveVideoPath = "";     // 暂停缓存传值
    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private String currentVideoFilePath;

    private Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<VideoRecordActivity> mActivity;

        public MyHandler(VideoRecordActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case CONTROL_CODE:
                    //开启按钮
                    mActivity.get().mRecordControl.setEnabled(true);
                    break;
            }
        }
    }

    private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();       //释放资源
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_video_record;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_video_record);
        initView();    //  获取控件
    }

    private void initView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回的导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);  // 不显示子标题
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPauseRecord.setEnabled(false);     //暂停按钮

        //配置SurfaceHodler
        mSurfaceHolder = surfaceView.getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(mCallBack);//回调接口
    }

    // 设置音频接口
    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override   // 当surfaceHolder 被创建时回调
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initCamera();
        }

        @Override     // 当surfaceHolder 尺寸变化时回调
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (mSurfaceHolder.getSurface() == null) {
                return;
            }
        }

        @Override    // 当surfaceHolder 被销毁时回调
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            stopCamera();    // 关闭摄像头，释放资源
        }
    };

    /**
     * 初始化摄像头
     *
     * @throws IOException
     * @author liuzhongjun
     * @date 2016-3-16
     */
    private void initCamera() {
        if (mCamera != null) {
            stopCamera();     // 关闭摄像头，释放资源
        }
        //默认启动后置摄像头
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (mCamera == null) {
            Toast.makeText(this, "未能获取到相机！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            setCameraParams();
            //启动相机预览
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 设置摄像头为竖屏
     *
     * @author lip
     * @date 2015-3-16
     */
    private void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            //设置相机的横竖屏(竖屏需要旋转90°)
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                params.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
            } else {
                params.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
            }
            //设置聚焦模式
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            //缩短Recording启动时间
            params.setRecordingHint(true);
            //影像稳定能力
            if (params.isVideoStabilizationSupported())
                params.setVideoStabilization(true);
            mCamera.setParameters(params);
        }
    }

    /**
     * 释放摄像头资源
     *
     * @author liuzhongjun
     * @date 2016-2-5
     */
    private void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始录制视频
     */
    public void startRecord() {
        initCamera();      //  初始化摄像头
        mCamera.unlock();
        setConfigRecord();     //  配置对媒体设备
        try {
            //开始录制
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRecording = true;     // 标记正在录制
        if (mPauseTime != 0) {
            mRecordTime.setBase(SystemClock.elapsedRealtime() - (mPauseTime - mRecordTime.getBase()));
        } else {
            mRecordTime.setBase(SystemClock.elapsedRealtime());
        }
        mRecordTime.start();
    }

    /**
     * 停止录制视频
     */
    public void stopRecord() {
        if (isRecording && mediaRecorder != null) {
            // 设置后不会崩
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setPreviewDisplay(null);
            //停止录制
            mediaRecorder.stop();
            mediaRecorder.reset();
            //释放资源
            mediaRecorder.release();
            mediaRecorder = null;

            mRecordTime.stop();
/*            //设置开始按钮可点击，停止按钮不可点击
            mRecordControl.setEnabled(true);
            mPauseRecord.setEnabled(false);*/
            isRecording = false;
        }
    }

    // surfaceView 页面点击事件
    @OnClick({R.id.record_control, R.id.record_pause})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_control:
                if (isPause) {        //如果暂停，则继续录制视频
                    mPauseRecord.setImageResource(R.mipmap.recordvideo_stop);   //显示停止按钮
                    if (mPauseTime != 0) {
                        mRecordTime.setBase(SystemClock.elapsedRealtime() - (mPauseTime - mRecordTime.getBase()));
                    } else {
                        mRecordTime.setBase(SystemClock.elapsedRealtime());
                    }
                    mRecordTime.start();
                    //继续视频录制
                    startRecord();
                    isPause = false;
                } else {
                    if (!isRecording) {    // 标记 录制为false
                        //开始录制视频
                        startRecord();
                        mRecordControl.setImageResource(R.mipmap.recordvideo_stop);   // 暂停按钮
                        mRecordControl.setEnabled(false);//1s后才能停止
                        mHandler.sendEmptyMessageDelayed(CONTROL_CODE, 1000);
                        mPauseRecord.setVisibility(View.VISIBLE);      // 显示暂停按钮
                        mPauseRecord.setEnabled(true);
                    } else {
                        //停止视频录制
                        mRecordControl.setImageResource(R.mipmap.recordvideo_start);
                        mPauseRecord.setVisibility(View.GONE);
                        mPauseRecord.setEnabled(false);
                        stopRecord();     // 停止录制
                        mCamera.lock();
                        stopCamera();    //释放摄像头资源
                        mRecordTime.stop();   // 计时停止
                        mPauseTime = 0;       // 时间间隔归零
                        final AlertDialog.Builder builder=new AlertDialog.Builder(this);       //关闭录像提醒
                        builder.setTitle("是否要保存视频");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 返回工单页
                                Intent intent=new Intent();
                                Bundle bundle=new Bundle();
                                bundle.putString("videoPath",currentVideoFilePath);   // 视频地址 （跳转）
                                intent.putExtras(bundle);
                                VideoRecordActivity.this.setResult(2,intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (currentVideoFilePath!=null){
                                    File videoPath=new File(currentVideoFilePath);
                                    videoPath.delete();
                                }
                                Intent intent=new Intent(VideoRecordActivity.this,RepairActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                }
                break;
            case R.id.record_pause:
                if (isRecording) {   //正在录制的视频，点击后暂停
                    mPauseRecord.setImageResource(R.mipmap.recordvideo_start);
                    //暂停视频录制
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success == true)
                                VideoRecordActivity.this.mCamera.cancelAutoFocus();
                        }
                    });
                    stopRecord();    //  停止录制
                    mRecordTime.stop();
                    isPause = true;

//                    if (saveVideoPath.equals("")) {
//                        saveVideoPath = currentVideoFilePath;
//                    }

                } else {
                    mPauseRecord.setImageResource(R.mipmap.recordvideo_stop);
                    if (mPauseTime != 0) {
                        mRecordTime.setBase(SystemClock.elapsedRealtime() - (mPauseTime - mRecordTime.getBase()));
                    } else {
                        mRecordTime.setBase(SystemClock.elapsedRealtime());
                    }
                    mRecordTime.start();
                    //继续视频录制
                    startRecord();
                    isPause = false;
                }
                break;
            default:
        }
    }

    /**
     * 配置MediaRecorder()
     */
    private void setConfigRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOnErrorListener(OnErrorListener);

        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 2 * 1024 * 1024)
            mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        else
            mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mediaRecorder.setOrientationHint(90);
        //设置录像的分辨率
        mediaRecorder.setVideoSize(352, 288);
        //设置录像视频保存地址 (命名）
        currentVideoFilePath = getSDPath(getApplicationContext()) + getVideoName();
        mediaRecorder.setOutputFile(currentVideoFilePath);
    }

    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else if (!sdCardExist) {

            Toast.makeText(context, "SD卡不存在", Toast.LENGTH_SHORT).show();

        }
        File eis = new File(sdDir.toString() + "/lefulyy/video/");
        try {
            if (!eis.exists()) {
                eis.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdDir.toString() + "/lefulyy/video/";
    }

    // 给录制的是视频命名
    private String getVideoName() {
        return "orderVideo" +  ".mp4";
    }
}

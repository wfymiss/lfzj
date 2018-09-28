package com.ovov.lfzj.home.repair.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 工单视频播放surface 界面
 * 凯特联众  lyy   2017/0812
 */
public class VideoPlayerActivity extends BaseActivity implements SurfaceHolder.Callback{
    private Unbinder unbinder;
    @BindView(R.id.video_start_toolbar)
    Toolbar toolbar;
    @BindView(R.id.video_start_surfaceView)
    SurfaceView surfaceView;
//    @BindView(R.id.video_start_time)
//    Chronometer time;
    @BindView(R.id.video_start_player)
ImageView player;
    private MediaPlayer mediaPlayer;    //播放管理器
    private SurfaceHolder mSurfaceHolder;
    private String videoPathstart=null;  //录像路径
    private long videoTime;   //录像时间
    private boolean isPlayer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_video_player;
    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        initToolbar();  //标题

        Intent intent=getIntent();   // 获取值
        videoPathstart=intent.getStringExtra("videostart");   //播放路径
        videoTime= Long.parseLong(intent.getStringExtra("videoTime"));   //播放时间
    }

    // 显示标题
    private void initToolbar() {
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //显示导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);   //隐藏小标题
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //返回
            }
        });
    }

    @OnClick({R.id.video_start_player})
    public void onClickListener(View view){
        switch (view.getId()){
            case R.id.video_start_player:
                if (isPlayer){
                    stopPlayer(); //停止播放视频
                    Toast.makeText(this,"视频停止播放", Toast.LENGTH_SHORT).show();
                }else {
                    videoPlayer();  //播放视频
                    Toast.makeText(this,"视频开始播放", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    /**
     * 播放视频
     */
    public void videoPlayer() {
        if (mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
        mediaPlayer.reset();   //重置
        try {
            mediaPlayer.setDataSource(this, Uri.parse(videoPathstart));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        mSurfaceHolder=surfaceView.getHolder();    //给surfaceHolder 添加监听事件
        // 设置播放时打开屏幕
//        surfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  // 播放指定Buffer类型
        isPlayer=true;
    }

    public void stopPlayer(){
        if (isPlayer&&mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
            isPlayer=false;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder=holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder=holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceView=null;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

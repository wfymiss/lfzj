package com.ovov.lfzj.opendoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.kyleduo.switchbutton.SwitchButton;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  智能门禁开关声音设置
 */
public class OpenSettingActivity extends BaseActivity {
    @BindView(R.id.sound_control)
    SwitchButton soundControl;      // 声音控制
    @BindView(R.id.shake_control)
    SwitchButton shakeControl;      // 震动控制

    private boolean sound=true;
    private boolean shake=true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_setting;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_opendoor_setting);
        initControl();    // 查看设置信息
    }

    // 查看设置信息
    private void initControl() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        sound=spf.getBoolean("soundControl",true);
        shake=spf.getBoolean("shakeControl",true);
        if (sound){
            soundControl.setChecked(true);
        }else {
            soundControl.setChecked(false);
        }
        if (shake){
            shakeControl.setChecked(true);
        }else {
            shakeControl.setChecked(false);
        }
    }

    @OnClick({R.id.sound_control, R.id.shake_control,R.id.iv_back})
    public void OnClickListener(View view){
        switch (view.getId()){
            case R.id.sound_control:
                if (soundControl.isChecked()){
                    Log.i("hhh","ssssssssss");
                    sound=true;
                }else {
                    sound=false;
                }
                break;
            case R.id.shake_control:
                if (shakeControl.isChecked()){
                    shake=true;
                }else {
                    shake=false;
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }

    // 保存设置信息
    private void safeControlInfo() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("soundControl",sound);
        editor.putBoolean("shakeControl",shake);
        Log.i("hhh","fffff"+sound);
        Log.i("hhh","rrrrrrrr"+shake);
        editor.commit();
    }

    /**
     *  返回时存储设置类型
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                safeControlInfo();    // 保存设置信息
                finish();
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }




}

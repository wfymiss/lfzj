package com.ovov.lfzj.opendoor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.widget.WaveView;

public class DemoActivity extends Activity {
    WaveView wave;
    ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_demo);
//        wave = (WaveView) findViewById(R.id.wave);
//        head = (ImageView) findViewById(R.id.head);
//        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DemoActivity.this, "哈哈", Toast.LENGTH_SHORT).show();
//            }
//        });
//        head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    wave.start();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        wave.setImageRadius(head.getWidth() / 2);
//    }
}}
package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

public class WorkerOrderDetailActivity extends BaseActivity {

    public static void toActivity(Context context){
        Intent intent = new Intent(context,WorkerOrderDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_worker_order_detail;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_worker_order_detail);
    }
}

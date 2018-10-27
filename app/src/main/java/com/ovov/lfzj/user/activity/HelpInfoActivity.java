package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.tv_title_que)
    TextView tvTitleQue;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_kefu)
    TextView tvKefu;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_a_content)
    TextView tvAContent;


    public static void toActivity(Context context, String time, String q, String a, String ke) {
        Intent intent = new Intent(context, HelpInfoActivity.class);
        intent.putExtra("time", time);
        intent.putExtra("question", q);
        intent.putExtra("aw", a);
        intent.putExtra("reply_time", ke);
        ;
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_qusetion_list;
    }

    @Override
    public void init() {
        String time = getIntent().getExtras().get("time").toString();
        String question = getIntent().getExtras().get("question").toString();
        String aw = getIntent().getExtras().get("aw").toString();
        String reply_time = getIntent().getExtras().get("reply_time").toString();
        tvTitle.setText("您的反馈");
        layout.setVisibility(View.VISIBLE);
        tvTitleQue.setText("我");


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

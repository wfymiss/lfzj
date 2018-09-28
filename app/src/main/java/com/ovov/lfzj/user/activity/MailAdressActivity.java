package com.ovov.lfzj.user.activity;

import android.content.Intent;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

public class MailAdressActivity extends BaseActivity {
//    @BindView(R.id.tv_jilu)
//    TextView mJilu;
//    @BindView(R.id.btn_order)
//    Button mOrder;
//    @BindView(R.id.hmm_com_toolbar)
//    Toolbar mToolbar;

    protected void initView() {
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public int getLayoutId() {
        return R.layout.bbs_message_item;
    }

    @Override
    public void init() {

    }

//    @OnClick({R.id.tv_jilu, R.id.btn_order})
//    public void setOnclick(View view) {
//        Intent intent;
//        if (view.getId() == R.id.tv_jilu) {
//            intent = new Intent(this, MailActivity.class);
//            startActivity(intent);
//        }else {
//            intent = new Intent(this, OrderGoodsActivity.class);
//            startActivity(intent);
//        }
//    }
}

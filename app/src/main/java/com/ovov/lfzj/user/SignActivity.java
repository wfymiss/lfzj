package com.ovov.lfzj.user;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.ReviseNicknameEvent;
import com.ovov.lfzj.event.ReviseSignEvent;

import butterknife.BindView;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {
    @BindView(R.id.neekname_et)
    EditText neeknameEt;


    public static void toActivity(Context context){
        Intent intent = new Intent(context,SignActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    public void init() {

        setTitleText("修改签名");
        setRightText("保存");
        neeknameEt.setHint("请输入签名");
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(neeknameEt.getText().toString())){
                    showToast("请输入签名");
                    return;
                }
                RxBus.getDefault().post(new ReviseSignEvent(neeknameEt.getText().toString()));
                finish();
                break;
        }
    }


}

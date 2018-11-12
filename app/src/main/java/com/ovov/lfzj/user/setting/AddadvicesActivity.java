package com.ovov.lfzj.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.AdviceDialog;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.base.bean.UrlBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class AddadvicesActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right_but)
    TextView tvRight;
    @BindView(R.id.edit)
    EditText edit;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, AddadvicesActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_addadvices;
    }

    @Override
    public void init() {

        tvTitle.setText("意见反馈");
        addRxBusSubscribe(IdentityEvent.class, new Action1<IdentityEvent>() {
            @Override
            public void call(IdentityEvent identityEvent) {
                finish();
            }
        });

    }


    @OnClick({R.id.iv_back, R.id.tv_right_but})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_but:

                if (!TextUtils.isEmpty(edit.getText().toString()) && !edit.getText().toString().equals("")) {
                    if (edit.getText().toString().trim().length() < 50) {
                        String data = edit.getText().toString();
                        updata(data);
                    } else {
                        showToast("字数不能超过50哦");
                        return;
                    }


                } else {
                    showToast("请输入意见");
                }

                break;
        }
    }

    private void updata(String data) {
        Subscription subscription = RetrofitHelper.getInstance().Upload(data)
                .compose(RxUtil.<DataInfo<UrlBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<UrlBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(DataInfo<UrlBean> listInfoDataInfo) {
                        AdviceDialog adviceDialog = new AdviceDialog(mActivity);
                        adviceDialog.show();


                    }

                });
        addSubscrebe(subscription);
    }

}

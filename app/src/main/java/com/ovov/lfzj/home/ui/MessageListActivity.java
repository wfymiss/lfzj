package com.ovov.lfzj.home.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    public void init() {
        tvTitle.setText("消息通知");
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.list_serch);

        initList();
        geitData();


    }

    private void geitData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().isLogin()
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(R.string.text_login_invaild);
                            LoginUserBean.getInstance().reset();
                            LoginUserBean.getInstance().save();
                            LoginActivity.toActivity(mActivity);
                            mActivity.finish();
                        } else {

                            doFailed();
                        }


                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {





    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

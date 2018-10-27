package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.RepairCommentSuccessEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class RepairCommentActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.rating_speed)
    RatingBar mRatingSpeed;
    @BindView(R.id.rating_attitude)
    RatingBar mRatingAttitude;
    @BindView(R.id.rating_technology)
    RatingBar mRatingTechnology;
    private String ratingSpeed = "5";
    private String ratingAttitude = "5";
    private String ratingTec = "5";
    private String wid;

    public static void toActivity(Context context,String wid) {
        Intent intent = new Intent(context, RepairCommentActivity.class);
        intent.putExtra("wid",wid);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repair_comment;
    }

    @Override
    public void init() {

        wid = getIntent().getStringExtra("wid");
        setTitleText(R.string.text_common_comment);
        setRightText(R.string.common_commit);
        mRatingSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingSpeed = String.valueOf(rating);
            }
        });
        mRatingAttitude.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingAttitude = String.valueOf(rating);
            }
        });
        mRatingTechnology.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingTec = String.valueOf(rating);
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(mEtContent.getText().toString().trim())){
                    showToast("请输入评价内容");
                    return;
                }
                commitComment();
                break;
        }
    }

    private void commitComment(){
        showLoadingDialog();
        Log.e("starrrrr","speed:"+ratingSpeed+"attitude:"+ratingAttitude+"tec:"+ratingTec);
        Subscription subscription = RetrofitHelper.getInstance().repairComment(wid,mEtContent.getText().toString().trim(),
                ratingSpeed,ratingAttitude,ratingTec)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("评价成功");
                        RxBus.getDefault().post(new RepairCommentSuccessEvent());
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }


}

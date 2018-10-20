package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepairCommentActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.rating_speed)
    RatingBar mRatingSpeed;
    @BindView(R.id.rating_attitude)
    RatingBar mRatingAttitude;
    @BindView(R.id.rating_technology)
    RatingBar mRatingTechnology;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RepairCommentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repair_comment;
    }

    @Override
    public void init() {

        setTitleText(R.string.text_common_comment);
        setRightText(R.string.common_commit);
        mRatingSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("rating",rating+"");
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
                break;
        }
    }


}

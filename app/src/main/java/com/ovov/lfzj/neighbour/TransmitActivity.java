package com.ovov.lfzj.neighbour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.TransmitEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class TransmitActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.iv_transim)
    ImageView mIvTransim;
    @BindView(R.id.tv_nickname)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    private String id;

    public static void toActivity(Context context, String nickname, String content, String img, String id) {
        Intent intent = new Intent(context, TransmitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("nickname", nickname);
        bundle.putString("content", content);
        bundle.putString("img", img);
        bundle.putString("id", id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transmit;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_transim_neighbour);
        setRightText(R.string.text_send);
        Bundle bundle = getIntent().getExtras();
        String nickname = bundle.getString("nickname");
        String content = bundle.getString("content");
        String img = bundle.getString("img");
        id = bundle.getString("id");
        mTvTitle.setText("@"+nickname);
        mTvContent.setText(content);
        if (TextUtils.isEmpty(img)){
            mIvTransim.setVisibility(View.GONE);
        }else {
            Picasso.with(mActivity).load(img).into(mIvTransim);
        }
    }


    private void tranmit() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().transimSquare(id,mEtContent.getText().toString())
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
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
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("转发成功");
                        RxBus.getDefault().post(new TransmitEvent());
                        finish();

                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(mEtContent.getText().toString())){
                    showToast("请输入分享体会");
                    return;
                }
                tranmit();
                break;
        }
    }
}

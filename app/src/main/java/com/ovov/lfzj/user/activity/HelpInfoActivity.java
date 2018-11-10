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
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class HelpInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.user_im)
    ImageView userIm;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.user1_im)
    ImageView user1Im;
    @BindView(R.id.user_tv)
    TextView userTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.user1_tv)
    TextView user1Tv;
    @BindView(R.id.content1_tv)
    TextView content1Tv;
    @BindView(R.id.rela)
    RelativeLayout rela;
    private String id;


    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, HelpInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_helpinfo;
    }

    @Override
    public void init() {
        id = getIntent().getExtras().get("id").toString();
        tvTitle.setText("您的反馈");
        layout.setVisibility(View.VISIBLE);
        initData();

    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getFeedBackInfo(id)
                .compose(RxUtil.<DataInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<SquareListInfo>>() {
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
                    public void onNext(DataInfo<SquareListInfo> listInfoDataInfo) {
                        userTv.setText(listInfoDataInfo.datas().getNickname());
                        contentTv.setText(listInfoDataInfo.datas().getQuestion());
                        if (listInfoDataInfo.datas().getUser_logo() != null && !listInfoDataInfo.datas().getUser_logo().equals("")) {
                            Picasso.with(mActivity).load(listInfoDataInfo.datas().getUser_logo()).into(userIm);
                        }

                        tvTime.setText(listInfoDataInfo.datas().getTime());
                        if (!listInfoDataInfo.datas().getAnswer().isEmpty()) {
                            rela.setVisibility(View.VISIBLE);
                            user1Tv.setText("客服乐乐");
                            content1Tv.setText(listInfoDataInfo.datas().getAnswer());
                            user1Im.setImageResource(R.mipmap.ic_default_head);
                            tvTime1.setText(listInfoDataInfo.datas().getReply_time());
                        }


                    }

                });
        addSubscrebe(subscription);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}

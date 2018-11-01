package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

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
    @BindView(R.id.question_im)
    ImageView questionIm;
    @BindView(R.id.awer_im)
    ImageView awerIm;
    @BindView(R.id.liner)
    LinearLayout liner;
    @BindView(R.id.liner1)
    LinearLayout liner1;
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
        questionIm.setVisibility(View.GONE);
        awerIm.setVisibility(View.GONE);
        liner.setVisibility(View.VISIBLE);
        liner1.setVisibility(View.VISIBLE);
        id = getIntent().getExtras().get("id").toString();
        tvTitle.setText("您的反馈");
        layout.setVisibility(View.VISIBLE);
        tvTitleQue.setText("我");
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
                        tvTitleQue.setText("我");
                        tvContent.setText(listInfoDataInfo.datas().getQuestion());
                        tvId.setText(listInfoDataInfo.datas().getTime());

                        if (!listInfoDataInfo.datas().getAnswer().isEmpty())
                            tvKefu.setText("客服乐乐:");
                        tvAContent.setText(listInfoDataInfo.datas().getAnswer());
                        tvTime.setText(listInfoDataInfo.datas().getReply_time());


                    }

                });
        addSubscrebe(subscription);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}

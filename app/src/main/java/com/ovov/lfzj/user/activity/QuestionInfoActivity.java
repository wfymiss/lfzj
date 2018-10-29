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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class QuestionInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_title_que)
    TextView tv_title_que;
    @BindView(R.id.layout)
    RelativeLayout layout;
    private String id;

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, QuestionInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_qusetion_list;
    }

    @Override
    public void init() {
        layout.setVisibility(View.VISIBLE);
        tvTitle.setText("问题详情");
        id = getIntent().getExtras().get("id").toString();
        initdata();
    }

    private void initdata() {
        Subscription subscription = RetrofitHelper.getInstance().questionInfo(id)
                .compose(RxUtil.<DataInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<SquareListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(R.string.text_login_invaild);
                        } else {

                            doFailed();
                        }


                    }

                    @Override
                    public void onNext(DataInfo<SquareListInfo> dataInfo) {
                        tv_title_que.setText(dataInfo.datas().getTitle());
                        tvContent.setText(dataInfo.datas().getInfo());
                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

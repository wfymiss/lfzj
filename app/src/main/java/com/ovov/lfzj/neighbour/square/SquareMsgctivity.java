package com.ovov.lfzj.neighbour.square;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.SquareMsgBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class SquareMsgctivity extends BaseActivity {

    @BindView(R.id.list_msg)
    ListView mListMsg;
    private List<SquareMsgBean> mData;
    private CommonAdapter<SquareMsgBean> mAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SquareMsgctivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_square_msgctivity;
    }

    @Override
    public void init() {
        setTitleText("新消息");
        initList();
        initData();
    }

    private void initList() {
        mData = new ArrayList<>();
        mAdapter = new CommonAdapter<SquareMsgBean>(mActivity,mData, R.layout.item_square_msg) {
            @Override
            public void convert(ViewHolder viewHolder, SquareMsgBean squareMsgBean, int i) {
                if (squareMsgBean.userInfo.user_logo != null && !TextUtils.isEmpty(squareMsgBean.userInfo.user_logo)){
                    Picasso.with(mActivity).load(squareMsgBean.userInfo.user_logo).into((ImageView) viewHolder.getView(R.id.iv_head));
                }
                if (!TextUtils.isEmpty(squareMsgBean.message)){
                    viewHolder.setVisible(R.id.tv_comment,true);
                    viewHolder.setText(R.id.tv_comment,squareMsgBean.message);
                    viewHolder.setVisible(R.id.iv_zan,false);
                }else {
                    viewHolder.setVisible(R.id.tv_comment,false);
                    viewHolder.setVisible(R.id.iv_zan,true);
                }
                viewHolder.setText(R.id.tv_time,squareMsgBean.time);
                viewHolder.setText(R.id.tv_nickname,squareMsgBean.userInfo.nickname);
                if (squareMsgBean.info.img != null && squareMsgBean.info.img.size() > 0){
                    Picasso.with(mActivity).load(squareMsgBean.info.img.get(0)).into((ImageView) viewHolder.getView(R.id.iv_image));
                    viewHolder.setVisible(R.id.iv_image,true);
                    viewHolder.setVisible(R.id.tv_content,false);
                }else {
                    viewHolder.setVisible(R.id.iv_image,false);
                    viewHolder.setVisible(R.id.tv_content,true);
                    viewHolder.setText(R.id.tv_content,squareMsgBean.info.comment);
                }
                viewHolder.setOnClickListener(R.id.container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareMsgDetailActivity.toActivity(mActivity,squareMsgBean.info.id);
                    }
                });
            }
        };
        mListMsg.setAdapter(mAdapter);
    }

    private void initData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getMessagelist()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SquareMsgBean>>() {
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
                    public void onNext(ListInfo<SquareMsgBean> listInfo) {
                        dismiss();
                        mData.clear();
                        mData.addAll(listInfo.datas());
                        mAdapter.notifyDataSetChanged();
                    }
                });
        addSubscrebe(subscription);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

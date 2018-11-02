package com.ovov.lfzj.neighbour.square;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.GoodListBean;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.neighbour.MyCommunityActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;


/**
 * A simple {@link Fragment} subclass.
 * 点赞列表页面
 */

public class GoodActivity extends BaseActivity {

    @BindView(R.id.list_comment)
    RecyclerView mListComment;

    Unbinder unbinder;
    private SquareDetailInfo squareDetailInfo;
    private List<GoodListBean> mData;
    private CommonAdapter<GoodListBean> mAdapter;
    private String id;

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, GoodActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_activity_comment;
    }

    @Override
    public void init() {
        setTitleText("点赞列表");
        id = getIntent().getStringExtra("id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mData = new ArrayList<>();
        mListComment.setLayoutManager(linearLayoutManager);
        mAdapter = new CommonAdapter<GoodListBean>(mActivity, mData, R.layout.item_good) {
            @Override
            public void convert(ViewHolder viewHolder, final GoodListBean replyBean) {
                viewHolder.setText(R.id.tv_nickname, replyBean.nickname);
                viewHolder.setText(R.id.tv_time, replyBean.time);
                CircleImageView ivHeader = viewHolder.getView(R.id.iv_header);
                if (replyBean.user_logo != null && !replyBean.user_logo.equals("")) {
                    Picasso.with(mActivity).load(replyBean.user_logo).into(ivHeader);
                }
                ivHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyCommunityActivity.toUserActivity(mActivity, replyBean.nickname, replyBean.user_logo, "2", replyBean.user_id, replyBean.signature);
                    }
                });
            }

        };
        mListComment.setAdapter(mAdapter);
        getGoodList();
        /*addRxBusSubscribe(GoodEvent.class, new Action1<GoodEvent>() {
            @Override
            public void call(GoodEvent goodEvent) {
                getSquareDetail();
            }
        });
*/
    }

    private void getGoodList() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getGoodList(id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<GoodListBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ListInfo<GoodListBean> listInfo) {
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



   /* private void getSquareDetail() {
        Subscription subscription = RetrofitHelper.getInstance().getSquareDetail(LoginUserBean.getInstance().getId())
                .compose(RxUtil.<DataInfo<SquareDetailInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<SquareDetailInfo>>() {
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
                    public void onNext(final DataInfo<SquareDetailInfo> squareDetailInfoDataInfo) {
                        mData.clear();
                        mData.addAll(squareDetailInfoDataInfo.datas().fabulous);
                        mAdapter.notifyDataSetChanged();

                    }
                });
        addSubscrebe(subscription);
    }
*/


}

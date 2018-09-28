package com.ovov.lfzj.neighbour.square;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.AddCommentEvent;
import com.ovov.lfzj.event.SquareCommentEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.isGood;

/**
 * Created by kaite on 2018/9/5.
 */

public class SquareCommentFragment extends BaseFragment {
    @BindView(R.id.list_comment)
    RecyclerView mListComment;
    Unbinder unbinder;
    private SquareDetailInfo squareDetailInfo;
    private CommonAdapter<SquareDetailInfo.ReplyBean> mAdapter;
    private List<SquareDetailInfo.ReplyBean> mData;

    public SquareCommentFragment() {
    }
    public static SquareCommentFragment newInstance() {
        SquareCommentFragment activityDetailFragment = new SquareCommentFragment();
        return activityDetailFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_comment, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        super.init();
        mData = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListComment.setLayoutManager(linearLayoutManager);
        mAdapter = new CommonAdapter<SquareDetailInfo.ReplyBean>(getActivity(), mData, R.layout.item_activity_comment) {
            @Override
            public void convert(ViewHolder viewHolder, SquareDetailInfo.ReplyBean replyBean) {
                viewHolder.setText(R.id.tv_nickname,replyBean.userInfo.nickname);
                viewHolder.setText(R.id.tv_content,replyBean.content);
                viewHolder.setText(R.id.tv_time,replyBean.time);
                CircleImageView ivHeader = viewHolder.getView(R.id.iv_header);
                if (replyBean.userInfo.user_logo != null && !replyBean.userInfo.user_logo.equals("")){
                    Picasso.with(mActivity).load(replyBean.userInfo.user_logo).into(ivHeader);
                }else {
                    Picasso.with(mActivity).load(R.mipmap.ic_default_head).into(ivHeader);
                }
            }
        };
        mListComment.setAdapter(mAdapter);
        getSquareDetail();
        addRxBusSubscribe(AddCommentEvent.class, new Action1<AddCommentEvent>() {
            @Override
            public void call(AddCommentEvent addCommentEvent) {
                getSquareDetail();
            }
        });


    }

    private void getSquareDetail() {
        Subscription subscription = RetrofitHelper.getInstance().getSquareDetail(squareDetailInfo.id)
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
                        mData.addAll(squareDetailInfoDataInfo.datas().reply);
                        mAdapter.notifyDataSetChanged();

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        squareDetailInfo = ((SquareDetailActivity) getActivity()).getSquareDetailinfo();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

package com.ovov.lfzj.neighbour.actiity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.event.SquareCommentEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by kaite on 2018/9/5.
 */

public class ActivityCommentFragment extends BaseFragment {
    @BindView(R.id.list_comment)
    RecyclerView mListComment;
    Unbinder unbinder;

    public ActivityCommentFragment() {
    }
    public static ActivityCommentFragment newInstance() {
        ActivityCommentFragment activityDetailFragment = new ActivityCommentFragment();
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
        final List<SquareDetailInfo.ReplyBean> mData = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListComment.setLayoutManager(linearLayoutManager);
        final CommonAdapter<SquareDetailInfo.ReplyBean> mAdapter = new CommonAdapter<SquareDetailInfo.ReplyBean>(getActivity(),mData, R.layout.item_activity_comment) {
            @Override
            public void convert(ViewHolder viewHolder, SquareDetailInfo.ReplyBean replyBean) {
                viewHolder.setText(R.id.tv_nickname,replyBean.userInfo.nickname);
                CircleImageView ivHeader = viewHolder.getView(R.id.iv_header);
                if (replyBean.userInfo.user_logo != null){
                    Picasso.with(mActivity).load(replyBean.userInfo.user_logo).into(ivHeader);
                }
            }

        };
        mListComment.setAdapter(mAdapter);
        addRxBusSubscribe(SquareCommentEvent.class, new Action1<SquareCommentEvent>() {
            @Override
            public void call(SquareCommentEvent squareCommentEvent) {
                mData.addAll(squareCommentEvent.replyBean);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

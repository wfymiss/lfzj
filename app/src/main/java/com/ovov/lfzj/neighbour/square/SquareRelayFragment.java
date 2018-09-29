package com.ovov.lfzj.neighbour.square;

import android.content.Context;
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
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.event.SquareCommentEvent;
import com.ovov.lfzj.event.SquareReplayEvent;
import com.ovov.lfzj.neighbour.MyCommunityActivity;
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

public class SquareRelayFragment extends BaseFragment {
    @BindView(R.id.list_comment)
    RecyclerView mListComment;
    Unbinder unbinder;
    private SquareDetailInfo squareDetailInfo;

    public SquareRelayFragment() {
    }

    public static SquareRelayFragment newInstance() {
        SquareRelayFragment activityDetailFragment = new SquareRelayFragment();
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
        final List<SquareDetailInfo.ForwardBean> mData = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListComment.setLayoutManager(linearLayoutManager);
        final CommonAdapter<SquareDetailInfo.ForwardBean> mAdapter = new CommonAdapter<SquareDetailInfo.ForwardBean>(getActivity(), mData, R.layout.item_activity_comment) {
            @Override
            public void convert(ViewHolder viewHolder, final SquareDetailInfo.ForwardBean replyBean) {
                viewHolder.setText(R.id.tv_nickname, replyBean.userInfo.nickname);
                viewHolder.setText(R.id.tv_content, replyBean.comment);
                CircleImageView ivHeader = viewHolder.getView(R.id.iv_header);
                if (replyBean.userInfo.user_logo != null && !replyBean.userInfo.user_logo.equals("")) {
                    Picasso.with(mActivity).load(replyBean.userInfo.user_logo).into(ivHeader);
                }
                ivHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyCommunityActivity.toUserActivity(mActivity,replyBean.userInfo.nickname,replyBean.userInfo.user_logo,"2",replyBean.user_id,replyBean.userInfo.signature);
                    }
                });
            }

        };
        mListComment.setAdapter(mAdapter);
        if (LoginUserBean.getInstance().getUserInfoBean() != null && LoginUserBean.getInstance().getForwardBean().size() > 0){
            mData.addAll(LoginUserBean.getInstance().getForwardBean());
            mAdapter.notifyDataSetChanged();
        }



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*squareDetailInfo = ((SquareDetailActivity) getActivity()).getSquareDetailinfo();
        LoginUserBean.getInstance().setForwardBean(squareDetailInfo.forward);
        LoginUserBean.getInstance().save();*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

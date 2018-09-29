package com.ovov.lfzj.neighbour.square;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ovov.lfzj.event.GoodEvent;
import com.ovov.lfzj.event.SquareGoodEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.neighbour.MyCommunityActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseGoodFragment extends BaseFragment {

    @BindView(R.id.list_comment)
    RecyclerView mListComment;

    Unbinder unbinder;
    private SquareDetailInfo squareDetailInfo;
    private List<SquareDetailInfo.FabulousBean> mData;
    private CommonAdapter<SquareDetailInfo.FabulousBean> mAdapter;


    public static BaseGoodFragment newInstance() {

        Bundle args = new Bundle();

        BaseGoodFragment fragment = new BaseGoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BaseGoodFragment() {
        // Required empty public constructor
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mData = new ArrayList<>();
        mListComment.setLayoutManager(linearLayoutManager);
        mAdapter = new CommonAdapter<SquareDetailInfo.FabulousBean>(getActivity(), mData, R.layout.item_good) {
            @Override
            public void convert(ViewHolder viewHolder, final SquareDetailInfo.FabulousBean replyBean) {
                viewHolder.setText(R.id.tv_nickname, replyBean.userInfo.nickname);
                viewHolder.setText(R.id.tv_time, replyBean.time);
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

        getSquareDetail();
        addRxBusSubscribe(GoodEvent.class, new Action1<GoodEvent>() {
            @Override
            public void call(GoodEvent goodEvent) {
                getSquareDetail();
            }
        });

    }

    private void getSquareDetail() {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* squareDetailInfo = ((SquareDetailActivity) getActivity()).getSquareDetailinfo();
        LoginUserBean.getInstance().setId(squareDetailInfo.id);
        LoginUserBean.getInstance().save();*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


}

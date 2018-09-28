package com.ovov.lfzj.neighbour.actiity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.neighbour.square.SquareFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/***
 * 活动fragment父容器，标题
 * Created by 袁磊 on 2017/7/3.
 */
public class ActivityFragment extends BaseFragment {

    @BindView(R.id.activity_list_recycler)
    ListView mActivityListRecycler;
    @BindView(R.id.activity_list_swf)
    SmartRefreshLayout mActivityListSwf;
    Unbinder unbinder;

    public ActivityFragment() {
    }

    public static ActivityFragment newInstance() {
        ActivityFragment activityDetailFragment = new ActivityFragment();
        return activityDetailFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_details, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }

    @Override
    public void init() {
        super.init();
        List<String> mData = new ArrayList<>();
        mData.add("凯德世家读书活动");
        mData.add("亲子活动");
        final List<Integer> mGridData = new ArrayList<>();
        mGridData.add(R.mipmap.guide_view_01);
        mGridData.add(R.mipmap.guide_view_02);
        mGridData.add(R.mipmap.guide_view_03);
        mGridData.add(R.mipmap.guide_view_04);
        mGridData.add(R.mipmap.guide_view_01);
        mGridData.add(R.mipmap.guide_view_02);
        mGridData.add(R.mipmap.guide_view_03);
        mGridData.add(R.mipmap.guide_view_04);

        CommonAdapter<String> mAdapter = new CommonAdapter<String>(this.getActivity(), mData, R.layout.item_activity) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                viewHolder.setText(R.id.tv_title, s);
            }
        };
        mActivityListRecycler.setAdapter(mAdapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.neighbour_list_header,null,false);
        mActivityListRecycler.addHeaderView(view);
        RecyclerView mGameRecycler = (RecyclerView) view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGameRecycler.setLayoutManager(linearLayoutManager);
        mGameRecycler.addItemDecoration(new SquareFragment.SpacesItemDecoration(20));
        com.mcxtzhang.commonadapter.rv.CommonAdapter<Integer> mGameAdapter = new com.mcxtzhang.commonadapter.rv.CommonAdapter<Integer>(getActivity(),mGridData, R.layout.item_game) {
            @Override
            public void convert(com.mcxtzhang.commonadapter.rv.ViewHolder viewHolder, Integer integer) {
                ImageView ivGame = viewHolder.getView(R.id.iv_game);
                Picasso.with(getActivity()).load(integer).into(ivGame);
            }
        };
        mGameRecycler.setAdapter(mGameAdapter);
        mActivityListRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityDetailActivity.toActivity(getActivity());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

}

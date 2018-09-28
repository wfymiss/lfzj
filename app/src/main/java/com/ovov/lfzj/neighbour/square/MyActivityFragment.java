package com.ovov.lfzj.neighbour.square;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.neighbour.actiity.ActivityPutActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/***
 * 活动fragment父容器，标题
 * Created by 袁磊 on 2017/7/3.
 */
public class MyActivityFragment extends BaseFragment {

    @BindView(R.id.activity_list_recycler)
    ListView mActivityListRecycler;
    @BindView(R.id.activity_list_swf)
    SmartRefreshLayout mActivityListSwf;
    @BindView(R.id.tv_put)
    TextView mTvPut;
    Unbinder unbinder;
    Unbinder unbinder1;

    public MyActivityFragment() {
    }

    public static MyActivityFragment newInstance() {
        MyActivityFragment activityDetailFragment = new MyActivityFragment();
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
        mTvPut.setText("发布活动");
        mTvPut.setVisibility(View.VISIBLE);
        List<String> mData = new ArrayList<>();
        mData.add("凯德世家读书活动");
        mData.add("亲子活动");

        CommonAdapter<String> mAdapter = new CommonAdapter<String>(this.getActivity(), mData, R.layout.item_activity) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                viewHolder.setText(R.id.tv_title, s);
                TextView tvEnrollNum = viewHolder.getView(R.id.tv_enroll_num);
                tvEnrollNum.setVisibility(View.VISIBLE);
                View view = viewHolder.getView(R.id.view);
                view.setVisibility(View.INVISIBLE);
                LinearLayout linFoot = viewHolder.getView(R.id.lin_foot);
                linFoot.setVisibility(View.GONE);
            }
        };
        mActivityListRecycler.setAdapter(mAdapter);
    }



    @OnClick(R.id.tv_put)
    public void onViewClicked() {
        ActivityPutActivity.toActivity(getActivity());
    }
}

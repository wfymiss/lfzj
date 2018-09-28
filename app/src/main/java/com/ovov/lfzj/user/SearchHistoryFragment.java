package com.ovov.lfzj.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.widget.NoScrollGridView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kaite on 2018/8/6.
 */

public class SearchHistoryFragment extends BaseFragment {
    Unbinder unbinder;

    List<SearchHistoryInfo> mSearchHistoryInfos = new ArrayList<>();
    @BindView(R.id.grid_history)
    NoScrollGridView mGridHistory;
    @BindView(R.id.grid_hot)
    NoScrollGridView mGridHot;
    @BindView(R.id.tv_search_history)
    TextView mTvSearchHistory;
    @BindView(R.id.iv_del)
    ImageView mIvDel;
    @BindView(R.id.tv_search_hot)
    TextView mTvSearchHot;
    private CommonAdapter<SearchHistoryInfo> mHistoryAdapter;

    private List<String> mHotData = new ArrayList<>();
    public SearchHistoryFragment() {

    }

    public static SearchHistoryFragment newInstance() {
        Bundle args = new Bundle();

        SearchHistoryFragment fragment = new SearchHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {

        initSearchHistory();
        initHot();
    }

    private void initHot() {

        CommonAdapter<String> mHotAdapter = new CommonAdapter<String>(mActivity,mHotData,R.layout.item_search_history) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                viewHolder.setText(R.id.tv_name, s);
            }
        };
        mGridHot.setAdapter(mHotAdapter);
        mGridHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RxBus.getDefault().post(new SearchHisotryEvent(mHotData.get((int) l)));
            }
        });

        mHotData.add("门禁卡");
        mHotData.add("商业街");
        mHotData.add("仁人路");
        mHotData.add("巨型铜火锅");
        mHotData.add("庙会");
        mHotData.add("吴家窑");
        mHotData.add("改革开放");
        mHotData.add("征集启事");
        mHotData.add("供热");

        mHotAdapter.notifyDataSetChanged();
    }

    private void initSearchHistory() {

        mHistoryAdapter = new CommonAdapter<SearchHistoryInfo>(mActivity, mSearchHistoryInfos, R.layout.item_search_history) {
            @Override
            public void convert(ViewHolder viewHolder, SearchHistoryInfo searchHistoryInfo, int i) {
                viewHolder.setText(R.id.tv_name, searchHistoryInfo.content);
            }

            @Override
            public int getCount() {
                return mSearchHistoryInfos.size() == 0 ? 0 : super.getCount();
            }
        };
        mGridHistory.setAdapter(mHistoryAdapter);
        mGridHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RxBus.getDefault().post(new SearchHisotryEvent(mSearchHistoryInfos.get((int) l).content));
            }
        });
        refreshHistoryData();
    }

    private void refreshHistoryData() {
        mSearchHistoryInfos.clear();
        mSearchHistoryInfos.addAll(SpHistoryStorage.getInstance(getActivity(), 6).sortHistory());
        /*if (mSearchHistoryInfos.isEmpty()) {

            mTvSearchHistory.setVisibility(View.GONE);
            mIvDel.setVisibility(View.GONE);
        }*/
        mHistoryAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_del)
    public void onViewClicked() {
        SpHistoryStorage.getInstance(getActivity(), 6).clear();
        refreshHistoryData();
    }
}

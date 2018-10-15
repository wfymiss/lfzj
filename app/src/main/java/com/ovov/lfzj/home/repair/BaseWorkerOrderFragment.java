package com.ovov.lfzj.home.repair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * Created by kaite on 2018/10/10.
 */

public abstract class BaseWorkerOrderFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.list_wolrorder)
    ListView mListWolrorder;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_work_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    protected abstract Observable<Object> getObservable(int type);
    protected abstract boolean isMaintenanceSupervisor();
    protected abstract boolean isPending();
    protected abstract boolean isMaintenance();
    protected abstract boolean isInHand();
    @Override
    public void init() {
        super.init();

        List<String> mData = new ArrayList<>();
        mData.add("原磊");
        mData.add("王杰");
        mData.add("刘庆杰");
        CommonAdapter<String> mAdapter = new CommonAdapter<String>(mActivity, mData, R.layout.item_work_order) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                viewHolder.setText(R.id.tv_nickname,s);
                TextView tvDispatch = viewHolder.getView(R.id.tv_dispatch);
                if (isMaintenanceSupervisor() && isPending()){
                    tvDispatch.setVisibility(View.VISIBLE);
                }else{
                    tvDispatch.setVisibility(View.GONE);
                }
            }
        };

        mListWolrorder.setAdapter(mAdapter);
        mListWolrorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkerOrderDetailActivity.toActivity(mActivity);

            }
        });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

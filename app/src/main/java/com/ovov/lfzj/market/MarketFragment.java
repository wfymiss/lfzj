package com.ovov.lfzj.market;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.youzan.androidsdkx5.YouzanBrowser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.youzan_view)
    YouzanBrowser mYouzanView;

    public static MarketFragment newInstance() {

        Bundle args = new Bundle();

        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MarketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }


    @Override
    public void init() {
        super.init();

        mYouzanView.loadUrl("https://h5.youzan.com/wscshop/feature/FElLHR3x42");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}

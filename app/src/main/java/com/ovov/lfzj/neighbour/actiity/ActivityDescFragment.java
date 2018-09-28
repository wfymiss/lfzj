package com.ovov.lfzj.neighbour.actiity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kaite on 2018/9/5.
 */

public class ActivityDescFragment extends BaseFragment {

    Unbinder unbinder;
    public ActivityDescFragment() {
    }
    public static ActivityDescFragment newInstance() {
        ActivityDescFragment activityDetailFragment = new ActivityDescFragment();
        return activityDetailFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_desc, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

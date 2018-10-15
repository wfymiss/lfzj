package com.ovov.lfzj.user.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerFragment extends BaseFragment {

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }

    @Override
    public void init() {
        super.init();

    }
}

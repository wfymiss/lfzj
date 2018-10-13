package com.ovov.lfzj.home.repair;

import android.os.Bundle;

import rx.Observable;

/**
 * Created by kaite on 2018/10/12.
 */

public class InHandFragment extends BaseWorkerOrderFragment {
    public InHandFragment() {
    }
    public static InHandFragment newInstance (){
        Bundle args = new Bundle();
        InHandFragment fragment = new InHandFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected Observable<Object> getObservable(int type) {
        return null;
    }

    @Override
    protected boolean isMaintenanceSupervisor() {
        return false;
    }

    @Override
    protected boolean isPending() {
        return false;
    }

    @Override
    protected boolean isMaintenance() {
        return false;
    }

    @Override
    protected boolean isInHand() {
        return false;
    }
}

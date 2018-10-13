package com.ovov.lfzj.property.home.repair;

import android.os.Bundle;

import com.ovov.lfzj.home.repair.BaseWorkerOrderFragment;

import rx.Observable;

/**
 * Created by kaite on 2018/10/12.
 */

public class PendingWorkorderFragment extends BaseWorkerOrderFragment {
    public PendingWorkorderFragment() {

    }

    public static PendingWorkorderFragment newInstance (){
        Bundle args = new Bundle();
        PendingWorkorderFragment fragment = new PendingWorkorderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected Observable<Object> getObservable(int type) {
        return null;
    }

    @Override
    protected boolean isMaintenanceSupervisor() {
        return true;
    }

    @Override
    protected boolean isPending() {
        return true;
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

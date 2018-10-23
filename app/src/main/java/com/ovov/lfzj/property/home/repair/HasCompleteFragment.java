package com.ovov.lfzj.property.home.repair;

import android.os.Bundle;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.home.repair.BaseWorkerOrderFragment;

import rx.Observable;

/**
 * Created by kaite on 2018/10/12.
 */

public class HasCompleteFragment extends BaseWorkerOrderFragment {
    public HasCompleteFragment() {
    }
    public static HasCompleteFragment newInstance (){
        Bundle args = new Bundle();
        HasCompleteFragment fragment = new HasCompleteFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected Observable<ListInfo<WorkOrderListInfo>> getObservable(int type) {
        return null;
    }

    @Override
    protected boolean isMaintenanceSupervisor() {
        return true;
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

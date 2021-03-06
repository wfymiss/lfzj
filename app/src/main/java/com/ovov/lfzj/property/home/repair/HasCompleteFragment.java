package com.ovov.lfzj.property.home.repair;

import android.os.Bundle;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.widget.CancelWorkerOrderEvent;
import com.ovov.lfzj.event.CancelEvent;
import com.ovov.lfzj.http.RetrofitHelper;

import rx.Observable;
import rx.functions.Action1;

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
    protected Observable<ListInfo<WorkOrderListInfo>> getObservable(int page) {
        return RetrofitHelper.getInstance().getWorkList(3,page);
    }

    @Override
    public void init() {
        super.init();
        addRxBusSubscribe(CancelWorkerOrderEvent.class, new Action1<CancelWorkerOrderEvent>() {
            @Override
            public void call(CancelWorkerOrderEvent cancelWorkerOrderEvent) {
                mRefresh.autoRefresh();
            }
        });
        addRxBusSubscribe(CancelEvent.class, new Action1<CancelEvent>() {
            @Override
            public void call(CancelEvent cancelEvent) {
                mRefresh.autoRefresh();
            }
        });
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

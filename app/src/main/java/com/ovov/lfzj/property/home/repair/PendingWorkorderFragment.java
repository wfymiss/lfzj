package com.ovov.lfzj.property.home.repair;

import android.os.Bundle;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.event.CancelEvent;
import com.ovov.lfzj.event.DispathSuccessEvent;
import com.ovov.lfzj.event.RecieptSuccessEvent;
import com.ovov.lfzj.event.WorkerCancelSuccessEvent;
import com.ovov.lfzj.http.RetrofitHelper;

import rx.Observable;
import rx.functions.Action1;

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
    public void init() {
        super.init();
        addRxBusSubscribe(DispathSuccessEvent.class, new Action1<DispathSuccessEvent>() {
            @Override
            public void call(DispathSuccessEvent dispathSuccessEvent) {
                mRefresh.autoRefresh();
            }
        });
        addRxBusSubscribe(WorkerCancelSuccessEvent.class, new Action1<WorkerCancelSuccessEvent>() {
            @Override
            public void call(WorkerCancelSuccessEvent workerCancelSuccessEvent) {
                mRefresh.autoRefresh();
            }
        });
        addRxBusSubscribe(RecieptSuccessEvent.class, new Action1<RecieptSuccessEvent>() {
            @Override
            public void call(RecieptSuccessEvent recieptSuccessEvent) {
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
    protected Observable<ListInfo<WorkOrderListInfo>> getObservable(int page) {
        return RetrofitHelper.getInstance().getWorkList(0,page);
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

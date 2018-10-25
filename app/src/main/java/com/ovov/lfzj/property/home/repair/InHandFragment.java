package com.ovov.lfzj.property.home.repair;

import android.os.Bundle;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.http.RetrofitHelper;

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
    protected Observable<ListInfo<WorkOrderListInfo>> getObservable(int page) {
        return RetrofitHelper.getInstance().getWorkList(2,page);
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
        return true;
    }
}

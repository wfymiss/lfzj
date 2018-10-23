package com.ovov.lfzj.home.repair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.home.HomeFragment;
import com.ovov.lfzj.http.RetrofitHelper;

import butterknife.ButterKnife;
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
    protected Observable<ListInfo<WorkOrderListInfo>> getObservable(int page) {
        return RetrofitHelper.getInstance().getWorkList(0,page);
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

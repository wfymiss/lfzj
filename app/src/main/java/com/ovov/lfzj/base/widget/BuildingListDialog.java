package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.adapter.SublistAdapter;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.SubselectEvent;
import com.ovov.lfzj.home.bean.SubListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaite on 2018/10/18.
 */

public class BuildingListDialog extends BaseDialog {
    List<SublistInfo> mData = new ArrayList<>();

    Context mContext;
    private SublistAdapter mAdapter;

    public BuildingListDialog(Context context, List<SublistInfo> datas) {
        super(context);
        mContext = context;
        this.mData = datas;

    }

    public void setData(List<SublistInfo> mData){
        mAdapter.setmData(mData);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.common_dialog_style;
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_building_list,null,false);
        ListView mList = view.findViewById(R.id.list_sub);
        mAdapter = new SublistAdapter(context);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RxBus.getDefault().post(new SubselectEvent(mData.get((int) id).str_id,mData.get((int) id).subdistrict_name));
                dismiss();
            }
        });
        return view;
    }
}

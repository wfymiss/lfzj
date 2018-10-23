package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseDialog;
import com.ovov.lfzj.base.adapter.RoomlistAdapter;
import com.ovov.lfzj.base.adapter.SublistAdapter;
import com.ovov.lfzj.base.bean.RoomListInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.bean.UnitInfo;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.RoomSelectEvent;
import com.ovov.lfzj.event.SubselectEvent;
import com.ovov.lfzj.home.bean.SubdistrictsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaite on 2018/10/18.
 */

public class RoomListDialog extends BaseDialog {
    List<RoomListInfo> mData = new ArrayList<>();

    Context mContext;
    private RoomlistAdapter mAdapter;

    public RoomListDialog(Context context, List<RoomListInfo> datas) {
        super(context);
        mContext = context;
        this.mData = datas;

    }

    public void setData(List<RoomListInfo> mData){
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
        mAdapter = new RoomlistAdapter(context);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RxBus.getDefault().post(new RoomSelectEvent(mData.get((int) id).building_name,mData.get((int) id).unit,mData.get((int) id).number));
                dismiss();
            }
        });
        return view;
    }
}

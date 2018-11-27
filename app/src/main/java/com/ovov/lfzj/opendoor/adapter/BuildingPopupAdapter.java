package com.ovov.lfzj.opendoor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.BuildingListResult;


import java.util.ArrayList;
import java.util.List;

/**
 * 小区楼宇适配器
 * Created by lyy on 2017/10/26.
 */

public class BuildingPopupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<BuildingListResult.DataBean> list=new ArrayList<>();    // 定义楼宇数据集
    private BuildingItemListener buildingItemListener;                 // 楼宇列表点击事件

    public BuildingPopupAdapter(Context context){
        this.context=context;
    }

    // 传值楼宇数据
    public void setBuilding(List<BuildingListResult.DataBean> buildinglist){
        this.list=buildinglist;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.building_popup_item,parent,false);
        return new BuildingHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BuildingHolder buildingHolder= (BuildingHolder) holder;
        if (list!=null){
            String comm_build =list.get(position).getBuilding() + "  号楼";
            buildingHolder.building.setText(comm_build);
        }
        // 楼宇列表点击
        buildingHolder.building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildingItemListener!=null){
                    buildingItemListener.ItemClick(position,list.get(position).getBuilding());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BuildingHolder extends RecyclerView.ViewHolder {
        private TextView building;
        public BuildingHolder(View itemView) {
            super(itemView);
            building= (TextView) itemView.findViewById(R.id.comm_popu_text);    // popup 列表
        }
    }

    // 小区楼宇列表点击事件
    public interface BuildingItemListener{
        void ItemClick(int position, String build_id);
    }

    public void setBuildingItemListener(BuildingItemListener listener){
        this.buildingItemListener=listener;
    }
}

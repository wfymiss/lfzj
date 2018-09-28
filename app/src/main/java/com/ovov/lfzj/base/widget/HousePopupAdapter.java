package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.UserHouseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户拥有房间适配器
 * Created by lyy on 2017/10/28.
 */

public class HousePopupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private String sub_id,sub_name,houseName=null;  // 选中的小区名称，房间名称
    private List<UserHouseInfo> list=new ArrayList<>();
    private HouseItemListener HouseItemListener;

    public HousePopupAdapter(Context context){
        this.context=context;
    }

    public void setVillageData(List<UserHouseInfo> listData){
        this.list=listData;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.house_popup_item,parent,false);  // item 视图
        return new VillageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final VillageHolder villageHolder= (VillageHolder) holder;
        if (list!=null && list.size()>0){
            String house_name=list.get(position).getBuilding()+"-"+list.get(position).getUnit()+"-"+list.get(position).getRoom();
            villageHolder.house.setText(house_name);    // 显示房间名称
        }
        // 小区列表点击
        villageHolder.house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HouseItemListener!=null){
                    sub_id=list.get(position).getSub_id();
                    sub_name=list.get(position).getSub_name();
                    houseName=list.get(position).getBuilding()+"号楼 "+list.get(position).getUnit()+"单元 "+list.get(position).getRoom();
                    HouseItemListener.HouseItemClick(position,list.get(position).getId_one(),houseName,sub_id,sub_name);    // 房间id
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VillageHolder extends RecyclerView.ViewHolder {
        private TextView house;

        public VillageHolder(View itemView) {
            super(itemView);
            house= (TextView) itemView.findViewById(R.id.house_popu_text);
        }
    }

    // 小区列表点击事件
    public interface HouseItemListener{
        void HouseItemClick(int position, String house_id, String house_name, String sub_id, String sub_name);
    }
    public void setHouseItemListener(HouseItemListener listener){
        this.HouseItemListener=listener;
    }
}

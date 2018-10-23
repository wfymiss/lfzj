package com.ovov.lfzj.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.RoomListInfo;
import com.ovov.lfzj.base.bean.SublistInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaite on 2018/10/19.
 */

public class RoomlistAdapter extends BaseAdapter {


    List<RoomListInfo> mData = new ArrayList<>();
    Context context ;

    public RoomlistAdapter(Context context) {
        this.context = context;
    }

    public void setmData(List<RoomListInfo> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sub,null,false);
            holder = new ViewHolder();
            holder.tvSub = convertView.findViewById(R.id.tv_sub);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvSub.setText(mData.get(position).building_name+"号楼"+mData.get(position).unit+"单元"+mData.get(position).number);
        return convertView;
    }
    class ViewHolder{
        TextView tvSub;
    }
}

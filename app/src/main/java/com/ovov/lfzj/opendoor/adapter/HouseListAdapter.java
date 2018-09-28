package com.ovov.lfzj.opendoor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ovov.lfzj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 * 申请钥匙小区选择列表adapter
 */

public class HouseListAdapter extends BaseAdapter {
    List<String> listData = new ArrayList<String>();
    Context context;

    public HouseListAdapter(List<String> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.house_list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.tvHouse = (TextView) view.findViewById(R.id.tv_house_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvHouse.setText(listData.get(i));
        return view;
    }

    class ViewHolder {
        TextView tvHouse;
    }
}

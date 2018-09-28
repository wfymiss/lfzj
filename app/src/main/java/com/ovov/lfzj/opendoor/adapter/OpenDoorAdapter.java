package com.ovov.lfzj.opendoor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ovov.lfzj.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * 开门记录适配器
 * Created by Administrator on 2017/8/31.
 */

public class OpenDoorAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Map<String,Object>> list=new ArrayList<>();

    public OpenDoorAdapter(Context context, ArrayList<Map<String, Object>> list){
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_opendoor_item,parent,false);
            holder=new viewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.key_open_list_name);
            holder.time= (TextView) convertView.findViewById(R.id.key_open_list_time);
            holder.open_type= (TextView) convertView.findViewById(R.id.key_open_list_type);
            holder.fruit= (TextView) convertView.findViewById(R.id.key_open_list_fruit);

            convertView.setTag(holder);
        }else {
            holder= (viewHolder) convertView.getTag();
        }
        Map<String,Object> map=list.get(position);
        holder.name.setText(map.get("name").toString());
        holder.time.setText(map.get("time").toString());;
        holder.open_type.setText(map.get("type").toString());;
        holder.fruit.setText(map.get("fruit").toString());
        return convertView;
    }

    class viewHolder{
        TextView name,time,open_type,fruit;
    }
}

package com.ovov.lfzj.property.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyLFGJMineGridAdapter extends BaseAdapter {
    private List<Map<String,Object>> mUI=new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    Context context;
    public MyLFGJMineGridAdapter(List<Map<String,Object>> mUI, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mUI = mUI;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mUI == null ? 0 : mUI.size();
    }

    @Override
    public Object getItem(int position) {
        return mUI.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.lfgj_mine_item,
                    parent, false);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.home_grid_image);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.home_grid_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(context.getString((int)mUI.get(position).get("text")));
        viewHolder.imageView.setImageResource((int)mUI.get(position).get("image"));
        return convertView;
    }

    private static class MyGridViewHolder {
        ImageView imageView;
        TextView tvTitle;
    }
}

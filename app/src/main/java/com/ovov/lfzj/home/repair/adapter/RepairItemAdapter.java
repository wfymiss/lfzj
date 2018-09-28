package com.ovov.lfzj.home.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ovov.lfzj.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/** 报修图片————gradView
 * Created by lyy on 2017/11/6.
 */

public class RepairItemAdapter extends BaseAdapter {
    private List<String> mUI=new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    Context context;
    public RepairItemAdapter(List<String> ui, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        if (ui!=null && ui.size()>0){
            this.mUI = ui;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        int size=0;
        if (mUI.size()>3){
            size=3;
        }else {
            size=mUI.size();
        }
        return size;
    }

    @Override
    public String getItem(int position) {
        return mUI.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RepairViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new RepairViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.repair_img_item,
                    parent, false);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.iv_repair_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RepairViewHolder) convertView.getTag();
        }
        if (mUI.size()>0 && position<3 && mUI.get(position)!=null && !mUI.get(position).equals("")){    // 只显示3张图片
            String url = getItem(position);
            Picasso.with(context).load(url).into(viewHolder.imageView);
        }
        return convertView;
    }

    private static class RepairViewHolder {
        ImageView imageView;
    }
}

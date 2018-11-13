package com.ovov.lfzj.opendoor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.VisistorRecordResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class VIsitorListAdapter extends BaseAdapter {
    List<VisistorRecordResult.DataBean> listben = new ArrayList<>();
    Context context;

    public VIsitorListAdapter(Context context) {

        this.context = context;
    }
    public void setData(List<VisistorRecordResult.DataBean> list){
        listben.clear();
        this.listben = list;
        notifyDataSetChanged();
    }

    public void addAll(List<VisistorRecordResult.DataBean> list){
        this.listben.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return listben.size();
    }

    @Override
    public Object getItem(int i) {
        return listben.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.visitor_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
//            viewHolder.ivPhoto = (ImageView) view.findViewById(R.id.iv_visitor_photo);
            viewHolder.tvOpenTime = (TextView) view.findViewById(R.id.tv_open_time);
            viewHolder.tvVaildTime = (TextView) view.findViewById(R.id.tv_vaild_time);
            viewHolder.tvOpenNumber = (TextView) view.findViewById(R.id.tv_number_open);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tv_object);
            viewHolder.tvSuccess = (TextView) view.findViewById(R.id.tv_success);
            view.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
//        Picasso.with(context).load(listben.get(i).getPic()).into(viewHolder.ivPhoto);
        viewHolder.tvName.setText(listben.get(i).getVisitor());
        viewHolder.tvOpenTime.setText(listben.get(i).getCreated_time());
        viewHolder.tvOpenNumber.setText(listben.get(i).getValid_num()+"次");
        viewHolder.tvVaildTime.setText(listben.get(i).getActive_time()+"分钟");
        viewHolder.tvSuccess.setText(listben.get(i).getVisitor_tel());
        return view;
    }

    class ViewHolder {
        ImageView ivPhoto;
        TextView tvOpenTime,tvVaildTime,tvOpenNumber,tvName,tvSuccess;
    }


}

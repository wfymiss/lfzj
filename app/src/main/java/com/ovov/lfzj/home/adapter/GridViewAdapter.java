package com.ovov.lfzj.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.user.bean.HealthBean;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<HealthBean> mList;
    private int selectorPosition;

    public GridViewAdapter(Context context, List<HealthBean> mList) {
        this.mContext = context;
        this.mList = mList;

    }

    // 数据重置
    public void reset(List<HealthBean> dataSet) {
        mList.clear();
        if (dataSet != null) mList.addAll(dataSet);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return mList != null ? position : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.gird_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.gird_time);
        textView.setText(mList.get(position).getTime());
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            textView.setBackgroundResource(R.color.color_green);

        } else {
            if (mList.get(position).getCheckbox().equals("1")) {
                textView.setBackgroundResource(R.color.blu);
            } else {
                textView.setBackgroundResource(R.color.white);
            }
        }
        return convertView;
    }


    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
}

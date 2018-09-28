package com.ovov.lfzj.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by gqq on 2017/2/24.
 */

/**
 * 适配器的基类
 *
 * 1. 数据不一样
 * 2. item的布局不一样，ViewHolder
 * 不确定的内容可以用泛型来表示
 * <T>数据源
 * <V> ViewHolder
 */
public abstract class BaseListAdapter<T,V extends BaseListAdapter.ViewHolder> extends BaseAdapter {

    private List<T> mDataSet = new ArrayList<>();

    // 数据重置
    public void reset(List<T> dataSet){
        mDataSet.clear();
        if (dataSet!=null)mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    // 数据添加
    public void addAll(List<T> dataSet){
        if (dataSet!=null) mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public T getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(getItemViewLayout(),parent,false);
            convertView.setTag(getItemViewHolder(convertView));
        }
        viewHolder = (ViewHolder) convertView.getTag();

        // 绑定数据的方法
        viewHolder.bind(position);

        return convertView;
    }

    @LayoutRes
    protected abstract int getItemViewLayout();
    protected abstract V getItemViewHolder(View view);

    public abstract class ViewHolder{

        View mItemView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
            mItemView = itemView;
        }

        // 让数据和视图绑定
        protected abstract void bind(int position);

        // 提供一个可以直接拿到上下文的方法
        protected final Context getContext(){
            return mItemView.getContext();
        }
    }

    public List<T> getdata(){
        return mDataSet;
    }
}

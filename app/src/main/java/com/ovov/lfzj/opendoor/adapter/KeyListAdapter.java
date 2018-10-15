package com.ovov.lfzj.opendoor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.KeyReplyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 钥匙列表适配器
 * Created by 刘永毅 on 2017/8/31.
 */

public class KeyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<KeyReplyInfo.DataBean> list=new ArrayList<>();    //钥匙列表

    public KeyListAdapter(Context context){
        this.context=context;

    }
    public void setKeydata(List<KeyReplyInfo.DataBean> list_key){
        list.clear();
        this.list=list_key;   //清除旧钥匙、更新新钥匙
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_key_list,parent,false);
        return new KeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        KeyViewHolder keyViewHolder= (KeyViewHolder) holder;
        if (list!=null && list.size()>0){
            keyViewHolder.keyName.setText(list.get(position).getSn_name());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class KeyViewHolder extends RecyclerView.ViewHolder{
        private TextView keyName, key,time,keyType;
        public KeyViewHolder(View itemView) {
            super(itemView);
            keyName= (TextView)itemView.findViewById(R.id.key_list_name);

        }
    }
}

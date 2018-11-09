package com.ovov.lfzj.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.market.MarketActivity;
import com.ovov.lfzj.market.order.bean.ShopBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kaite on 2018/9/25.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyHolder> {
    private List<ShopBean> lists;
    private Context context;

    public CardAdapter(List<ShopBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }


    public void setmData(List<ShopBean> mData) {
        lists.clear();
        this.lists = mData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (lists.get(position).getImg() != null) {
            Picasso.with(context).load(lists.get(position).getImg()).into(holder.tv1);

        }
        if (lists.get(position).getName() != null) {
            holder.title.setText(lists.get(position).getName());
        }

        if (lists.get(position).getPrice() != null) {
            holder.price.setText("￥"+lists.get(position).getPrice());
        }

        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketActivity.toActivity(context, lists.get(position).getUrl(),9);


            }
        });

    }


    class MyHolder extends RecyclerView.ViewHolder {
        public TextView price;
        public TextView title;
        public ImageView tv1;
        public LinearLayout recycler;

        public MyHolder(View itemView) {
            super(itemView);
            tv1 = (ImageView) itemView.findViewById(R.id.iv_new_seed_ic);
            title = itemView.findViewById(R.id.tv_new_seed_title);
            price = itemView.findViewById(R.id.home_goods_price);
            recycler = itemView.findViewById(R.id.home_recycler_item_img);


        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}

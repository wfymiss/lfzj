package com.ovov.lfzj.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.SwitchEvent;
import com.ovov.lfzj.home.ui.WebActivity;

import java.util.List;

/**
 * Created by kaite on 2018/9/25.
 */

public class CardAdapter extends RecyclerView.Adapter {
    private List<Integer> lists;
    private Context context;

    public CardAdapter(List<Integer> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    class myholder extends RecyclerView.ViewHolder {

        public ImageView tv1;

        public myholder(View itemView) {
            super(itemView);
            tv1 = (ImageView) itemView.findViewById(R.id.iv_new_seed_ic);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myholder holder = new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((myholder) holder).tv1.setImageResource(lists.get(position));


        ((myholder) holder).tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (position) {
                    case 0:
                        RxBus.getDefault().post(new SwitchEvent());
                        break;
                    case 1:
                        RxBus.getDefault().post(new SwitchEvent());
                        break;
                    case 2:
                        RxBus.getDefault().post(new SwitchEvent());
                        break;
                    case 3:
                        RxBus.getDefault().post(new SwitchEvent());
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}

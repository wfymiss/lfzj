package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.ActivityTitleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动标题适配器
 * Created by Administrator on 2017/10/30.
 */

public class BuildingListTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<ActivityTitleInfo> list=new ArrayList<>();
    private TitleItemListener titleItemListener;

    public BuildingListTitleAdapter(Context context){
        this.context=context;
    }

    public void setTitleData(List<ActivityTitleInfo> infoList){
        this.list=infoList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.popup_activity_title_item,parent,false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TitleViewHolder viewHolder= (TitleViewHolder) holder;
        if (list!=null && list.size()>0){
            String title=list.get(position).getTitle();
            viewHolder.title.setText(title);   // 活动标题
        }
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleItemListener!=null){
                    //titleItemListener.titleItemClick(position,list.get(position).getTitle_id(),list.get(position).getTitle());
                    titleItemListener.titleItemClick(position,list.get(position).getTitle(),list.get(position).getTitle_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView title;   // 活动标题

        public TitleViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title_popu_text);
        }
    }

    //  活动列表点击选择
    public interface TitleItemListener {
        void titleItemClick(int position, String building, String type_id);
    }

    public void setTitleItemListener(TitleItemListener listener){
        this.titleItemListener=listener;
    }
}

package com.ovov.lfzj.opendoor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.OpenLogRecoder;


import java.util.ArrayList;
import java.util.List;

/**
 * 开门日志适配器
 * Created by 刘永毅 on 2017/7/25.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int LOAD_MORE=2;   // 底部加载更多数据
    public static final int LOAD_NO_DATA=3;   // 数据加载完成
    public static final int LOADING=4;    // 数据正在加载中
    private  int LOAD_STATE=LOAD_MORE;    // 加载数据底部初始化状态
    public final int TYPE_Body=0;    // 身体 活动评论
    public final int TYPE_Foot=1;    //底部
    private Context context;
    private List<OpenLogRecoder.DataBean> list=new ArrayList<>();

    public RecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1==getItemCount()){
            return TYPE_Foot;   //底部标题
        }
        return TYPE_Body;
    }

    //加载首页数据
    public void setRecoderAdapter(List<OpenLogRecoder.DataBean> recoderlist){
        list.clear();
        this.list=recoderlist;
        notifyDataSetChanged();
    }
    // 更多开门日志
    public void setRcordAdapterMore(List<OpenLogRecoder.DataBean> recoderlistMore){
        this.list.addAll(recoderlistMore);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_Foot){
            View view= LayoutInflater.from(context).inflate(R.layout.recycler_active_foot_item, parent, false);
            return new footViewHolder(view);
        }
        if (viewType==TYPE_Body){
            View view= LayoutInflater.from(context).inflate(R.layout.opendoor_recoder_item,parent,false);
            return new OpenRecoderHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_Foot) {
            footViewHolder footHolder = (footViewHolder) holder;
            switch (LOAD_STATE) {
                case LOAD_MORE:
                    if (getItemCount()>9){
                        footHolder.tv_foot.setVisibility(View.VISIBLE);
                        footHolder.tv_foot.setText("上拉加载数据 ......");
                    }
                    break;
                case LOADING:
                    footHolder.tv_foot.setText("正在加载数据 ......");
                    break;
                case LOAD_NO_DATA:
                    footHolder.tv_foot.setText("数据全部加载完 ......");
                    break;
                default:
            }
        }

        if (getItemViewType(position)==TYPE_Body){
            OpenRecoderHolder openHolder= (OpenRecoderHolder) holder;
            if (list!=null && list.size()>0){
                openHolder.tvAdree.setText(list.get(position).getLocation_id());
                openHolder.open_time.setText(list.get(position).getCreated_time());
                openHolder.type.setText(list.get(position).getLink());
                openHolder.open_result.setText(list.get(position).getResult());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class OpenRecoderHolder extends RecyclerView.ViewHolder{
        private TextView tvAdree,open_time,type,open_result;

        public OpenRecoderHolder(View itemView) {
            super(itemView);
            tvAdree= (TextView) itemView.findViewById(R.id.open_door_adree);
            open_time= (TextView) itemView.findViewById(R.id.tv_month);
            type= (TextView) itemView.findViewById(R.id.key_open_type);
            open_result= (TextView) itemView.findViewById(R.id.key_open_result);
        }
    }

    // 底部标题
    class footViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_foot;

        public footViewHolder(View itemView) {
            super(itemView);
            tv_foot = (TextView) itemView.findViewById(R.id.active_xq_foot);
        }
    }

    // 脚布局改变状态
    public void changeLoadState(int state) {
        LOAD_STATE = state;
        notifyDataSetChanged();  //通知刷新
    }
}

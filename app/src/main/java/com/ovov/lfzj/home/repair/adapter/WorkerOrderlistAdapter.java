package com.ovov.lfzj.home.repair.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ovov.lfzj.R;

import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 工单列表适配器
 * Created by lyy on 2017/9/3.
 */

public class WorkerOrderlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int LOAD_MORE=2;        // 底部加载更多数据
    public static final int LOAD_NO_DATA=3;    // 数据加载完成
    public static final int LOADING=4;         // 数据正在加载中
    private  int LOAD_STATE=LOAD_MORE;        // 加载数据底部初始化状态
    public final int TYPE_Body=0;    // 身体 活动评论
    public final int TYPE_Foot=1;    // 底部

    private Context context;
    private List<WorkOrderListInfo.DataBean> list=new ArrayList<>();
    private OnWorkListItemListener workListItemListener;   // 工单列表点击事件
    private OnListItemWidgetListener widgetListener;        // 列表控件点击
    private OnGridViewListener onGridViewListener;          // 定义图片点击事件

    private int wo_status=1;              // 工单状态
    private String img_uri=null;         // 头像路径
    private String grid_uri=null;

    public WorkerOrderlistAdapter(Context context){
        this.context=context;
    }

    //判断页面布局
    @Override
    public int getItemViewType(int position) {
        if (position+1==getItemCount()){
            return TYPE_Foot;   //底部标题
        }
        return TYPE_Body;
    }

    // 刷新获取数据为空
    public void setNoList(){
        if (list!=null){
            list.clear();
        }
        notifyDataSetChanged();
    }

    // 获取工单数据
    public void setWorkData(List<WorkOrderListInfo.DataBean> orderlist, int status){
        this.list=orderlist;
        wo_status=status;
        notifyDataSetChanged();
    }

    // 获取更多工单数据
    public void setMoreWorkData(List<WorkOrderListInfo.DataBean> orderlistMore, int status){
        this.list=orderlistMore;
        wo_status=status;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 下标题
        if(viewType==TYPE_Foot){
            View view= LayoutInflater.from(context).inflate(R.layout.recycler_active_foot_item, parent, false);
            return new footViewHolder(view);
        }
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_work_order_item,parent,false);
        return new WorklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_Foot) {
            footViewHolder footHolder = (footViewHolder) holder;
            if (getItemCount()>8){
                footHolder.tv_foot.setVisibility(View.VISIBLE);
                switch (LOAD_STATE) {
                    case LOAD_MORE:
                        footHolder.tv_foot.setText(R.string.script_load_more);
                        break;
                    case LOADING:
                        footHolder.tv_foot.setText(R.string.script_loading);
                        break;
                    case LOAD_NO_DATA:
                        footHolder.tv_foot.setText(R.string.script_load_no_data);
                        break;
                    default:
                }
            }else {
                footHolder.tv_foot.setVisibility(View.GONE);
            }
            return;
        }
        if (getItemViewType(position)==TYPE_Body){
            final WorklistViewHolder viewHolder= (WorklistViewHolder) holder;
            if (list!=null && list.size()>0){
                img_uri=list.get(position).getHead_img();
                if (img_uri!=null && !img_uri.trim().equals("")){
                    Picasso.with(context).load(img_uri).into(viewHolder.head_img);
                }
                viewHolder.user_name.setText(list.get(position).getMake_person());
                viewHolder.user_phone.setText(list.get(position).getMake_tel());
                viewHolder.create_time.setText(list.get(position).getRepair_time());   //  报修时间
                viewHolder.bespeak_time.setText(list.get(position).getMake_time());    //  预约时间
                viewHolder.addre.setText(list.get(position).getAddr());                 //  报修地址
                viewHolder.repair_content.setText(list.get(position).getContent().trim());
                if (list.get(position).getDesc_img()!=null && list.get(position).getDesc_img().size()>0){
                    viewHolder.imageGrid.setVisibility(View.VISIBLE);
                    viewHolder.imageGrid.setAdapter(new RepairItemAdapter(list.get(position).getDesc_img(),context));
                }else {
                    viewHolder.imageGrid.setVisibility(View.GONE);
                }
                //  显示状态按钮
                if (wo_status==3){
                    viewHolder.wo_status.setVisibility(View.VISIBLE);
                    viewHolder.wo_status.setText("验收");
                }
                if (wo_status==4){
                    if (list.get(position).getAppraise()!=null && list.get(position).getAppraise().size()>0){
                        viewHolder.wo_status.setVisibility(View.GONE);
                    }else {
                        viewHolder.wo_status.setVisibility(View.VISIBLE);
                        viewHolder.wo_status.setText("评论");
                    }
                }
                // 列表点击事件
                if (workListItemListener!=null){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int itemId=viewHolder.getLayoutPosition();  // 获取列表item 下标
                            workListItemListener.listItemClickListener(viewHolder.itemView,itemId,list.get(itemId).getId());
                        }
                    });
                }
                // 控件点击事件
                if (widgetListener!=null){
                    viewHolder.wo_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int itemId=viewHolder.getLayoutPosition();  // 获取列表item ID
                            widgetListener.widgetClick(wo_status,position,list.get(itemId).getId());
                        }
                    });
                }
                // 图片放大
                if (onGridViewListener!=null){
                    viewHolder.imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                            String uri=list.get(position).getDesc_img().get(i);
                            onGridViewListener.gridListener(position,uri);
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    // 工单列表布局页面
    class WorklistViewHolder extends RecyclerView.ViewHolder{
        private TextView user_name,user_phone,create_time,addre,bespeak_time,repair_content,wo_status;
        private NoScrollGridView imageGrid; // 工单图片
        private CircleImageView head_img;

        public WorklistViewHolder(View itemView) {
            super(itemView);
            head_img = (CircleImageView) itemView.findViewById(R.id.wo_head_img);
            user_name= (TextView) itemView.findViewById(R.id.wo_user_name);
            user_phone= (TextView) itemView.findViewById(R.id.wo_user_phone);
            create_time= (TextView) itemView.findViewById(R.id.wo_create_time);
            bespeak_time= (TextView) itemView.findViewById(R.id.wo_bespeak_time);
            repair_content= (TextView) itemView.findViewById(R.id.work_order_lidt_content);
            addre= (TextView) itemView.findViewById(R.id.wo_addr_tv);
            imageGrid= (NoScrollGridView) itemView.findViewById(R.id.worker_order_repair_image);
            wo_status= (TextView) itemView.findViewById(R.id.wo_status);
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
        notifyDataSetChanged();
    }

    // 工单列表点击事件
    public interface OnWorkListItemListener{
        void listItemClickListener(View view, int position, String order_id);
    }
    public void setWorkListItemListener(OnWorkListItemListener listener){
        this.workListItemListener=listener;
    }

    // 列表控件 点击
    public interface OnListItemWidgetListener{
        void widgetClick(int status, int pos, String wo_id);
    }

    public void setItemWidgetListener(OnListItemWidgetListener listener){
        this.widgetListener=listener;
    }

    // 图片点击（放大）
    public interface OnGridViewListener{
        void gridListener(int pos, String uri);
    }
    public void setOnGridViewListener(OnGridViewListener listener){
        this.onGridViewListener=listener;
    }
}

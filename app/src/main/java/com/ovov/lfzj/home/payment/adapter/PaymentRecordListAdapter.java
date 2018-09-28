package com.ovov.lfzj.home.payment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 物业缴费账单适配器
 * Created by Administrator on 2017/7/28.
 */

public class PaymentRecordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LOAD_MORE=2;        // 底部加载更多数据
    public static final int LOAD_NO_DATA=4;    // 数据加载完成
    public static final int LOADING=3;          // 数据正在加载中
    private  int LOAD_STATE=LOAD_MORE;         // 刷新时底部状态
    public final int TYPE_Body=0;      // 信息列表
    public final int TYPE_Foot=1;      // 页面底部下标

    private Context context;
    private List<PropertyPaymentInfo> list=new ArrayList<>();

    private OnItemListener onItemlistener;                  // 定义列表点击事件

    public PaymentRecordListAdapter(Context context) {
        this.context = context;
    }

    @Override  //   页面布局
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()){
            return TYPE_Foot;
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

   /* // 获取首页数据
    public void getPropertyOrder(List<PropertyPaymentInfo.DataBean> listData){
        this.list=listData;
        notifyDataSetChanged();
    }

    // 更多账单信息
    public void getOrderMore(List<PropertyPaymentInfo.DataBean> beanListMore){
        this.list=beanListMore;
        notifyDataSetChanged();
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 页脚
        if (viewType==TYPE_Foot){
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_active_foot_item,parent,false);
        //    Aut.autoSize(view);
            return new FootViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_paymentrecord_list_item,parent,false);
      //  AutoUtils.autoSize(view);
        return new PastHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position)==TYPE_Foot){
            final FootViewHolder footViewHolder= (FootViewHolder) holder;
            if (getItemCount()>8){
                footViewHolder.tv_foot.setVisibility(View.VISIBLE);
                switch (LOAD_STATE) {
                    case LOAD_MORE:
                        footViewHolder.tv_foot.setText(R.string.script_load_more);
                        break;
                    case LOADING:
                        footViewHolder.tv_foot.setText(R.string.script_loading);
                        break;
                    case LOAD_NO_DATA:
                        footViewHolder.tv_foot.setText(R.string.script_load_no_data);
                        break;
                    default:
                }
            }else {
                footViewHolder.tv_foot.setVisibility(View.GONE);
            }
            return;
        }
        final PastHolder pastHolder= (PastHolder) holder;
        if (getItemViewType(position)==TYPE_Body){
            if (list!=null && list.size()>0){
                /*pastHolder.order_title.setText(list.get(position).getName());
                pastHolder.user_target.setText(list.get(position).getRoom_number());
                pastHolder.order_time.setText(list.get(position).getCharge_from());
                pastHolder.order_pay.setText(list.get(position).getMoney());
                pastHolder.end_time.setText(list.get(position).getCharge_end());*/
                pastHolder.select_img.setVisibility(View.INVISIBLE);                             // 不显示选择弹框
            }
            // 列表点击
            pastHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemlistener!=null){
                        onItemlistener.itemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    //  数据主体
    class PastHolder extends RecyclerView.ViewHolder {
        private TextView order_title,user_target,order_time,order_pay,end_time;
        private ImageView select_img;
        private RelativeLayout check_rel;

        public PastHolder(View itemView) {
            super(itemView);
            order_title = (TextView) itemView.findViewById(R.id.payment_title);
            user_target = (TextView) itemView.findViewById(R.id.payment_obj);
            order_time= (TextView) itemView.findViewById(R.id.payment_time);
            order_pay= (TextView) itemView.findViewById(R.id.payment_bill);
            end_time = (TextView) itemView.findViewById(R.id.payment_endtime);
            check_rel = (RelativeLayout) itemView.findViewById(R.id.payment_sel_img_rel);       // 选择框——rel
            select_img = (ImageView) itemView.findViewById(R.id.payment_sel_img);                // 选择框
        }
    }

    // 页面下标题
    class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_foot;

        public FootViewHolder(View itemView) {
            super(itemView);
            tv_foot= (TextView) itemView.findViewById(R.id.active_xq_foot);
        }
    }

    // 脚布局改变状态
    public void changeLoadState(int state) {
        LOAD_STATE = state;
        notifyDataSetChanged();  //通知刷新
    }

    // 列表点击
    public interface OnItemListener{
        void itemClick(int pos);
    }
    public void setOnItemlistener(OnItemListener listener){
        this.onItemlistener=listener;
    }
}

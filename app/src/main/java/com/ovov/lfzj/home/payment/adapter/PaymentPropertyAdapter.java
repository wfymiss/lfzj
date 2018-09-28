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
 * Created by lyy on 2018/4/25.
 */

public class PaymentPropertyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int LOAD_MORE=2;        // 底部加载更多数据
    public static final int LOAD_NO_DATA=4;    // 数据加载完成
    public static final int LOADING=3;          // 数据正在加载中
    private  int LOAD_STATE=LOAD_MORE;         // 刷新时底部状态
    public final int TYPE_Body=0;      // 信息列表
    public final int TYPE_Foot=1;      // 页面底部下标

    private Context context;
    private List<PropertyPaymentInfo> list=new ArrayList<>();
    private boolean check_state=false;    // 账单支付合并状态

    private OnItemListener onItemlistener;                  // 定义列表点击事件
    private OnChooseRefreshListener onRefreshListener;     // 定义刷新事件

    public PaymentPropertyAdapter(Context mContext){
        this.context=mContext;
    }

    @Override  //   页面布局
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()){
            return TYPE_Foot;
        }
        return TYPE_Body;
    }

    // 账单支付状态——合并
    public void getCheck(boolean checkState){
        check_state=checkState;
        notifyDataSetChanged();
    }

    // 刷新获取数据为空
    public void setNoList(){
        if (list!=null){
            list.clear();
        }
        notifyDataSetChanged();
    }

    /*// 账单信息
    public void getCheckData(List<PropertyPaymentInfo.DataBean> beanList){
        list.clear();
        this.list=beanList;
        notifyDataSetChanged();
    }*/

    /*// 更多账单信息
    public void getCheckDataMore(List<PropertyPaymentInfo.DataBean> beanListMore){
        this.list=beanListMore;
        notifyDataSetChanged();
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 页脚
        if (viewType==TYPE_Foot){
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_active_foot_item,parent,false);
            return new FootViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_paymentrecord_list_item,parent,false);
        return new CheckHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position)==TYPE_Foot){
            FootViewHolder footViewHolder= (FootViewHolder) holder;
            switch (LOAD_STATE){
                case LOAD_MORE:
                    if (getItemCount()>9){
                        footViewHolder.tv_foot.setVisibility(View.VISIBLE);
                        footViewHolder.tv_foot.setText(R.string.script_load_more);
                    }else {
                        footViewHolder.tv_foot.setVisibility(View.GONE);
                    }
                    break;
                case LOADING:
                    footViewHolder.tv_foot.setText(R.string.script_loading);
                    break;
                case LOAD_NO_DATA:
                    footViewHolder.tv_foot.setText(R.string.script_load_no_data);
                    break;
                default:
            }
            return;
        }
        final CheckHolder checkHolder= (CheckHolder) holder;
        if (getItemViewType(position)==TYPE_Body) {
            if (list != null && list.size() > 0) {
                /*checkHolder.order_title.setText(list.get(position).getName());
                checkHolder.user_target.setText(list.get(position).getRoom_number());
                checkHolder.order_time.setText(list.get(position).getCharge_from());
                checkHolder.order_pay.setText(list.get(position).getMoney());
                checkHolder.end_time.setText(list.get(position).getCharge_end());*/
                if (check_state){
                    checkHolder.check_rel.setVisibility(View.VISIBLE);                                  // 选择框——rel
                    checkHolder.select_img.setVisibility(View.VISIBLE);                                // 显示选择弹框
                }else {
                    checkHolder.check_rel.setVisibility(View.INVISIBLE);                               // 不选择框——rel
                    checkHolder.select_img.setVisibility(View.GONE);                                   // 不显示选择弹框
                }
            }

            // 判断是否选中
            /*if (list.get(position).getSelect()) {
                checkHolder.select_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.car_select));
            } else {
                checkHolder.select_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.car_unselect));
            }
*/
            // 选择框选择刷新事件
            if (onRefreshListener != null) {         //判断是否被选中
                boolean isSelect = false;
                for (int i = 0; i < list.size(); i++) {
                   /* if (!list.get(i).getSelect()) {    // 账单有一条未选中，isSelect  为 false
                        isSelect = false;
                        break;      // 退出循环
                    } else {
                        isSelect = true;                // 全部选中为true
                    }*/
                }
                onRefreshListener.onRefresh(isSelect);
            }
            // 账单选择框点击
            checkHolder.check_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*list.get(position).setSelect(!list.get(position).getSelect());             // 单个账单状态变化（点击后select 定义相反值）
                    notifyDataSetChanged();   // 刷新页面*/
                }
            });
            // 列表点击
            checkHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!check_state){            //  非合并支付状态——单个账单付款
                        if (onItemlistener!=null){
                            onItemlistener.itemClick(position);
                        }
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
    class CheckHolder extends RecyclerView.ViewHolder {
        private TextView order_title,user_target,order_time,order_pay,end_time;
        private ImageView select_img;
        private RelativeLayout check_rel;

        public CheckHolder(View itemView) {
            super(itemView);
            order_title = (TextView) itemView.findViewById(R.id.payment_title);
            user_target = (TextView) itemView.findViewById(R.id.payment_obj);
            order_time= (TextView) itemView.findViewById(R.id.payment_time);
            order_pay= (TextView) itemView.findViewById(R.id.payment_bill);
            end_time = (TextView) itemView.findViewById(R.id.payment_endtime);
            check_rel = (RelativeLayout) itemView.findViewById(R.id.payment_sel_img_rel);
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

    //  刷新账单列表
    public interface OnChooseRefreshListener {
        void onRefresh(boolean isSelect);
    }
    public void setChooseRefreshListener(OnChooseRefreshListener listener){
        this.onRefreshListener=listener;
    }

    // 列表点击
    public interface OnItemListener{
        void itemClick(int pos);
    }
    public void setOnItemlistener(OnItemListener listener){
        this.onItemlistener=listener;
    }
}

package com.ovov.lfzj.home.repair.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单详情适配器
 * Created by lyy on 2017/11/6.
 */

public class OwnerRepairDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WorkOrderListInfo.DataBean> list=new ArrayList<>();
    private Context context;
    private float staff_cost,service_cost,repair_cost = 0 ;   //  材料费、服务费
    private int index=0;
    private int status=1;  // 工单状态
    private String repair_address,repair_remark=null;                    // 报修地址场所分类
    private WorkOrderCheckListener orderCheckListener;     // 工单控件点击事件
    private OnGridViewListener onGridViewListener;          // 定义图片点击事件

    public OwnerRepairDetailAdapter(Context context){
        this.context=context;
    }
    // 获取工单数据
    public void setData(List<WorkOrderListInfo.DataBean> listdata, int list_index, int wo_status){
        list.clear();
        if (listdata!=null && listdata.size()>0){
            this.list=listdata;
        }
        this.index=list_index;
        this.status=wo_status;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.owner_repair_detail_item,parent,false);
        return new repairDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final repairDetailHolder detailHolder= (repairDetailHolder) holder;
        if (list!=null && list.size()>0){
            repair_address=list.get(index).getPosition();               // 工单维修地址状态（入室维修，公共维修）
            detailHolder.order_num.setText(list.get(index).getOdd_numbers());
            detailHolder.addre_cf.setText(repair_address);
            detailHolder.repair_addr.setText(list.get(index).getAddr());
            detailHolder.repair_content.setText(list.get(index).getContent());
            detailHolder.make_time.setText(list.get(index).getMake_time());
            if (list.get(index).getDesc_img()!=null && list.get(index).getDesc_img().size()>0){          // 报修图片
                detailHolder.repair_pic.setVisibility(View.VISIBLE);
                detailHolder.gridView.setVisibility(View.VISIBLE);
                detailHolder.gridView.setAdapter(new RepairItemAdapter(list.get(index).getDesc_img(),context));
            }else {
                detailHolder.repair_pic.setVisibility(View.GONE);
                detailHolder.gridView.setVisibility(View.GONE);
            }
            if (status > 1){
                detailHolder.wo_indentify.setVisibility(View.VISIBLE);          // 显示维修工信息 取首条信息（下标0）
                if (list.get(index).getRes()!=null && list.get(index).getRes().size()>0){
                    detailHolder.worker.setText(list.get(index).getRes().get(0).getStaff_name());
                    detailHolder.worker_phone.setText(list.get(index).getRes().get(0).getStaff_tel());
                    detailHolder.worker_dement.setText(list.get(index).getRes().get(0).getDepartment_name());
                }else {
                    detailHolder.worker.setText(list.get(index).getSencond_send().get(0).getStaff_name());
                    detailHolder.worker_phone.setText(list.get(index).getSencond_send().get(0).getStaff_tel());
                    detailHolder.worker_dement.setText(list.get(index).getSencond_send().get(0).getDepartment_name());
                }
            }
            if (status >2){       //  验收
                repair_remark = list.get(index).getRes().get(0).getRemarks();    // 备注
                if (repair_remark!=null && !repair_remark.equals("")){
                    detailHolder.rek_content.setText("备注："+repair_remark);   // 显示备注信息
                }else {
                    detailHolder.rek_content.setText("备注： 无");
                }

                detailHolder.finish_time_tv.setText("结束时间");
                detailHolder.rek_content.setVisibility(View.VISIBLE);   // 备注
                detailHolder.wo_item_cost.setVisibility(View.VISIBLE);  // 费用 // 费用判断
                if (list.get(index).getCondition()!=null && list.get(index).getCondition().size()>0){
                    detailHolder.finish_time.setText(list.get(index).getCondition().get(0).getComplete_time());                          // 显示完成时间
                    if (list.get(index).getCondition().get(0).getMaterial_science()!=null){
                        detailHolder.stuff_price.setText(list.get(index).getCondition().get(0).getMaterial_science());    // 显示材料费
                        staff_cost= Float.parseFloat(list.get(index).getCondition().get(0).getMaterial_science());       // 材料费
                    }
                    if (list.get(index).getCondition().get(0).getService()!=null){
                        detailHolder.serve_price.setText(list.get(index).getCondition().get(0).getService());             // 显示服务费
                        service_cost= Float.parseFloat(list.get(index).getCondition().get(0).getService());              // 服务费
                    }
                    repair_cost = staff_cost + service_cost;       // 维修费合计
                    detailHolder.order_cost.setText(String.valueOf(repair_cost));    // 维修总费用
                }
            }
            if (status==1){
                detailHolder.finish_time_tv.setText("派单时间");
                detailHolder.finish_time.setText("暂无");
                detailHolder.repair_status.setText("待处理");
            }
            if (status==2){
                detailHolder.finish_time_tv.setText("派单时间");
                detailHolder.finish_time.setText(list.get(index).getDispatch_time());
                detailHolder.repair_status.setText("处理中");
            }
            if (status==3) {
                detailHolder.repair_status.setText("已处理");
                detailHolder.check_comm.setVisibility(View.VISIBLE);   // 工单验收按钮状态
            }else {
                detailHolder.check_comm.setVisibility(View.GONE);      // 工单验收按钮状态
            }
            if (status==4){
                if (list.get(index).getAppraise()!=null && list.get(index).getAppraise().size()>0){
                    detailHolder.repair_status.setText("已完成");
                    detailHolder.check_comm.setVisibility(View.GONE);        // 已评价
                }else {
                    detailHolder.repair_status.setText("未评价");
                    detailHolder.check_comm.setVisibility(View.VISIBLE);    // 未评价
                    detailHolder.check_comm.setText("评论");
                }
            }
            // 图片放大
            if (onGridViewListener!=null){
                detailHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        String uri=list.get(index).getDesc_img().get(i);
                        onGridViewListener.gridListener(position,uri);
                    }
                });
            }
            // 验收、评价按钮
            detailHolder.check_comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderCheckListener!=null){
                        orderCheckListener.OrderCheckClick(status,repair_address,list.get(index).getId(),list.get(index).getOdd_numbers(),repair_cost);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1;                     //  只取首条数据工单详情
    }

    public class repairDetailHolder extends RecyclerView.ViewHolder{
        private TextView order_num,worker,worker_phone,worker_dement,addre_cf,repair_addr,repair_content,
                make_time,finish_time_tv,finish_time,rek_content,
                repair_status,order_cost,stuff_price,serve_price,check_comm;
        private NoScrollGridView gridView;
        private LinearLayout wo_indentify,ft_time,wo_item_cost,repair_pic;

        public repairDetailHolder(View itemView) {
            super(itemView);
            order_num= (TextView) itemView.findViewById(R.id.wo_work_order);                // 工单号
            wo_indentify= (LinearLayout) itemView.findViewById(R.id.wo_identify_item);     // 维修工信息
            worker= (TextView) itemView.findViewById(R.id.wo_detail_worker);                // 维修人
            worker_phone= (TextView) itemView.findViewById(R.id.wo_detail_phone);          // 维修工电话
            worker_dement= (TextView) itemView.findViewById(R.id.wo_detail_company);      // 维修工所在部门
            addre_cf= (TextView) itemView.findViewById(R.id.wo_detail_cf_addr);            // 报修地址分类
            repair_addr= (TextView) itemView.findViewById(R.id.wo_detail_addr);            // 报修地址
            repair_content= (TextView) itemView.findViewById(R.id.wo_detail_content);     // 报修内容
            repair_pic= (LinearLayout) itemView.findViewById(R.id.wo_repair_pic);
            gridView= (NoScrollGridView) itemView.findViewById(R.id.wo_detail_gridview);  // 报修图片
            ft_time= (LinearLayout) itemView.findViewById(R.id.wo_finish_item);           // 工单完成时间item
            make_time= (TextView) itemView.findViewById(R.id.wo_make_time);               // 预约时间
            finish_time_tv= (TextView) itemView.findViewById(R.id.wo_finish_time_tv);   // 派单、结束时间标题
            finish_time= (TextView) itemView.findViewById(R.id.wo_finish_time);          // 派单、结束时间
            repair_status= (TextView) itemView.findViewById(R.id.wo_repair_status);     // 维修状态
            rek_content= (TextView) itemView.findViewById(R.id.wo_remark_content);      // 维修完成后维修工备注
            wo_item_cost= (LinearLayout) itemView.findViewById(R.id.wo_item_cost);
            stuff_price= (TextView) itemView.findViewById(R.id.wo_stuff_price);      // 材料费
            serve_price= (TextView) itemView.findViewById(R.id.wo_serve_price);      // 服务费
            order_cost= (TextView) itemView.findViewById(R.id.wo_repair_cost);       // 合计维修费用
            check_comm= (TextView) itemView.findViewById(R.id.wo_check);             // 工单验收
        }
    }

    // 工单验收、评价
    public interface WorkOrderCheckListener{
        void OrderCheckClick(int state, String re_adre, String num_id, String order_num, float num_cost);
    }
    public void setOrderCheckListener(WorkOrderCheckListener listener){
        this.orderCheckListener=listener;
    }

    // 图片点击（放大）
    public interface OnGridViewListener{
        void gridListener(int pos, String uri);
    }
    public void setOnGridViewListener(OnGridViewListener listener){
        this.onGridViewListener=listener;
    }
}

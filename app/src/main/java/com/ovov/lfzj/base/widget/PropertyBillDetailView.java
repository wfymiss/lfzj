package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 业主版我的缴费账单详情明细
 * Created by lyy on 2018/4/26.
 */

public class PropertyBillDetailView extends LinearLayout {
    private LayoutInflater layoutInflater;
    private List<PropertyPaymentInfo> list=new ArrayList<>();

    private String pos=null;   // 选定的支付账单下标

    //  构造方法
    public PropertyBillDetailView(Context context) {
        super(context);
    }

    public PropertyBillDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PropertyBillDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 获取明细 list d单条数据
    public void setDataOne(List<PropertyPaymentInfo> listDatas, String position){
        list.clear();
        pos= position;   // 账单下标
        if (listDatas!=null && listDatas.size()>0){
            list=listDatas;
        }
        notifyDataSetChanged();    // 获取页面item 数据
    }

    // 获取明细 list 数据
    public void setData(List<PropertyPaymentInfo> listDatas){
        list.clear();
        if (listDatas!=null && listDatas.size()>0){
            list=listDatas;
        }
        notifyDataSetChanged();    // 获取页面item 数据
    }

    private void notifyDataSetChanged() {
        removeAllViews();                          // 删除 所有视图
        if (list==null||list.size()==0){
            return;
        }
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (pos!=null){              // 单条账单
            int index= Integer.parseInt(pos);
            View view=getView(index);
            if (view==null){
                throw new NullPointerException("listView item layout is null");
            }
            addView(view,0,layoutParams);         // view 视图集（单条数据）
        }else {                      // 多条账单
            for (int i=0;i<list.size();i++){
                View view=getView(i);
                if (view==null){
                    throw new NullPointerException("listView item layout is null");
                }
                addView(view,i,layoutParams);            // view 视图集
            }
        }
    }

    // 循环累积item列表
    private View getView(int index) {
        if (layoutInflater==null){
            layoutInflater= LayoutInflater.from(getContext());
        }
        View itemView=layoutInflater.inflate(R.layout.property_bill_detail_item,null,false);   // item ——view
        TextView title= (TextView) itemView.findViewById(R.id.bill_title);    // 标题
        TextView price= (TextView) itemView.findViewById(R.id.bill_price);    // 金额
        TextView interval_time= (TextView) itemView.findViewById(R.id.bill_time_interval);    // 费用时间间隔
        View line_view= (View) itemView.findViewById(R.id.bill_line);         // 分割线
        if (list!=null && list.size()>0){
            String bill_title=list.get(index).name;
            DecimalFormat df = new DecimalFormat("0.00");
            double money = list.get(index).money;
            String newMoney = df.format(money);
            String bill_time=list.get(index).charge_from+"至"+list.get(index).charge_end;
            title.setText(bill_title);
            price.setText(newMoney);
            interval_time.setText(bill_time);
            if (index+1==list.size()||pos!=null){
                line_view.setVisibility(GONE);     // 首条数据隐藏上分割线（view）
            }
        }
        return itemView;
    }
}

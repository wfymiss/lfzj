package com.ovov.lfzj.home.repair.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.bean.WorkOrderUpInfo;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.repair.adapter.OwnerRepairDetailAdapter;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import rx.Subscription;

/**
 *  业主版工单详情
 */
public class OwnerRepairDetailActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.ow_repair_detail_lv)
    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private OwnerRepairDetailAdapter adapter;
    private List<WorkOrderListInfo.DataBean> list=new ArrayList<>();

    private String rep_address=null;       // 工单维修地址状态（入室维修，公共维修）
    private int list_index=0;       // 工单下标
    private int wo_status = 1;      // 当前工单状态
    private String sub_id=null;     // 小区id
    private String token=null;      // 用户token
    private String order_numId=null;
    private String orderNum=null;
    private float order_numCost=0;
    private String order_type="work_order";       // 工单类型标注


    @Override
    public int getLayoutId() {
        return R.layout.activity_owner_repair_detail;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_worker_order_detail);
        storageTokenRead();
        initData();         // 页面传值
        initDataShow();    // 页面展示
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token", "");
        sub_id = spf.getString("subId","");
    }

    // 页面传值
    private void initData() {
        Intent intent = getIntent();
        String position = intent.getStringExtra("repair_pos");      // 工单所在列表下标
        String status = intent.getStringExtra("repair_status");   // 工单状态
        if (position != null) {
            list_index = Integer.parseInt(position);
        }
        if (status !=null){
            wo_status = Integer.parseInt(status);
        }
        list= (List<WorkOrderListInfo.DataBean>) getIntent().getSerializableExtra("repair_list");
    }
    // 页面展示
    private void initDataShow() {
        linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);    //竖向刷新
        recyclerView.setLayoutManager(linearLayoutManager);  //recycler 竖向滑动
        adapter=new OwnerRepairDetailAdapter(this);
        adapter.setData(list,list_index,wo_status);
        recyclerView.setAdapter(adapter);
        // 放大图片
        adapter.setOnGridViewListener(new OwnerRepairDetailAdapter.OnGridViewListener() {
            @Override
            public void gridListener(int pos, String uri) {
                //pictureDisPlay(uri);   // 图片放大显示框
            }
        });
        // 工单验收
        adapter.setOrderCheckListener(new OwnerRepairDetailAdapter.WorkOrderCheckListener() {
            @Override
            public void OrderCheckClick(int state , String re_adre, String num_id, String order_num, float num_cost) {
                rep_address=re_adre;
                order_numId=num_id;
                orderNum=order_num;
                order_numCost=num_cost;
                if (state==3){
                    if (rep_address!=null && rep_address.equals("入室维修")){
                        if (num_cost == 0 || num_cost< 0){
                            AlertDialog.Builder builder=new AlertDialog.Builder(OwnerRepairDetailActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("当前费用为0，请与维修工确认工单费用");
                            builder.setPositiveButton("验收", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   confirmOrder();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.show();
                        }else {
                            formFeedPay();           // 跳转支付页面
                        }
                    }else if (rep_address!=null && rep_address.equals("公共维修")){
                        confirmOrder();
                    }
                }
                if (state==4){
                    Intent intent=new Intent(OwnerRepairDetailActivity.this, OrderCommentActivity.class);       // 评论页
                    intent.putExtra("orderId",order_numId);              // 列表下标
                    intent.putExtra("orderType",order_type);            // 类型
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void confirmOrder(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().setOrderConfirm(order_numId,sub_id)
                .compose(RxUtil.<ServerFeedBackInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ServerFeedBackInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();
                    }

                    @Override
                    public void onNext(ServerFeedBackInfo serverFeedBackInfo) {
                        if (serverFeedBackInfo.getStatus()==0){
                            showToast("工单验收成功");
                            finish();
                        }else {
                            Toast.makeText(mActivity,serverFeedBackInfo.getError_msg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addSubscrebe(subscription);
    }

/*
    // 图片放大显示框
    private void pictureDisPlay(String uri) {
        final PictureDisplayDialog.Builder displayDialog=new PictureDisplayDialog.Builder(this);
        displayDialog.setContent(uri);
        final PictureDisplayDialog dialog=displayDialog.onCreate();
        dialog.setCanceledOnTouchOutside(false);                 // 点击外部区域（true）关闭   false——不关闭
        dialog.show();

        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager windowManager=this.getWindowManager();                   // 为获取屏幕宽、高
        Display display=windowManager.getDefaultDisplay();    // 屏幕属性
        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();       // 获取对话框当前的参数值
        lp.width = (int)(display.getWidth());                                 //宽度设置为全屏
        lp.alpha = 1f;                                                        //参数为0到1之间。0表示完全透明，1就是不透明
        dialog.getWindow().setAttributes(lp);                                  //设置生效
    }
*/


    // 跳转支付页面
    private void formFeedPay() {
        /*Intent on_pay=new Intent(OwnerRepairDetailActivity.this, PayMentCostActivity.class);   // 跳转支付页面
        Bundle bundle = new Bundle();
        bundle.putString("type","支付类型");
        bundle.putString("title","工单支付");
        bundle.putString("order_id",order_numId);
        bundle.putString("order_num",orderNum);
        bundle.putString("order_pay", String.valueOf(order_numCost));
        bundle.putString("order_type","work");       //   支付类型
        on_pay.putExtras(bundle);
        startActivity(on_pay);
        finish();*/
    }


    // ——————————————————————服务端返回数据————————————


}

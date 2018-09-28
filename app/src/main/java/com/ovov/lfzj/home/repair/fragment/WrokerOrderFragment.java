package com.ovov.lfzj.home.repair.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.home.repair.activity.OrderCommentActivity;
import com.ovov.lfzj.home.repair.activity.OwnerRepairDetailActivity;
import com.ovov.lfzj.home.repair.adapter.WorkerOrderlistAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

;

/**
 *  工单列表页面
 * Created by 刘永毅 on 2017/8/7.
 */

public class WrokerOrderFragment extends BaseFragment  {
    private Context mContext;
    private static final String ARGS_PAGE="args_page";
    @BindView(R.id.repait_item_swf)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.work_order_recycler)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private WorkerOrderlistAdapter adapter;     // 工单列表适配器
    private List<WorkOrderListInfo.DataBean> list=new ArrayList<>();;
    private LinearLayoutManager linearLayoutManager;

    private int mpage;   // 当前fragment页数
    private String token=null;
    private String sub_id=null;          // 小区ID
    private int page=1;                 // 数据页数
    private int status=1;             // 工单状态   (status=1:待处理；status=2:处理中；status=3:待验收；status=4:已处理)
    private String order_type="work_order";       // 工单类型标注
    private int lastItem = 0;                      // 定义recyclerview 以获取的数据最底部position
    private boolean loading=true;

    private String rep_address=null;
    private String order_numId=null;
    private String orderNum=null;
    private float staff_cost,service_cost,repair_cost = 0 ;   //  材料费、服务费    总费用

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 获取fragment 生成 id
     * @param frag_page
     * @return
     */
    public static WrokerOrderFragment newInstance(int frag_page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE,frag_page);   // 获取fragment页面
        WrokerOrderFragment fragment = new WrokerOrderFragment();
        fragment.setArguments(args);  //传页面值
        return fragment;
    }

    // 当前fragment 页面
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        if (getArguments()!=null){
            mpage=getArguments().getInt(ARGS_PAGE);  // 获取当前fragment 页面
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_worker_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }


    @Override
    public void init() {
        super.init();
        mContext=getActivity();
        storageTokenRead();      // 获取token
        initData();              // 数据
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf=getActivity().getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token","");
        sub_id = spf.getString("subId","");
    }

    // 数据
    private void initData() {
        status=mpage;    // 当前工单状态（ 切换 ）
        linearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);    //竖向刷新
        recyclerView.setLayoutManager(linearLayoutManager);  //recycler 竖向滑动
        adapter=new WorkerOrderlistAdapter(mContext);      // 初始化适配器
        recyclerView.setAdapter(adapter);
        initRefresh();    // 刷新、加载
    }
    private void initRefresh() {
        // 下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading=true;     // 重新标记加载状态（true）
                        page=1;    // 刷新首页
                       // present.getWorkOrderlist(token,sub_id, String.valueOf(status),page);     // 工单网络请求
                        if (refreshLayout!=null && refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                    }
                },1500);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== RecyclerView.SCROLL_STATE_IDLE && lastItem + 1==adapter.getItemCount()){
                    adapter.changeLoadState(WorkerOrderlistAdapter.LOADING);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loading){     //加载更多
                                page=page + 1;
                               // present.getMoreWorkOrderlist(token,sub_id, String.valueOf(status),page);     // 工单网络请求
                            }else {
                                //  数据全部加载完成
                                adapter.changeLoadState(WorkerOrderlistAdapter.LOAD_NO_DATA);     // 没有数据了
                            }
                        }
                    },1500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 获取加载的最后一个可见视图在适配器item的位置
                lastItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        //  工单列表点击事件,进入工单详情
        adapter.setWorkListItemListener(new WorkerOrderlistAdapter.OnWorkListItemListener() {
            @Override
            public void listItemClickListener(View view, int position, String order_id) {
                Intent intent = new Intent(getActivity(), OwnerRepairDetailActivity.class);
                intent.putExtra("repair_status", String.valueOf(status));     // 当前工单状态
                intent.putExtra("repair_pos", String.valueOf(position));       // 点击列表下标
                intent.putExtra("repair_list", (Serializable) list);
                startActivity(intent);
            }
        });
        // 验收或评价
        adapter.setItemWidgetListener(new WorkerOrderlistAdapter.OnListItemWidgetListener() {
            @Override
            public void widgetClick(int wo_status, int pos, String wo_id) {
                if (wo_status==3){
                    // 费用判断
                    if (list.get(pos).getCondition()!=null && list.get(pos).getCondition().size()>0){
                        if (list.get(pos).getCondition().get(0).getMaterial_science()!=null){
                            staff_cost= Float.parseFloat(list.get(pos).getCondition().get(0).getMaterial_science());   // 材料费
                        }
                        if (list.get(pos).getCondition().get(0).getService()!=null){
                            service_cost= Float.parseFloat(list.get(pos).getCondition().get(0).getService());   // 服务费
                        }
                        repair_cost = staff_cost + service_cost;       // 维修费合计
                    }
                    rep_address=list.get(pos).getPosition();
                    order_numId=list.get(pos).getId();
                    orderNum=list.get(pos).getOdd_numbers();
                    if (rep_address!=null && rep_address.equals("入室维修")){
                        if (repair_cost == 0 || repair_cost< 0){
                            initRemind();    // 验收提醒
                        }else {
                            //formFeedPay();           // 跳转支付页面
                        }
                    }else if (rep_address!=null && rep_address.equals("公共维修")){
                       // present_check.setConfirmOrder(token,order_numId,sub_id);              //  工单验收
                    }
                }
                if (wo_status==4){
                    Intent intent=new Intent(getActivity(), OrderCommentActivity.class);            //对评论
                    intent.putExtra("orderId",wo_id);              // 工单id
                    intent.putExtra("orderType",order_type);     // 类型
                    startActivity(intent);
                }
            }
        });
        // 放大图片
        adapter.setOnGridViewListener(new WorkerOrderlistAdapter.OnGridViewListener() {
            @Override
            public void gridListener(int pos ,String uri) {
                //pictureDisPlay(uri);   // 图片放大显示框
            }
        });
    }
    /*// 图片放大显示框
    private void pictureDisPlay(String uri) {
        final PictureDisplayDialog.Builder displayDialog=new PictureDisplayDialog.Builder(getActivity());
        displayDialog.setContent(uri);
        final PictureDisplayDialog dialog=displayDialog.onCreate();
        dialog.setCanceledOnTouchOutside(false);                 // 点击外部区域（true）关闭   false——不关闭
        dialog.show();

        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager windowManager=getActivity().getWindowManager();                   // 为获取屏幕宽、高
        Display display=windowManager.getDefaultDisplay();    // 屏幕属性
        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();       // 获取对话框当前的参数值
        lp.width = (int)(display.getWidth());                                 //宽度设置为全屏
        lp.alpha = 1f;                                                        //参数为0到1之间。0表示完全透明，1就是不透明
        dialog.getWindow().setAttributes(lp);                                  //设置生效
    }
*/
    // 验收提醒
    private void initRemind() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("当前费用为0，请与维修工确认工单费用");
        builder.setPositiveButton("验收", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //present_check.setConfirmOrder(token,order_numId,sub_id);                       //  工单验收
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /*// 跳转支付页面
    private void formFeedPay() {
        Intent on_pay=new Intent(mContext, PayMentCostActivity.class);   // 跳转支付页面
        Bundle bundle = new Bundle();
        bundle.putString("type","支付类型");
        bundle.putString("title","工单支付");
        bundle.putString("order_id",order_numId);
        bundle.putString("order_num",orderNum);
        bundle.putString("order_pay", String.valueOf(repair_cost));
        bundle.putString("order_type","work");       //   支付类型
        on_pay.putExtras(bundle);
        startActivity(on_pay);
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        loading=true;     // 重新标记加载状态（true）
        page=1;           // 刷新首页
        //present.getWorkOrderlist(token,sub_id, String.valueOf(status),page);     // 工单网络请求——重新加载页面刷新
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refreshLayout!=null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }
    //————————————————————————————服务端返回数据————————————————————————

    /*@Override   // 获取工单列表数据
    public void getWorkOrderList(WorkOrderListInfo infoData) {
        if (infoData.getData()!=null && infoData.getData().size()>0){
            list.clear();        // 删除旧数据
            list=infoData.getData();
            adapter.setWorkData(list,status);
            adapter.changeLoadState(WorkerOrderlistAdapter.LOAD_MORE);     // 下标题——加载更多
        } else {
            list.clear();
            adapter.setNoList();     // 没有数据了
        }
    }
    // 加载更多
    @Override
    public void getMoreWorkOrderList(WorkOrderListInfo infoData) {
        if (infoData.getData()!=null && infoData.getData().size()>0){
            list.addAll(infoData.getData());
            adapter.setMoreWorkData(list,status);
            adapter.changeLoadState(WorkerOrderlistAdapter.LOAD_MORE);     // 下标题——加载更多
        }else {
            loading=false;
            adapter.changeLoadState(WorkerOrderlistAdapter.LOAD_NO_DATA);
        }
    }
    // 刷新失败
    @Override
    public void showNoneData(DataResultException dataResult) {
        list.clear();
        adapter.setNoList();      // 没有数据了
    }
    //  加载更多失败
    @Override
    public void getNoneWork(DataResultException dataResult) {
        loading=false;   // 没有数据时定义为 false
        adapter.changeLoadState(WorkerOrderlistAdapter.LOAD_NO_DATA);
    }

    @Override
    public void getWorkOrderUp(WorkOrderUpInfo infoData) {
        // 提交工单返回————本页不调用
    }

    // 工单验收————返回
    @Override
    public void setConfirm(ServerFeedBackInfo infoData) {
        if (infoData.getStatus()==0){
            Toast.makeText(mContext,"验收成功", Toast.LENGTH_SHORT).show();
            loading=true;     // 重新标记加载状态（true）
            page=1;    // 刷新首页
            present.getWorkOrderlist(token,sub_id, String.valueOf(status),page);     // 工单网络请求——重新加载页面刷新
        }else {
            Toast.makeText(mContext,infoData.getError_msg(), Toast.LENGTH_SHORT).show();
        }
    }

    // 工单验收————失败
    @Override
    public void showMsg(DataResultException dataResult) {
        if (dataResult.getMsg()!=null){
            Toast.makeText(mContext,dataResult.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }*/
}

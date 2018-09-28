package com.ovov.lfzj.base.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ovov.lfzj.R;

import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.BuildingEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.opendoor.adapter.BuildingPopupAdapter;


import org.greenrobot.eventbus.EventBus;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 小区所有楼宇选择
 * Created by lyy on 2017/10/26.
 */

public class BuildingPopup extends PopupWindow {
    private RecyclerView recyclerView;
    private View mview;                    //  视图
    private String token=null;            //   小区id
    private Context context;
    protected CompositeSubscription mCompositeSubscription;
    private BuildingPopupAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public BuildingPopup(Context mcontext, String token){
        this.context = mcontext;
        this.token=token;
        init(context);     //布局
        setPopupWindow();   //布局属性
    }

    private void init(Context ncontext) {
        adapter=new BuildingPopupAdapter(ncontext);
       getBuilding();
        LayoutInflater infalter = LayoutInflater.from(ncontext);
        mview=infalter.inflate(R.layout.building_popup,null);
        recyclerView= (RecyclerView) mview.findViewById(R.id.comm_popup_recy);
        linearLayoutManager = new LinearLayoutManager(ncontext, LinearLayoutManager.VERTICAL, false);    //竖向刷新
        recyclerView.setLayoutManager(linearLayoutManager);  //recycler 刷新
        recyclerView.setAdapter(adapter);
        // 小区楼宇列表点击事件
        adapter.setBuildingItemListener(new BuildingPopupAdapter.BuildingItemListener() {
            @Override
            public void ItemClick(int position, String build_id) {
                EventBus.getDefault().post(new BuildingEvent(build_id));
                dismiss();
            }
        });
    }

    private void getBuilding(){
        Subscription subscription = RetrofitHelper.getInstance().getBuildingList()
                .compose(RxUtil.<BuildingListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BuildingListResult>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BuildingListResult buildingListResult) {
                        if (buildingListResult.getStatus() == 0){
                            adapter.setBuilding(buildingListResult.getData());
                        }
                    }
                });
        addSubscrebe(subscription);

    }


    private void setPopupWindow() {
        this.setContentView(mview); //设置View
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);  //弹出窗宽度
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT); //弹出高度
        this.setFocusable(true);  //弹出窗可触摸
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));   //设置背景透明
        this.setAnimationStyle(R.style.mypopupwindow_anim_style);   //弹出动画
        mview.setOnTouchListener(new View.OnTouchListener() {   //如果触摸位置在窗口外面则销毁
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mview.findViewById(R.id.comm_popup_recy).getTop();    // 视图高度
                int y = (int) event.getY();     // 点击位置
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        unSubscribe();// 触摸位置在视图位置内部
                        dismiss();   //销毁
                        try {
                            BuildingPopup.this.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
    }
    /**
     * 管理各组件之间的通信
     *
     * @param eventType
     * @param act
     * @param <U>
     */
    public  <U> void addRxBusSubscribe(Class<U> eventType, Action1<U> act) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(RxBus.getDefault().toDefaultObservable(eventType, act));
    }

    /**
     * 取消订阅
     */
    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }
    /**
     * 添加订阅
     *
     * @param subscription
     */
    public void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

}

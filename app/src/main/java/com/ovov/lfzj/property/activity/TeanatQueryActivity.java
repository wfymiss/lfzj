package com.ovov.lfzj.property.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.RoomListPopup;
import com.ovov.lfzj.event.BuildingListEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.property.bean.ShopListResult;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class TeanatQueryActivity extends BaseActivity {

    private String token;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, TeanatQueryActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.tenant_query_toolbar)
    Toolbar tenantQueryToolbar;
    @BindView(R.id.tv_select_room)
    TextView tvSelectRoom;
    private String room;
    private String room_numb;
    private String houseid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_teanat_query;
    }

    @Override
    public void init() {
        storageTokenRead();
        initToolBar();
        initData();
        addRxBusSubscribe(BuildingListEvent.class, new Action1<BuildingListEvent>() {
            @Override
            public void call(BuildingListEvent event) {
                if (event.getIndex().equals("3")) {
                    houseid = event.getId();
                    tvSelectRoom.setText(event.getBuilding());
                }
            }
        });
    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getDJShopList()
                .compose(RxUtil.<ShopListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ShopListResult>() {
                    @Override
                    public void onError(Throwable e) {

                    }


                    @Override
                    public void onNext(ShopListResult result) {

                        if (result.getStatus() == 0) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                for (int i = 0; i < result.getData().size(); i++) {
                                    String room_id = result.getData().get(i).getNumber();
                                    String room_number = result.getData().get(i).getId();

                                    if (i == 0) {
                                        room = result.getData().get(i).getNumber();
                                        room_numb = result.getData().get(i).getId();
                                    }
                                    if (i > 0) {
                                        room = room + "," + room_id;
                                        room_numb = room_numb + "," + room_number;

                                    }
                                }
                                SharedPreferences spf = TeanatQueryActivity.this.getSharedPreferences("room", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("room_id", room);
                                editor.putString("room_number", room_numb);
                                editor.commit();
                            }
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token", "");
        Log.e("token", token);
    }

    // 获取标题toolbar
    private void initToolBar() {
        setSupportActionBar(tenantQueryToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //显示导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);  //不显示子标题
            tenantQueryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        tenantQueryToolbar.setNavigationIcon(R.mipmap.nav_icon_back_white);
    }

    private RoomListPopup roomListPopup;

    private void roomSelected() {
        if (roomListPopup != null) {
            roomListPopup.dismiss();
            roomListPopup = null;
        }
        roomListPopup = new RoomListPopup(this);
        // 开启 popup 时界面透明
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();  //获取页面属性
        lp.alpha = 0.7f;
//                if (bgAlpha == 1) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//                } else {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//                }
        this.getWindow().setAttributes(lp);
        // popupwindow 第一个参数指定popup 显示页面
        roomListPopup.showAtLocation(this.findViewById(R.id.layout_tenant_query), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        roomListPopup.update();
        // popup 退出时界面恢复
        roomListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = TeanatQueryActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                TeanatQueryActivity.this.getWindow().setAttributes(lp);
                roomListPopup.dismiss();
            }
        });
    }

    @OnClick({R.id.tv_select_room, R.id.brn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_room:
                roomSelected();
                break;
            case R.id.brn_confirm:
                if (tvSelectRoom.getText().length() > 0)
                    PaymentDetailActivity.toTenantActivity(this, 2, tvSelectRoom.getText().toString(), houseid);
                else
                    Toast.makeText(this, "请选择房间信息", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

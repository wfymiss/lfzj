package com.ovov.lfzj.property.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.RoomListResult;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.base.widget.BuildingListPopup;
import com.ovov.lfzj.base.widget.RoomListPopup;
import com.ovov.lfzj.base.widget.UnitListPopup;
import com.ovov.lfzj.event.BuildingListEvent;
import com.ovov.lfzj.property.bean.InformMeterResult;
import com.ovov.lfzj.property.bean.MeterResult;
import com.ovov.lfzj.property.bean.ReadMeterResult;
import com.ovov.lfzj.property.presenter.ReadingPresenter;
import com.ovov.lfzj.property.view.ReadingView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class OwnerQueryActivity extends BaseActivity implements ReadingView {

    private String token;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, OwnerQueryActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.owner_query_toolbar)
    Toolbar ownerQueryToolbar;
    @BindView(R.id.tv_select_building)
    TextView tvSelectBuilding;
    @BindView(R.id.tv_select_unit)
    TextView tvSelectUnit;
    @BindView(R.id.tv_select_room)
    TextView tvSelectRoom;
    private String houseid;
    private String unit;
    private String room;
    private String room_id;
    private String type;
    private String id_building;
    private String building;
    private ReadingPresenter readingPresenter;
    String  buildingid;

    @Override
    public void init() {
        storageTokenRead();
        readingPresenter = new ReadingPresenter(this);
        initToolBar();
        addRxBusSubscribe(BuildingListEvent.class, new Action1<BuildingListEvent>() {
            @Override
            public void call(BuildingListEvent event) {
                if (event.getIndex().equals("1")) {
                    buildingid  =event.getId();
                    Log.e("dadadada",buildingid);
                    tvSelectBuilding.setText(event.getBuilding());
                    readingPresenter.getUnitList(event.getBuilding());
                }
                if (event.getIndex().equals("2")) {
                    tvSelectUnit.setText(event.getBuilding());
                    readingPresenter.getRoomList(buildingid,tvSelectUnit.getText().toString());
                }
                if (event.getIndex().equals("3")) {
                    houseid = event.getId();
                    tvSelectRoom.setText(event.getBuilding());
                }
            }
        });

        TextWatcher buildingTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvSelectUnit.setText("");
                tvSelectRoom.setText("");
            }
        };
        tvSelectBuilding.addTextChangedListener(buildingTextWatcher);
        TextWatcher unitTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvSelectRoom.setText("");
            }
        };
        tvSelectUnit.addTextChangedListener(unitTextWatcher);
    }


    // 获取标题toolbar
    private void initToolBar() {
        setSupportActionBar(ownerQueryToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //显示导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);  //不显示子标题
            ownerQueryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ownerQueryToolbar.setNavigationIcon(R.mipmap.nav_icon_back_white);
    }

    @OnClick({R.id.tv_select_building, R.id.tv_select_unit, R.id.tv_select_room, R.id.brn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_building:
                readingPresenter.getBuildingList();
                sportSelect();
                break;
            case R.id.tv_select_unit:
                if (tvSelectBuilding.getText().length() > 0)
                    unitSelected();
                else
                    Toast.makeText(this, "请选择楼号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_select_room:
                if (tvSelectBuilding.getText().length() > 0 && tvSelectUnit.getText().length() > 0)
                    roomSelected();
                else
                    Toast.makeText(this, "请选择楼号和单元号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.brn_confirm:
                if (tvSelectBuilding.getText().length() > 0 && tvSelectUnit.getText().length() > 0 && tvSelectRoom.getText().length() > 0)

                    PaymentDetailActivity.toOwnerActivity(this, 1, tvSelectBuilding.getText().toString(), tvSelectUnit.getText().toString(), tvSelectRoom.getText().toString(), houseid);

                else
                    Toast.makeText(this, "请选择房间信息", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private BuildingListPopup titlePopup;

    private void sportSelect() {
        if (titlePopup != null) {
            titlePopup.dismiss();
            titlePopup = null;
        }
        titlePopup = new BuildingListPopup(this);
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
        titlePopup.showAtLocation(this.findViewById(R.id.layout_owner), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        titlePopup.update();
        // popup 退出时界面恢复
        titlePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = OwnerQueryActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                OwnerQueryActivity.this.getWindow().setAttributes(lp);
                titlePopup.dismiss();
            }
        });
    }

    private UnitListPopup unitListPopup;

    private void unitSelected() {
        if (unitListPopup != null) {
            unitListPopup.dismiss();
            unitListPopup = null;
        }
        unitListPopup = new UnitListPopup(this);
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
        unitListPopup.showAtLocation(this.findViewById(R.id.layout_owner), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        unitListPopup.update();
        // popup 退出时界面恢复
        unitListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = OwnerQueryActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                OwnerQueryActivity.this.getWindow().setAttributes(lp);
                unitListPopup.dismiss();
            }
        });
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
        roomListPopup.showAtLocation(this.findViewById(R.id.layout_owner), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        roomListPopup.update();
        // popup 退出时界面恢复
        roomListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = OwnerQueryActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                OwnerQueryActivity.this.getWindow().setAttributes(lp);
                roomListPopup.dismiss();
            }
        });
    }

    @Override
    public void setMeterType(MeterResult result) {

    }

    @Override
    public void setBuildingList(BuildingListResult result) {
        if (result.getData() != null && result.getData().size() > 0) {
            for (int i = 0; i < result.getData().size(); i++) {
                String building_id = result.getData().get(i).getBuilding();
                String idbuilding = result.getData().get(i).getBuilding_id();
                if (i == 0) {
                    building = result.getData().get(i).getBuilding();
                    id_building = result.getData().get(i).getBuilding_id();
                }
                if (i > 0) {
                    building = building + "," + building_id;
                    id_building = id_building + "," + idbuilding;
                }
            }
            SharedPreferences spf = this.getSharedPreferences("building", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("idbuilding", id_building);
            editor.putString("building_id", building);
            editor.commit();
        }
    }

    @Override
    public void setRoomList(RoomListResult result) {
        if (result.getDatas().getHouses_list() != null && result.getDatas().getHouses_list().size() > 0) {
            for (int i = 0; i < result.getDatas().getHouses_list().size(); i++) {
                String room_numb = result.getDatas().getHouses_list().get(i).getHouse_number();
                String room_getid = result.getDatas().getHouses_list().get(i).getId();

                if (i == 0) {
                    room = result.getDatas().getHouses_list().get(i).getHouse_number();
                    room_id = result.getDatas().getHouses_list().get(i).getId();
                }
                if (i > 0) {
                    room = room + "," + room_numb;
                    room_id = room_id + "," + room_getid;
                }
            }
            SharedPreferences spf = this.getSharedPreferences("room", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("room_id", room);
            editor.putString("room_number", room_id);
            editor.commit();
        } else {
            SharedPreferences spf = this.getSharedPreferences("room", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("room_id", "");
            editor.putString("room_number", "");
            editor.commit();

        }
    }

    @Override
    public void setUnitList(UnitListResult result) {
        if (result.getData() != null && result.getData().size() > 0) {
            for (int i = 0; i < result.getData().size(); i++) {
                String unit_id = result.getData().get(i).getUnit();
                if (i == 0) {
                    unit = result.getData().get(i).getUnit();
                }
                if (i > 0) {
                    unit = unit + "," + unit_id;
                }
            }
            SharedPreferences spf = this.getSharedPreferences("unit", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("unit_id", unit);
            editor.commit();
        }
    }

    @Override
    public void setInformMeter(InformMeterResult result) {

    }

    @Override
    public void setReadMeterResult(ReadMeterResult result) {

    }

    @Override
    public void showLoad() {
        showLoadingDialog();
    }

    @Override
    public void dissmiss() {
        dismiss();
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token", "");
        Log.e("token", token);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_owner_query;
    }



    /*@Subscribe
    public void setBuilding(BuildingListEvent event) {
        Log.e("clickevent", event.getBuilding());
        if (event.getId().equals("1")) {
            tvSelectBuilding.setText(event.getBuilding());
            readingPresenter.getUnitList(token, event.getBuilding());
        }
        if (event.getId().equals("2")) {
            tvSelectUnit.setText(event.getBuilding());
            readingPresenter.getRoomList(token, tvSelectBuilding.getText().toString(), event.getBuilding());
        }
        if (event.getId().equals("3")) {
            tvSelectRoom.setText(event.getBuilding());
        }

    }*/
}

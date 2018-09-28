package com.ovov.lfzj.home.repair.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ovov.lfzj.R;

import com.ovov.lfzj.base.BaseActivity;

import com.ovov.lfzj.base.bean.ActivityUpImageInfo;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.RoomListResult;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.base.bean.WorkOrderUpInfo;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.Uri2Pathutil;
import com.ovov.lfzj.base.widget.BuildingListPopup;
import com.ovov.lfzj.base.widget.HousePopup;
import com.ovov.lfzj.base.widget.RoomListPopup;
import com.ovov.lfzj.base.widget.SuccessPopupUtil;
import com.ovov.lfzj.base.widget.UnitListPopup;
import com.ovov.lfzj.event.BuildingListEvent;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.event.HouseEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.neighbour.adapter.GridPopupAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 物业报修页面
 */
public class RepairActivity extends BaseActivity implements  OnDateSetListener {
    private final int PHOTO_REQUEST_CODE = 111;              //  是否开启相机权限
    @BindView(R.id.tv_select_building)               // 楼管家 楼宇、房间选择框
            TextView tvSelectBuilding;
    @BindView(R.id.tv_select_unit)
    TextView tvSelectUnit;
    @BindView(R.id.tv_select_room)
    TextView tvSelectRoom;
    @BindView(R.id.re_house_info_owner)
    LinearLayout reHouseInfoOwner;
    @BindView(R.id.re_house_info_property)
    LinearLayout reHouseInfoProperty;

    private Unbinder unbinder;
//    @BindView(R.id.repair_viewpage)
//    ViewPager viewPager;
//    @BindView(R.id.repair_repair_tab)
//    TabLayout tab;
    @BindView(R.id.repair_category_indoor)     //选择室内维修
        TextView indoor;
    @BindView(R.id.repair_category_outdoor)   //选择室外维修
            TextView outdoor;
    @BindView(R.id.repair_lay_room)            // 室内
            LinearLayout lay_room;
    @BindView(R.id.repair_lay_out)             // 室外
            LinearLayout lay_out;
    @BindView(R.id.repair_addre_indoor)       // 室内报修房间
            TextView addre_room;
    @BindView(R.id.repair_addre_out)           // 室外报修地址
            EditText addre_out;
    @BindView(R.id.repair_person)              //报修联系人
            TextView person;
    @BindView(R.id.repair_phone)               //报修电话
            TextView phone;
    @BindView(R.id.repair_time)                //报修时间
            TextView time;
    @BindView(R.id.repair_category_select)    //报修类别选择
            LinearLayout radioGroup;
    @BindView(R.id.repair_category_water)
    TextView water;
    @BindView(R.id.repair_category_electric)
    TextView ecectric;
    @BindView(R.id.repair_category_gas)
    TextView gas;
    @BindView(R.id.repair_category_lock)
    TextView lock;
    @BindView(R.id.repair_category_other)     // 报修项目类别——其他
            TextView other;
    @BindView(R.id.repair_content)     // 工单报修内容
            EditText repair_content_text;
    @BindView(R.id.repair_image)       //  报修图片
            GridView gridView;
    @BindView(R.id.repair_right)       // 点击发送 提交工单
            TextView repair_right;
    @BindView(R.id.tv_phone)           // 物业电话
            TextView pro_phone;
    @BindView(R.id.tv_fee_scale)       // 收费标准
            TextView fee_scale;

    private String token, category = null;                 // token  报修项目   报修环境
    private String sub_id = null;         // 小区id
    private String houseId = null;       // 房间id
    private String houseName = null;     // 房间地址
    private String service_phone;       // 物业客服电话

    private String repair_address = null;     // 报修 场所分类（入室维修 或 公共维修 ）
    private String repair_user = null;        //报修人姓名
    private String repair_phone;              //报修电话
    private String repair_location = null;     //报修地址
    private String repair_time = null;         //预约时间
    private String repair_room = null;         // 报修房间
    private String categoryText = null;       //选中的类别文字
    private String repair_content;             //报修内容

    private TimePickerDialog mDialogAll;       // 日期选择器控件
    private GridPopupAdapter adapter;               // 实例化gridview 适配器
    private List<String> listImag = new ArrayList<>();        // 添加图片本地生成路径集合
    private String imageListUi = null;                       // 发布图片路径
    private HousePopup popup;          // 房间信息弹出框
    private List<String> listFileNmame = new ArrayList<>();
    private int REQUEST_CODE_CHOOSE = 101;
    private List<Uri> list = new ArrayList<Uri>();
    private File compressedImageFile;
    private String type;    // 报修工单用户身份
    private String unit;
    private String room;
    private String building;

    public static void toActivity(Context context, String type) {
        Intent intent = new Intent(context, RepairActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repair;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);// 用户房间弹出框监听

        initToolBar();                             //  获取标题
        storageTokenRead();                        //  获取token
        initLayoutInstall();                       //  页面设置
        initUserIdentity();                        //  用户身份识别
//        initViewPager();
        adapter = new GridPopupAdapter(this, 3);    //设置图片可发布最大数量
        gridView.setAdapter(adapter);

        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {

                        }
                    }
                });
        addRxBusSubscribe(ClickEvent.class, new Action1<ClickEvent>() {
            @Override
            public void call(ClickEvent clickEvent) {
                setPhoto();
            }
        });
        addRxBusSubscribe(BuildingListEvent.class, new Action1<BuildingListEvent>() {
            @Override
            public void call(BuildingListEvent event) {
                if (event.getId().equals("1")) {
                    tvSelectBuilding.setText(event.getBuilding());
                    getUnitList(event.getBuilding());
                }
                if (event.getId().equals("2")) {
                    tvSelectUnit.setText(event.getBuilding());
                    getRoomList(tvSelectBuilding.getText().toString(), event.getBuilding());
                }
                if (event.getId().equals("3")) {
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
    private void initToolBar() {
        setTitleText(R.string.text_repair);
        setTitleText(R.string.text_repair_list);
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token", "");
        sub_id = spf.getString("subId", "");
        service_phone = spf.getString("propertytel", "");
    }

    //  页面设置
    private void initLayoutInstall() {
        pro_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);            // 物业电话添加下划线
        fee_scale.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);            // 维修标准添加下划线
        other.performClick();                     // 默认工单选择类别初始化状态  // 报修项目——其他
        indoor.setSelected(true);                // 默认选择——室内维修
        categoryText = other.getText().toString();         // 默认选中的工单选择类别初始化状态值——其他
        repair_address = indoor.getText().toString();     // 维修地址分类值——室内维修
    }
    //  用户身份识别
    private void initUserIdentity() {
        type = getIntent().getStringExtra("type");
        if (type.equals("1")) {
            reHouseInfoOwner.setVisibility(View.VISIBLE);       // 普通用户
            reHouseInfoProperty.setVisibility(View.GONE);
        } else {
            reHouseInfoOwner.setVisibility(View.GONE);
            reHouseInfoProperty.setVisibility(View.VISIBLE);
            setRightText("");
        }

    }
    private void getBuildingList(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getBuildingList()
                .compose(RxUtil.<BuildingListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BuildingListResult>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();
                    }

                    @Override
                    public void onNext(BuildingListResult buildingListResult) {
                        dismiss();
                        if (buildingListResult.getData() != null && buildingListResult.getData().size() > 0) {
                            for (int i = 0; i < buildingListResult.getData().size(); i++) {
                                String building_id = buildingListResult.getData().get(i).getBuilding_id();
                                if (i == 0) {
                                    building = buildingListResult.getData().get(i).getBuilding_id();
                                }
                                if (i > 0) {
                                    building = building + "," + building_id;
                                }
                            }
                        }
                            SharedPreferences spf = getSharedPreferences("building", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putString("building_id", building);
                            editor.commit();
                    }
                });
        addSubscrebe(subscription);
    }
    private void getUnitList(String building){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getUnitList(building)
                .compose(RxUtil.<UnitListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<UnitListResult>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();
                    }

                    @Override
                    public void onNext(UnitListResult unitListResult) {
                        dismiss();
                        if (unitListResult.getData() != null && unitListResult.getData().size() > 0) {
                            for (int i = 0; i < unitListResult.getData().size(); i++) {
                                String unit_id = unitListResult.getData().get(i).getUnit();
                                if (i == 0) {
                                    unit = unitListResult.getData().get(i).getUnit();
                                }
                                if (i > 0) {
                                    unit = unit + "," + unit_id;
                                }
                            }
                            SharedPreferences spf = getSharedPreferences("unit", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putString("unit_id", unit);
                            editor.commit();
                        }
                    }
                });
        addSubscrebe(subscription);

    }
    private void getRoomList(String building,String unit){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getRoomList(building, unit)
                .compose(RxUtil.<RoomListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RoomListResult>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();
                    }

                    @Override
                    public void onNext(RoomListResult roomListResult) {
                        dismiss();
                        if (roomListResult.getData() != null && roomListResult.getData().size() > 0) {
                            for (int i = 0; i < roomListResult.getData().size(); i++) {
                                String room_id = roomListResult.getData().get(i).getNumber();
                                if (i == 0) {
                                    room = roomListResult.getData().get(i).getNumber();
                                }
                                if (i > 0) {
                                    room = room + "," + room_id;
                                }
                            }
                            SharedPreferences spf = getSharedPreferences("room", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putString("room_id", room);
                            editor.commit();
                        }
                    }
                });
        addSubscrebe(subscription);
    }
//    // 报修方法分类
//    private void initViewPager() {
//      List<String> titles=new ArrayList<>();
//        titles.add("语音");
//        titles.add("文本");
//        titles.add("视频");
//        for (int i=0;i<titles.size();i++){
//            tab.addTab(tab.newTab().setText(titles.get(i)));
//        }
//        tab.setTabMode(TabLayout.GRAVITY_CENTER);   // 标题居中
//
//        List<Fragment> list=new ArrayList<>();
//        list.add(new VoiceFragment());
//        list.add(new RepairtextFragment());
//        list.add(new VideoFragment());
//        FragmentBaseAdapter adapter=new FragmentBaseAdapter(getSupportFragmentManager(),list,titles);
//        viewPager.setAdapter(adapter);
//        tab.setupWithViewPager(viewPager);  //标题和页面同步
//    }

    /**
     * 设置工单标题、类别默认选择状态为false
     */
    public void selectRepairTitle() {
        indoor.setSelected(false);
        outdoor.setSelected(false);
    }

    //  报修项目选择分类
    public void selectCategory() {
        water.setSelected(false);
        ecectric.setSelected(false);
        gas.setSelected(false);
        lock.setSelected(false);
        other.setSelected(false);
    }

    //控件点击事件
    @OnClick({R.id.repair_category_indoor, R.id.repair_category_outdoor, R.id.repair_addre_indoor, R.id.repair_category_water, R.id.repair_category_electric, R.id.repair_category_gas,
            R.id.repair_category_lock, R.id.repair_category_other, R.id.repair_right, R.id.tv_right, R.id.repair_time, R.id.tv_phone, R.id.tv_select_building, R.id.tv_select_unit, R.id.tv_select_room})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.repair_category_indoor:     // 室内维修
                selectRepairTitle();
                indoor.setSelected(true);
                repair_address = indoor.getText().toString();
                break;
            case R.id.repair_category_outdoor:    // 室外维修
                selectRepairTitle();
                outdoor.setSelected(true);
                repair_address = outdoor.getText().toString();
                break;
            case R.id.repair_addre_indoor:
                chooseRoom();                              //选择报修房间
                break;
            case R.id.repair_category_water:      // 水
                selectCategory();
                water.setSelected(true);
                categoryText = water.getText().toString();
                break;
            case R.id.repair_category_electric:  // 电
                selectCategory();
                ecectric.setSelected(true);
                categoryText = ecectric.getText().toString();
                break;
            case R.id.repair_category_gas:       // 燃气
                selectCategory();
                gas.setSelected(true);
                categoryText = gas.getText().toString();
                break;
            case R.id.repair_category_lock:       // 门锁
                selectCategory();
                lock.setSelected(true);
                categoryText = lock.getText().toString();
                break;
            case R.id.repair_category_other:      // 其他
                selectCategory();
                other.setSelected(true);
                categoryText = other.getText().toString();
                break;
            case R.id.repair_time:    // 日期选择器
                showDatePicker();
                break;
            case R.id.repair_right:     // 提交工单
                if (type.equals("1")){
                    boolean judge = decideFormate();  // 判断输入的信息
                    if (judge) {
                        imageListUi = listToString(listFileNmame);
                        ownweCommitWorkorder(category, repair_address, houseId, repair_user, repair_phone, repair_time, repair_location, repair_content, sub_id, imageListUi);
                    } else {
                        return;
                    }
                }else {
                    if (tvSelectBuilding.getText().length() > 0 && tvSelectUnit.getText().length() > 0 && tvSelectRoom.getText().length() > 0){
                        boolean decide = propertydecideFormate();
                        if (decide){
                            imageListUi = listToString(listFileNmame);
                            propertycommitWorkOrder(repair_address,category,repair_user,imageListUi,repair_time,repair_phone,repair_location,repair_content,tvSelectBuilding.getText().toString(),tvSelectUnit.getText().toString(),tvSelectRoom.getText().toString());
                        }
                    } else
                        Toast.makeText(this, "请选择房间信息", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_right:     // 工单列表
                Intent intentlist = new Intent(this, WorkerOrderActivity.class);
                startActivity(intentlist);
                break;
            case R.id.tv_phone:         // 拨打客服电话
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + service_phone));  //  跳转到拨号界面，同时传递电话号码
                startActivity(dialIntent);
                break;

            case R.id.tv_select_building:   // 选择楼宇
                getBuildingList();
                sportSelect();
                break;
            case R.id.tv_select_unit:       // 选择单元
                if (tvSelectBuilding.getText().length() > 0)
                    unitSelected();
                else
                    Toast.makeText(this, "请选择楼号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_select_room:       // 选择房间
                if (tvSelectBuilding.getText().length() > 0 && tvSelectUnit.getText().length() > 0)
                    roomSelected();
                else
                    Toast.makeText(this, "请选择楼号和单元号", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }



    // 判断输入的信息
    private boolean decideFormate() {
        category = categoryText;                            // 报修项目
        repair_user = person.getText().toString();          // 维修人姓名
        repair_location = addre_out.getText().toString();  // 室外维修地址
        repair_room = houseName;                            // 室内维修房间
        repair_phone = phone.getText().toString();          // 报修人电话
        repair_content = repair_content_text.getText().toString();     // 报修内容
        if (TextUtils.isEmpty(repair_content) || TextUtils.isEmpty(repair_user) || TextUtils.isEmpty(repair_location)
                || TextUtils.isEmpty(repair_phone) || TextUtils.isEmpty(repair_time) || TextUtils.isEmpty(repair_room)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return false;
        } else if (RegexUtils.verifyMobile(repair_phone)) {
            if (TextUtils.isEmpty(repair_address)) {
                Toast.makeText(this, "请选择地址类型", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(repair_time)) {
                Toast.makeText(this, "请选择预约时间", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(houseId)) {
                Toast.makeText(this, "请选择房间信息", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // 判断输入的信息
    private boolean propertydecideFormate() {
        category = categoryText;                            // 报修项目
        repair_user = person.getText().toString();          // 维修人姓名
        repair_location = addre_out.getText().toString();  // 室外维修地址
        repair_room = houseName;                            // 室内维修房间
        repair_phone = phone.getText().toString();          // 报修人电话
        repair_content = repair_content_text.getText().toString();     // 报修内容
        if (TextUtils.isEmpty(repair_content) || TextUtils.isEmpty(repair_user) || TextUtils.isEmpty(repair_location)
                || TextUtils.isEmpty(repair_phone) || TextUtils.isEmpty(repair_time)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return false;
        } else if (RegexUtils.verifyMobile(repair_phone)) {
            if (TextUtils.isEmpty(repair_address)) {
                Toast.makeText(this, "请选择地址类型", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(repair_time)) {
                Toast.makeText(this, "请选择预约时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 选择报修房间
    private void chooseRoom() {
        if (popup != null) {
            popup.dismiss();
            popup = null;
        }
        popup = new HousePopup(this);   // 用户房间弹出框
        // 开启 popup 时界面透明
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popup.showAtLocation(this.findViewById(R.id.repair_room_popup), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);   // 第一个参数popup显示activity页面
        popup.update();
        // popup 退出时界面恢复
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
                popup.dismiss();
            }
        });
    }

    // 用户选择房间弹出框监听
    @Subscribe
    public void onEventMainThread(HouseEvent event) {
        if (event != null) {
            houseId = event.getHouse_id();
            houseName = event.getHouse_name();    // 选择的房间
            addre_room.setText(houseName);
        }
    }

    //展示时间选择器
    private void showDatePicker() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("报修时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(19)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    // 日期控件选择器返回值
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        repair_time = getDateToString(millseconds);   // 设定预约时间
        time.setText(repair_time);
    }

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    // 工单提交提醒弹出框
    private void remindPopup() {
        String ind_title = "提交成功";
        String ind_house = "工单已提交，等待派单";
        final SuccessPopupUtil.Builder dialogbuild = new SuccessPopupUtil.Builder(this);
        dialogbuild.setContent(ind_title, ind_house);                // 提醒内容
        final SuccessPopupUtil dialog = dialogbuild.Create();   // 弹出框事件
        // 活动报名弹出框回调点击事件
        dialogbuild.setOnTextViewClickListener(new SuccessPopupUtil.Builder.OnTextViewClickListener() {
            @Override
            public void onTextClick() {
                if (type.equals("1")){
                    Intent intent = new Intent(RepairActivity.this, WorkerOrderActivity.class);      // 跳转工单列表页
                    dialog.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialog.dismiss();
                    finish();
                }

            }
        });
        dialog.setCancelable(false);              // 点击返回键 dialog 不关闭
        dialog.setCanceledOnTouchOutside(false);  // 点击外部区域（true）关闭   false——外部区域点击不关闭
        dialog.show();

        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager windowManager = getWindowManager();                       //为获取屏幕宽、高
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();     //获取对话框当前的参数值
//        lp.width = (int)(display.getWidth());                                 //宽度设置为全屏
        lp.width = (int) (display.getWidth() * 0.56);                         //宽度设置为屏幕的0.58    //弹出框宽度
        //设置背景透明度 背景透明
        lp.alpha = 0.7f;                                                      //参数为0到1之间。0表示完全透明，1就是不透明
        dialog.getWindow().setAttributes(lp);                                  //设置生效
    }

    //  调用相机，相册 选择器
    private void setPhoto() {
        Matisse.from(RepairActivity.this)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.leFu.fileProvider"))
                .countable(true)
                .maxSelectable(3)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_130))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    /**
     * 拍照、调用相册回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            list.clear();
            list.addAll(Matisse.obtainResult(data));
            List<File> listImg = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ContentResolver contentResolver = getContentResolver();
                String path = Uri2Pathutil.getFromMediaUri(this, contentResolver, list.get(i));
                File file = new File(path);
                try {
                    compressedImageFile = new Compressor(this).compressToFile(file);   // 图片压缩
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showGridPhoto(compressedImageFile);
                listImg.add(compressedImageFile);

            }
            for (int i = 0; i < listImg.size(); i++) {
                if (!listImg.get(i).exists()) {
                    try {
//                    listImg.get(i).mkdirs();
                        listImg.get(i).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                uploadImage(listImg.get(i));
            }
        }
    }


    // 转换图片UI 路径
    public String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append("||"); // 分隔符
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 添加图片到适配器中
     *
     * @param bmppath 要添加的图片
     */
    public void showGridPhoto(File bmppath) {
        adapter.setGridImagePath(bmppath);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        titlePopup.showAtLocation(this.findViewById(R.id.repair_room_popup), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        titlePopup.update();
        // popup 退出时界面恢复
        titlePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = RepairActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                RepairActivity.this.getWindow().setAttributes(lp);
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
        unitListPopup.showAtLocation(this.findViewById(R.id.repair_room_popup), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        unitListPopup.update();
        // popup 退出时界面恢复
        unitListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = RepairActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                RepairActivity.this.getWindow().setAttributes(lp);
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
        roomListPopup.showAtLocation(this.findViewById(R.id.repair_room_popup), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);  // 第一个参数popup显示activity页面
        roomListPopup.update();
        // popup 退出时界面恢复
        roomListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = RepairActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                RepairActivity.this.getWindow().setAttributes(lp);
                roomListPopup.dismiss();
            }
        });
    }

    private void propertycommitWorkOrder(
                                 String position,
                                 String category,
                                 String contact,
                                 String desc_img,
                                 String time,
                                 String phone,
                                 String addr,
                                 String content,
                                 String building,
                                 String unit,
                                 String room) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().propertyCommitWorkeroeder( position, category, contact, desc_img,
                time, phone, addr, content, building, unit, room)
                .compose(RxUtil.<WorkOrderUpInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<WorkOrderUpInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        /*if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.getMsg());
                        }*/
                    }

                    @Override
                    public void onNext(WorkOrderUpInfo workOrderUpInfo) {

                        dismiss();
                        if (workOrderUpInfo.getStatus() == 0){
                            remindPopup();                     // 工单提交成功提醒弹出框
                        }
                    }
                });
        addSubscrebe(subscription);
    }
    private void ownweCommitWorkorder(String category, String repair_address, String houseId, String repair_user, String repair_phone, String repair_time, String repair_location, String repair_content, String sub_id, String imageListUi) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().ownerCommitWorkeroeder(category, repair_address, houseId, repair_user, repair_phone, repair_time, repair_location, repair_content, sub_id, imageListUi)
                .compose(RxUtil.<WorkOrderUpInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<WorkOrderUpInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();
                    }

                    @Override
                    public void onNext(WorkOrderUpInfo workOrderUpInfo) {
                        dismiss();
                        if (workOrderUpInfo.getStatus() == 0) {         // 提交成功
                            remindPopup();                     // 工单提交成功提醒弹出框
                        } else {
                            showToast(workOrderUpInfo.getError_msg());
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    private void uploadImage(File file){
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(), RequestBody.create(null,file));

        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().uploadImage(part)
                .compose(RxUtil.<ActivityUpImageInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ActivityUpImageInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();
                    }

                    @Override
                    public void onNext(ActivityUpImageInfo activityUpImageInfo) {
                        dismiss();
                        if (activityUpImageInfo.getStatus() == 0){
                            listFileNmame.add(activityUpImageInfo.getData());
                        }else {

                        }

                    }
                });
        addSubscrebe(subscription);
    }
}
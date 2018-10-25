package com.ovov.lfzj.property.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.property.bean.PaymentDetailBean;
import com.ovov.lfzj.property.widget.ChildListView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;

public class PaymentDetailActivity extends BaseActivity implements OnDateSetListener {
    @BindView(R.id.payment_detail_toolbar)
    Toolbar paymentDetailToolbar;
    @BindView(R.id.list_payment_detail)
    ListView listPaymentDetail;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private String token;
    private int status = 1;
    private String year = "2018";
    private List<PaymentDetailBean.BillListBean> mData = new ArrayList<>();
    private CommonAdapter<PaymentDetailBean.BillListBean> mAdapter;
    private TimePickerDialog mDialogAll;        // 日期选择器控件
    private String time = null;
    private int isShop;
    private String building;
    private String unit;
    private String room;
    private String houseid;
    private String idyear;
    private String getTotalAmount;
    private  TextView textView;
    private  TextView textView1;

    public static void toOwnerActivity(Context context, int is_shop, String building, String unit, String room, String houseid) {
        Intent intent = new Intent(context, PaymentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("building", building);
        bundle.putString("unit", unit);
        bundle.putString("room", room);
        bundle.putString("houseid", houseid);
        bundle.putInt("is_shop", is_shop);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void toTenantActivity(Context context, int is_shop, String room, String houseid) {
        Intent intent = new Intent(context, PaymentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("room", room);
        bundle.putInt("is_shop", is_shop);
        bundle.putString("houseid", houseid);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    public void init() {
        storageTokenRead();
        initToolBar();

        View view = View.inflate(this,R.layout.item_paymentdetatil, null);
        textView = view.findViewById(R.id.tv_time);
        textView1 = view.findViewById(R.id.tv_money);
        listPaymentDetail.addHeaderView(view);
        initList();
        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        isShop = bundle.getInt("is_shop");
        if (isShop == 1) {
            building = bundle.getString("building");
            houseid = bundle.getString("houseid");
            unit = bundle.getString("unit");
            room = bundle.getString("room");
            tvAddress.setText(building + "栋" + unit + "单元" + room + "室");
            getOwnerDetail("1", building, unit, room, houseid);
            Log.e("house", houseid);
        } else {
            room = bundle.getString("room");
            houseid = bundle.getString("houseid");
            tvAddress.setText("商" + room + "室");
            getTenantDetail("2", room, houseid);
        }
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });


    }

    //展示时间选择器
    private void showDatePicker() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId(time)
                .setYearText("年")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - tenYears)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.YEAR)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(19)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    private void getOwnerDetail(String is_shop, String building, String unit, String room, String houseid) {
        Subscription subscription = RetrofitHelper.getInstance().getOwnerPayment(is_shop, status, year, houseid)
                .compose(RxUtil.<DataInfo<PaymentDetailBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<PaymentDetailBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        //Log.e("payment", e.getMessage());
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;

                            mData.clear();
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(PaymentDetailActivity.this, dataResultException.errorInfo, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onNext(DataInfo<PaymentDetailBean> paymentDetailBean) {
                        mData.clear();
                        if (paymentDetailBean.success()) {
                            textView.setText(paymentDetailBean.datas().getYear());
                            textView1.setText(paymentDetailBean.datas().getTotalAmount());
                            mData.addAll(paymentDetailBean.datas().getFeeList());
                            mAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(PaymentDetailActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    private void getTenantDetail(String is_shop, String room, String houseid) {
        Subscription subscription = RetrofitHelper.getInstance().getShopPayment(is_shop, status, year, houseid)
                .compose(RxUtil.<DataInfo<PaymentDetailBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<PaymentDetailBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;

                            mData.clear();
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(PaymentDetailActivity.this, dataResultException.errorInfo, Toast.LENGTH_SHORT).show();
                        }else {
                            e.printStackTrace();
                            doFailed();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<PaymentDetailBean> paymentDetailBean) {
                        mData.clear();
                        if (paymentDetailBean.success()) {
                            textView.setText(paymentDetailBean.datas().getYear());
                            textView1.setText(paymentDetailBean.datas().getTotalAmount());

                            mData.addAll(paymentDetailBean.datas().getFeeList());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PaymentDetailActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
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

    private void initList() {
        Log.e("dadadadada",mData+"");
        mAdapter = new CommonAdapter<PaymentDetailBean.BillListBean>(this, mData, R.layout.item_payment_child) {
            @Override
            public void convert(ViewHolder viewHolder, PaymentDetailBean.BillListBean dataBean, int i) {
                viewHolder.setText(R.id.tv_title, dataBean.getName());
                viewHolder.setText(R.id.tv_money, dataBean.getMoney());

            }

        };
        listPaymentDetail.setAdapter(mAdapter);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_detail;
    }

    // 获取标题toolbar
    private void initToolBar() {
        setSupportActionBar(paymentDetailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //显示导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);  //不显示子标题
            paymentDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        paymentDetailToolbar.setNavigationIcon(R.mipmap.nav_icon_back_white);
    }

    //  日期选择器
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        tvTime.setText(getDateToString(millseconds));
        year = getDateToString(millseconds);
        if (isShop == 1) {
            Log.e("各种数据", "isshop" + isShop + "building" + building + "unit" + unit + "room" + room + "year" + year + "status" + status);
            getOwnerDetail("1", building, unit, room, houseid);
        } else {
            getTenantDetail("2", room, houseid);
        }
    }

    SimpleDateFormat sf = new SimpleDateFormat("yyyy");

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

}

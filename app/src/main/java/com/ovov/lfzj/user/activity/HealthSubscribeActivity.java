package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.HealthDialog;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.adapter.GridViewAdapter;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.user.bean.HealthBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

public class HealthSubscribeActivity extends BaseActivity implements OnDateSetListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_choose_time)
    ImageView tvChooseTime;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.orders_time)
    TextView ordersTime;
    @BindView(R.id.tv_orders_time)
    TextView tvOrdersTime;
    private TimePickerDialog mDialogAll;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");      // 设置时间格式
    private List<HealthBean> datalist1 = new ArrayList();
    private GridViewAdapter commonAdapter;
    String timeid;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, HealthSubscribeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_health_subscribe;
    }

    @Override
    public void init() {
        tv_time.setText(getDateToString(System.currentTimeMillis()));
        commonAdapter = new GridViewAdapter(this, datalist1);
        gridview.setAdapter(commonAdapter);

        Subscription subscription = RetrofitHelper.getInstance().getHealthTime("2018-10-10")
                .compose(RxUtil.<ListInfo<HealthBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<HealthBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<HealthBean> listInfoDataInfo) {
                        datalist1.addAll(listInfoDataInfo.datas());
                        commonAdapter.reset(listInfoDataInfo.datas());


                    }

                });
        addSubscrebe(subscription);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!datalist1.get(position).getCheckbox().equals("1")) {
                    datalist1.get(position).getId();
                    Log.e("2313131", datalist1.get(position).getId());
                    ordersTime.setVisibility(View.VISIBLE);
                    tvOrdersTime.setText(tv_time.getText()+"  "+datalist1.get(position).getTime());
                    commonAdapter.changeState(position);
                }
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
                .setTitleStringId("日期选择")
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.blu))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(19)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "year_month_day");
    }

    //  获取时间选择器选择时间
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        tv_time.setText(text);

    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return formatter.format(d);
    }

    @OnClick({R.id.tv_ok, R.id.tv_choose_time})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_ok) {
            updatadata();
        } else {
            showDatePicker();

        }

    }

    private void updatadata() {
        Subscription subscription = RetrofitHelper.getInstance().getHealthOrder(timeid, tv_time.getText().toString())
                .compose(RxUtil.<BannerBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BannerBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(BannerBean listInfoDataInfo) {
                        if (listInfoDataInfo.getCode().equals("200")) {
                            showToast(listInfoDataInfo.getDatas());
                        }
                        //上传网络数据
                        HealthDialog healthDialog = new HealthDialog(HealthSubscribeActivity.this);

                        healthDialog.show();


                    }

                });
        addSubscrebe(subscription);
    }


}

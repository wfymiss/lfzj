package com.ovov.lfzj.home.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.PayResultEvent;
import com.ovov.lfzj.event.PaymentEvent;

import com.ovov.lfzj.home.bean.PaymentDetailResult;

import com.ovov.lfzj.home.payment.presenter.PaymentDetailPresenter;

import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 物业缴费页面
 * Created by Administrator on 2017/9/25.
 */

public class PayMentPayActivity extends BaseActivity {

    @BindView(R.id.payment_name)             // 用户姓名
            TextView paymentName;
    @BindView(R.id.payment_charge)          // 账单费用
            TextView paymentCharge;

    @BindView(R.id.order_pay_commit)        // 支付按钮
            TextView orderPayCommit;
    @BindView(R.id.tv_house_id)              // 房间
            TextView tvHouseName;
    @BindView(R.id.charge_name)              // 账单分类
            TextView chargeName;
    @BindView(R.id.charge_description)      // 账单说明
            TextView chargeDescription;
    @BindView(R.id.payment_pair_fram)       // 已缴费水印图 view
            FrameLayout pair_fram;
    @BindView(R.id.payment_paid_wm)         // 已缴费水印图
            ImageView paid_wm;
    @BindView(R.id.tv_start_time)            // 开始时间
            TextView tvStartTime;
    @BindView(R.id.tv_end_time)              // 截止时间
            TextView tvEndTime;
    @BindView(R.id.tv_charge_num)            // 账单编号
            TextView tvChargeNum;
    @BindView(R.id.re_discount)              // 折扣———rel
            RelativeLayout reDiscount;
    @BindView(R.id.tv_charge)                // 账单合计金额
            TextView tvCharge;
    @BindView(R.id.bill_pay_lay)             // 支付模块———lay
            LinearLayout pay_lay;
    @BindView(R.id.order_under_menu)        // 页面下标题栏——rel
            RelativeLayout under_menu;
    @BindView(R.id.order_pay_amount)        // 实际支付费用
            TextView orderPayAmount;
    @BindView(R.id.tv_discount)
    TextView mTvDiscount;

    private String page_title;
    private String token, sub_id = null;           // token, 小区id
    private String version_pay_num = null;          // APP  版本号
    private String order_id = null;               // 账单id
    private String order_number = null;          // 账单编号
    private String order_state = null;           // 付款状态   paid —— 已付款
    private String order_type = "property";     // 支付类型
    private String orderInfo;

    private PaymentDetailPresenter pres_order;    // 定义获取账单详情方法


    public static void toActivity(Context context, String title, String order_id, String order_state) {
        Intent intent = new Intent(context, PayMentPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("order_id", order_id);
        bundle.putString("order_state", order_state);
        intent.putExtras(bundle);
        context.startActivity(intent);


    }

    @Override
    public void onResume() {
        super.onResume();
        //presenter.getWXPaResult(token, order_id, order_number, order_type, sub_id);          // 订单微信支付回调
        //confirmPay(order_type,order_id,order_number);
    }

    private void confirmPay(String order_type, String order_id, String order_number) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().confirmPayResult(order_type, order_id, order_number)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showError(dataResultException.errorInfo);
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        RxBus.getDefault().post(new PaymentEvent());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_pay;
    }

    @Override
    public void init() {


        storageTokenRead();      // 获取token
        initData();              // 获取页面详情数据
        addRxBusSubscribe(PayResultEvent.class, new Action1<PayResultEvent>() {
            @Override
            public void call(PayResultEvent payResultEvent) {
                confirmPay(order_type, order_id, order_number);
            }
        });
        addRxBusSubscribe(PaymentEvent.class, new Action1<PaymentEvent>() {
            @Override
            public void call(PaymentEvent paymentEvent) {
                finish();
            }
        });
    }


    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf = this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token = spf.getString("token", "");
        sub_id = spf.getString("subId", "");
        version_pay_num = spf.getString("versionPayNum", "");
    }

    // 获取页面详情数据
    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            page_title = bundle.getString("title");
            order_id = bundle.getString("order_id");
            order_state = bundle.getString("order_state");     // 账单支付状态

            // 已付款页———隐藏支付和下标栏
            if (order_state != null && order_state.equals("paid")) {
                pair_fram.setVisibility(View.VISIBLE);   // 已支付水印图——view
                paid_wm.setAlpha((float) 0.88);           // 已支付水印图显示
                pay_lay.setVisibility(View.GONE);         // 隐藏付款模块
                under_menu.setVisibility(View.GONE);
            }
        }
        Log.e("ndijdsdas", order_id + "丁");
        setTitleText(page_title);
        getPaymentDetail(order_id);

    }

    private void getPaymentDetail(String order_id) {
        showLoadingDialog();
        showError(order_id);
        Subscription subscription = RetrofitHelper.getInstance().getPaymentDetail(order_id)
                .compose(RxUtil.<DataInfo<PaymentDetailResult>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<PaymentDetailResult>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<PaymentDetailResult> paymentDetailResultDataInfo) {
                        dismiss();
                        order_number = paymentDetailResultDataInfo.datas().order_number;            // 账单编号
                        DecimalFormat df = new DecimalFormat("0.00");
                        double money = paymentDetailResultDataInfo.datas().money;
                        String newMoney = df.format(money);
                        paymentCharge.setText("￥" + newMoney);         // 账单费用
                        paymentName.setText(paymentDetailResultDataInfo.datas().owner_name);      // 姓名
                        tvHouseName.setText(paymentDetailResultDataInfo.datas().room_number);     // 房间
                        chargeName.setText(paymentDetailResultDataInfo.datas().category_name);    // 账单分类
                        tvStartTime.setText(paymentDetailResultDataInfo.datas().charge_from);     // 开始日期
                        tvEndTime.setText(paymentDetailResultDataInfo.datas().charge_end);        // 截止日期

                        double discount = Double.parseDouble(df.format(paymentDetailResultDataInfo.datas().discount));

                        if (paymentDetailResultDataInfo.datas().discount != 0){
                            mTvDiscount.setText(discount*10+"折");
                            double afterDiscount = discount * paymentDetailResultDataInfo.datas().money;
                            String newAfterDiscount = df.format(afterDiscount);
                            tvCharge.setText("￥" + newAfterDiscount);              // 合计金额
                            orderPayAmount.setText("实付款：" + "￥" + newAfterDiscount);      // 实际支付金额
                        }else {
                            tvCharge.setText("￥" + money);              // 合计金额
                            orderPayAmount.setText("实付款：" + "￥" + money);      // 实际支付金额
                        }


                        tvChargeNum.setText(order_number);    // 账单编号
                        chargeDescription.setText(paymentDetailResultDataInfo.datas().explain);  // 账单说明



                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick({R.id.re_discount, R.id.order_pay_commit,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_discount:              // 折扣
                if (order_state != null && order_state.equals("paid")) {
                    return;
                } else {
                    //Toast.makeText(this, "暂无折扣", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.order_pay_commit:        //  支付、付款
                /*if (orderBtnAlipay.isChecked()) {
                    presenter.aliPayInfo(token, order_id,order_number, sub_id,order_type,version_pay_num);         // 支付宝支付
                } else {
                Log.e("全部数据","token...."+token+".....order_id....."+order_id+".....order_number....."+order_number+".....sub_id....."+sub_id+
                        ".....order_type....."+order_type+"......version_pay_num....."+version_pay_num);
                    presenter.WXPayInfo(token, order_id, order_number, sub_id, order_type,version_pay_num);        // 微信支付
                }*/
                H5PayActivityActivity.toActivity(this, order_type, order_id, order_number);
                break;
            case R.id.iv_back:
                finish();
                break;

            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);     // 注销监听事件
    }

}

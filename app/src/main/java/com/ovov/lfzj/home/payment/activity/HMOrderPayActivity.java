package com.ovov.lfzj.home.payment.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.PayResultEvent;
import com.ovov.lfzj.event.PaymentEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 *  家政订单支付页、用户缴费、 用户缴费合并支付
 */
public class HMOrderPayActivity extends BaseActivity {
    private Unbinder unbinder;

    @BindView(R.id.hm_pay_num)             // 订单号
            TextView pay_num;
    @BindView(R.id.hm_pay_title)          // 订单标题
            TextView pay_title;
    @BindView(R.id.hm_pay_cost)           // 需支付费用
            TextView pay_cost;

    @BindView(R.id.hm_pay_commit)         // 支付
            TextView pay_commit;

    private String token = null;                  // token
    private String sub_id= null;                 // 小区id
    private String order_id = null;              // 订单id
    private String order_num = null;            // 订单编号
    private String order_type = null;           // 付款类型
    private String version_pay_num= null;         // APP  版本号
//    private String type=null;          // 支付类型
    private String title = null;      // 支付内容
    private String cost = null;       // 支付金额
    private String orderInfo;         //  支付宝调起支付返回字符数据

    //private PaymentPayPresenter presenter;  // 定义支付方法

    @Override
    public int getLayoutId() {
        return R.layout.activity_hmorder_pay;
    }

    @Override
    public void init() {
        setTitleText("在线支付");
        initData();              // 参数获取
        addRxBusSubscribe(PayResultEvent.class, new Action1<PayResultEvent>() {
            @Override
            public void call(PayResultEvent payResultEvent) {
                confirmPay(order_type, order_id, "");
            }
        });
    }



    // 参数获取
    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            title=bundle.getString("title");               //  标题
            order_id=bundle.getString("order_id");       //  订单id
            order_num=bundle.getString("order_num");     // 订单号
            order_type=bundle.getString("order_type");  // 订单支付分类
            cost=bundle.getString("order_pay");          // 金额
            pay_num.setText(order_num);
            pay_title.setText(title);
            pay_cost.setText("￥ "+cost);
        }
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
    @OnClick({R.id.hm_pay_commit,R.id.iv_back})
    public void onClickListener(View view){
        switch (view.getId()){

            case R.id.hm_pay_commit:               // 去支付订单
                H5PayActivityActivity.toActivity(this,order_type,order_id,order_num);
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
        EventBus.getDefault().unregister(this);    // 销毁页面关闭回调事件
    }

}

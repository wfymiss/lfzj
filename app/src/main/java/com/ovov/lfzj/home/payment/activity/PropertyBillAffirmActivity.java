package com.ovov.lfzj.home.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.PropertyCheckOrderInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.PropertyBillDetailView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

public class PropertyBillAffirmActivity extends BaseActivity  {
    private Unbinder unbinder;

    /*@BindView(R.id.ab_user_name)
    TextView user_name;
    @BindView(R.id.ab_house)
    TextView ab_house;*/
    @BindView(R.id.bill_item_view)        // 明细列表
            PropertyBillDetailView item_view;
    @BindView(R.id.bill_disc_rel)         //  折扣————rel
            RelativeLayout disc_rel;
    @BindView(R.id.bill_amount)           // 合计金额
            TextView bill_amount;
    @BindView(R.id.bill_pay)              // 合计金额
            TextView bill_pay;
    @BindView(R.id.bill_pm_payment)      // 支付按钮
            TextView bill_payment;


    private List<PropertyPaymentInfo> list = new ArrayList<>();
    private float totalCount = 0;        // 合并支付金额

    private String token, sub_id = null;            // token，小区id
    private String position = null;                // 单条支付账单下标
    private String order_ids = null;               // 合并缴费的账单id
    private String order_type = "prototal";       // 账单支付类型标注———与其他支付区分
    private String order_title = "物业账单缴费";  // 支付标题
    private String type = "prototal";              // 支付类型

    private SpannableStringBuilder builder;
   // private PropertyCheckOrderPresent present;      // 定义提交合并订单方法
   @Override
   public void init() {
       setTitleText("确认账单");
       initData();             // 传值账单信息

   }




   /* // 用户信息
    private void initUserInfo() {
        UserInfoDao userDao = new UserInfoDao(this);
        String tabname = "usertable";
        UserInfo userInfo = userDao.SelectCommenUse(tabname);
        if (userInfo != null) {
            String userName = userInfo.getNickname();
            String building = userInfo.getBuiding();
            String unit = userInfo.getUnit();
            String room = userInfo.getRoom();
            String userAddress = building + "号楼 " + unit + "单元 " + room;
            user_name.setText(userName);       // 用户姓名
            ab_house.setText(userAddress);     // 用户房间
        }
    }*/

    // 传值账单信息
    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getStringExtra("item_pos");   //  单条账单下标
            list = (List<PropertyPaymentInfo>) getIntent().getSerializableExtra("checkList");
        }
        float numCount = 0;              // 总的价格
        if (list != null && list.size() > 0) {
            if (position != null) {
                item_view.setDataOne(list, position);     // 传值明细 list 数据集——单条账单
                int pos = Integer.parseInt(position);
                order_ids = list.get(pos).id;          // 单个缴费的账单id
                numCount = (float) list.get(pos).money;
            } else {
                item_view.setData(list);                  // 传值明细 list 数据集
                for (int i = 0; i < list.size(); i++) {
                    numCount += list.get(i).money;
                    if (i == 0) {
                        order_ids = list.get(i).id;              // 合并缴费的账单id
                    } else {
                        builder = new SpannableStringBuilder();
                        order_ids = order_ids + builder.append("," + list.get(i).id);      // 合并缴费的账单id
                    }
                }
            }
            totalCount = numCount;            // 合并后总金额
            int scale = 2;//设置位数
            int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
            BigDecimal bd = new BigDecimal((double) totalCount);
            bd = bd.setScale(scale, roundingMode);
            totalCount = bd.floatValue();
            bill_amount.setText(String.valueOf(totalCount));    // 合计金额
            bill_pay.setText(String.valueOf(totalCount));       // 支付金额
        }
        //present = new PropertyCheckOrderPresent(this);    // 初始化合并订单方法
    }

    @OnClick({R.id.bill_disc_rel, R.id.bill_pm_payment,R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bill_disc_rel:          // 折扣
               // Toast.makeText(this, "暂无折扣", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bill_pm_payment:       // 确认付款
                //present.setCheckOrder(token, sub_id, order_ids);      // 提交合并的账单
                getCheckOrder();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_property_bill_affirm;
    }

    private void getCheckOrder(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getCheckOrder(order_ids)
                .compose(RxUtil.<DataInfo<PropertyCheckOrderInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<PropertyCheckOrderInfo>>() {
                    @Override
                    public void onError(Throwable e) {

                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(DataInfo<PropertyCheckOrderInfo> propertyCheckOrderInfoDataInfo) {
                        dismiss();
                        Intent on_pay = new Intent(PropertyBillAffirmActivity.this, HMOrderPayActivity.class);      // 跳转支付页面
                        Bundle bundle = new Bundle();
                        bundle.putString("type", type);                                            // 订单类型
                        bundle.putString("title", order_title);                                  // 商品名称
                        bundle.putString("order_id", propertyCheckOrderInfoDataInfo.datas().id);                 // 订单id
                        bundle.putString("order_num", propertyCheckOrderInfoDataInfo.datas().order_number);      // 订单编号
                        bundle.putString("order_pay", propertyCheckOrderInfoDataInfo.datas().money);            //  需支付金额
                        bundle.putString("order_type", order_type);                             //  订单支付分类参数
                        on_pay.putExtras(bundle);
                        startActivity(on_pay);
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }





    // ————————————————————————服务端返回合并订单信息——————————————————
   /* // 提交合并订单返回信息
    @Override
    public void getData(PropertyCheckOrderInfo infoData) {
        if (infoData.getStatus() == 0) {

        }
    }

    // 提交合并账单失败
    @Override
    public void showErrorMessage(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();     //  提交账单失败

    }*/
}

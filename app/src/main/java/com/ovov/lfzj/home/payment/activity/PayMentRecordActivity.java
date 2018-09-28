package com.ovov.lfzj.home.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.home.event.PassValueEvent;
import com.ovov.lfzj.home.payment.fragment.PaymentNoneFragment;
import com.ovov.lfzj.home.payment.fragment.PaymentRecordFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 缴费界面
 */
public class PayMentRecordActivity extends BaseActivity {
    @BindView(R.id.tab_payment_record)
    TabLayout tabPaymentRecord;
    @BindView(R.id.vp_payment_record)
    ViewPager vpPaymentRecord;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private boolean mSelect = false;   //图标是否合并支付


    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_ment_record;
    }

    @Override
    public void init() {
        tvTitle.setText("账单");
        setRightText("编辑");
        /*setTitleText(R.string.text_payment_record);
        setRightText(R.string.text_edit);*/
        initTab();
    }
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, PayMentRecordActivity.class);
        context.startActivity(intent);
    }

    private void initTab() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> listTab = new ArrayList<>();
        listTab.add("未缴账单");
        listTab.add("已缴账单");
        for (int i = 0; i < listTab.size(); i++) {
            tabPaymentRecord.addTab(tabPaymentRecord.newTab().setText(listTab.get(0)));
        }
        fragmentList.add(PaymentNoneFragment.newInstance());     // 未缴费账单
        fragmentList.add(PaymentRecordFragment.newInstance());   // 已缴费账单
        FragmentManager manager = getSupportFragmentManager();
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(getSupportFragmentManager(), fragmentList, listTab);   //绑定fragment
        vpPaymentRecord.setAdapter(adapter);
        tabPaymentRecord.setupWithViewPager(vpPaymentRecord);
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                mSelect = !mSelect;       // 合并按钮状态变化——标注选择相反状态
                RxBus.getDefault().post(new PassValueEvent(mSelect));
                //EventBus.getDefault().post(new PassValueEvent(mSelect));
                break;
        }
    }
}

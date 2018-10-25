package com.ovov.lfzj.property.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import com.ovov.lfzj.property.bean.PaymentQueryBean;
import com.squareup.picasso.Picasso;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PaymentQueryActivity extends BaseActivity {

    @BindView(R.id.payment_query_toolbar)
    Toolbar paymentQueryToolbar;
    @BindView(R.id.list_payment)
    ListView listPayment;
    private List<PaymentQueryBean> mData;
    @Override
    public void init() {
        initToolBar();
        initData();
        initList();
    }

    private void initList() {
        CommonAdapter<PaymentQueryBean> mAdapter = new CommonAdapter<PaymentQueryBean>(this,mData, R.layout.item_payment_query) {
            @Override
            public void convert(ViewHolder viewHolder, PaymentQueryBean paymentQueryBean, int i) {

                ImageView ivPayment = viewHolder.getView(R.id.iv_payment_query_item);
                Picasso.with(PaymentQueryActivity.this).load(paymentQueryBean.getImage()).into(ivPayment);
                viewHolder.setText(R.id.tv_title,paymentQueryBean.getTitle());
                notifyDataSetChanged();
            }
        };
        listPayment.setAdapter(mAdapter);
        listPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(l == 0){
                    OwnerQueryActivity.toActivity(PaymentQueryActivity.this);
                }else {
                    TeanatQueryActivity.toActivity(PaymentQueryActivity.this);
                }
            }
        });
    }

    private void initData() {
        mData = new ArrayList<>();
        PaymentQueryBean paymentQueryBean = new PaymentQueryBean();
        paymentQueryBean.setImage(R.mipmap.icon_owner);
        paymentQueryBean.setTitle("业主");
        mData.add(paymentQueryBean);
        PaymentQueryBean paymentQueryBean1 = new PaymentQueryBean();
        paymentQueryBean1.setImage(R.mipmap.icon_tenant);
        paymentQueryBean1.setTitle("商户");
        mData.add(paymentQueryBean1);

    }

    // 获取标题toolbar
    private void initToolBar() {
        setSupportActionBar(paymentQueryToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //显示导航图标
            getSupportActionBar().setDisplayShowTitleEnabled(false);  //不显示子标题
            paymentQueryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        paymentQueryToolbar.setNavigationIcon(R.mipmap.nav_icon_back_white);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_query;
    }


}

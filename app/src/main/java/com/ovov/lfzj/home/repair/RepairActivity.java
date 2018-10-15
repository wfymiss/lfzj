package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.neighbour.adapter.GridPopupAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepairActivity extends BaseActivity {

    @BindView(R.id.tv_family)
    TextView mTvFamily;
    @BindView(R.id.tv_common)
    TextView mTvCommon;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_location)
    EditText mEtLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.gridView)
    NoScrollGridView mGridView;
    @BindView(R.id.repair_category_water)
    TextView mRepairCategoryWater;
    @BindView(R.id.repair_category_electric)
    TextView mRepairCategoryElectric;
    @BindView(R.id.repair_category_gas)
    TextView mRepairCategoryGas;
    @BindView(R.id.repair_category_hot)
    TextView mRepairCategoryHot;
    @BindView(R.id.repair_category_other)
    TextView mRepairCategoryOther;
    @BindView(R.id.re_repair_item)
    LinearLayout mReRepairItem;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, RepairActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repair;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_property_repair);
        familySelect();
        initgrid();
        mRepairCategoryWater.setSelected(true);
    }

    private void initgrid() {
        GridPopupAdapter mGridAdapter = new GridPopupAdapter(this, 9);
        mGridView.setAdapter(mGridAdapter);
    }

    private void familySelect() {
        mTvFamily.setSelected(true);
        mTvFamily.setTextColor(getResources().getColor(R.color.color_ffffff));
        mTvCommon.setSelected(false);
        mTvCommon.setTextColor(getResources().getColor(R.color.colorPrimary));
        mEtLocation.setVisibility(View.GONE);
        mTvLocation.setVisibility(View.VISIBLE);
        mReRepairItem.setVisibility(View.VISIBLE);
    }

    private void commonSelect() {
        mTvFamily.setSelected(false);
        mTvFamily.setTextColor(getResources().getColor(R.color.colorPrimary));
        mTvCommon.setSelected(true);
        mTvCommon.setTextColor(getResources().getColor(R.color.color_ffffff));
        mEtLocation.setVisibility(View.VISIBLE);
        mTvLocation.setVisibility(View.GONE);
        mReRepairItem.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.tv_family, R.id.tv_common, R.id.tv_location, R.id.tv_commit,R.id.repair_category_water, R.id.repair_category_electric, R.id.repair_category_gas, R.id.repair_category_hot, R.id.repair_category_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_family:
                familySelect();
                break;
            case R.id.tv_common:
                commonSelect();
                break;
            case R.id.tv_location:
                break;
            case R.id.tv_commit:
                WorkOrderConfirmActivity.toActivity(mActivity);
                break;
            case R.id.repair_category_water:
                mRepairCategoryWater.setSelected(true);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                break;
            case R.id.repair_category_electric:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(true);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                break;
            case R.id.repair_category_gas:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(true);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(false);
                break;
            case R.id.repair_category_hot:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(true);
                mRepairCategoryOther.setSelected(false);
                break;
            case R.id.repair_category_other:
                mRepairCategoryWater.setSelected(false);
                mRepairCategoryElectric.setSelected(false);
                mRepairCategoryGas.setSelected(false);
                mRepairCategoryHot.setSelected(false);
                mRepairCategoryOther.setSelected(true);
                break;
        }
    }
}

package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.WorkDetailBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class WorkerOrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_item)
    TextView tvItem;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.gridView)
    NoScrollGridView gridView;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_trouble)
    TextView tvTrouble;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_repair_name)
    TextView tvRepairName;
    @BindView(R.id.lin_item)
    LinearLayout linItem;
    @BindView(R.id.lin_trouble)
    LinearLayout mLinTrouble;
    @BindView(R.id.lin_price)
    LinearLayout mLinPrice;
    @BindView(R.id.lin_repair_name)
    LinearLayout mLinRepairName;
    @BindView(R.id.tv_check)
    TextView mTvCheck;
    private String id;

    public static void toActivity(Context context, int id) {
        Intent intent = new Intent(context, WorkerOrderDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_worker_order_detail;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_worker_order_detail);
        id = String.valueOf(getIntent().getIntExtra("id", 0));
        initData();
    }

    private void initData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getWorkDetail(id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<WorkDetailBean>>() {
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
                    public void onNext(DataInfo<WorkDetailBean> dataInfo) {
                        dismiss();
                        switch (dataInfo.datas().receipt_staff) {
                            case 0:
                                tvStatus.setText("待维修");
                                mLinTrouble.setVisibility(View.GONE);
                                mLinPrice.setVisibility(View.GONE);
                                mLinRepairName.setVisibility(View.GONE);
                                mTvCheck.setVisibility(View.GONE);
                                break;
                            case 1:
                                tvStatus.setText("派单中");
                                break;
                            case 2:
                                tvStatus.setText("维修中");
                                break;
                            case 3:
                                tvStatus.setText("已完成");
                                break;
                            case 4:
                                tvStatus.setText("待评价");
                                break;
                            case 5:
                                tvStatus.setText("已评价");
                                break;
                            case 6:
                                tvStatus.setText("业主取消");
                                break;
                            default:
                                break;
                        }
                        tvName.setText(dataInfo.datas().username);
                        tvPhone.setText(dataInfo.datas().phone);
                        tvType.setText(dataInfo.datas().position_str);
                        tvLocation.setText(dataInfo.datas().address);
                        tvItem.setText(dataInfo.datas().category_name);
                        tvContent.setText(dataInfo.datas().content);
                        tvNumber.setText(dataInfo.datas().order_number);
                        tvTime.setText(dataInfo.datas().repair_time);
                        List<String> mGridData = dataInfo.datas().repair_img;
                        CommonAdapter<String> mAdapter = new CommonAdapter<String>(mActivity, mGridData, R.layout.item_grid_image) {
                            @Override
                            public void convert(ViewHolder viewHolder, String s, int i) {
                                ImageView ivImage = viewHolder.getView(R.id.iv_image);
                                Picasso.with(mActivity).load(s).into(ivImage);
                            }
                        };
                        gridView.setAdapter(mAdapter);
                        if (mGridData.size() > 0) {
                            gridView.setVisibility(View.VISIBLE);
                        } else {
                            gridView.setVisibility(View.GONE);
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    @OnClick({R.id.iv_back, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_check:
                break;
        }
    }
}

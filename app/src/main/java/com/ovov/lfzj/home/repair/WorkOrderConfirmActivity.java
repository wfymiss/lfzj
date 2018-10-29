package com.ovov.lfzj.home.repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.event.RepairSuccessEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;

public class WorkOrderConfirmActivity extends BaseActivity {

    @BindView(R.id.re_number)
    LinearLayout mReNumber;
    @BindView(R.id.lin_umber)
    LinearLayout mLinUmber;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.tv_item)
    TextView mTvItem;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.gridView)
    NoScrollGridView mGridView;
    @BindView(R.id.lin_item)
    LinearLayout mLinItem;
    private RepairContent repairContent;
    private List<File> mData;
    List<MultipartBody.Part>  parts = new ArrayList<>();
    public static void toActivity(Context context, RepairContent repairContent) {
        Intent intent = new Intent(context, WorkOrderConfirmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("repaircontent", repairContent);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_work_order_confirm;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_worker_order_confirm);
        mLinUmber.setVisibility(View.GONE);
        repairContent = (RepairContent) getIntent().getExtras().getSerializable("repaircontent");
        mTvName.setText(repairContent.getName());
        mTvPhone.setText(repairContent.getMobile());
        mTvContent.setText(repairContent.getContent());
        mTvType.setText(repairContent.getRepairArea());
        mTvLocation.setText(repairContent.getRepairLocation());
        if (repairContent.getAreaType() == 0)
            mTvItem.setText(repairContent.getRepairTypeString());
        else
            mLinItem.setVisibility(View.GONE);
        mData = new ArrayList<>();
        mData.addAll(repairContent.getmGrid());
        CommonAdapter<File> mAdapter = new CommonAdapter<File>(mActivity, mData, R.layout.item_grid_image) {
            @Override
            public void convert(ViewHolder viewHolder, File file, int i) {
                ImageView ivImage = viewHolder.getView(R.id.iv_image);
                Picasso.with(mActivity).load(file).into(ivImage);
            }
        };
        mGridView.setAdapter(mAdapter);
        if (mData.size()> 0){
            mGridView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_revise, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_revise:
                finish();
                break;
            case R.id.tv_commit:
                addWorkOrder();
                break;
        }
    }

    private void addWorkOrder() {
        for (int i = 0; i<mData.size();i++){
          MultipartBody.Part part = MultipartBody.Part.createFormData("repair_img[]", mData.get(i).getName(), RequestBody.create(null, mData.get(i)));
          parts.add(part);
        }
        showLoadingDialog();
        Log.e("repair",repairContent.getRepairType()+"repairmobile:"+repairContent.getMobile());
        Subscription subscription = RetrofitHelper.getInstance().workAdd(repairContent.getMobile(),repairContent.getName(),repairContent.getHouse_path(),repairContent.getAreaType(),repairContent.getRepairType(),repairContent.getContent(),parts)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("提交成功");
                        WorkerOrderActivity.toActivity(mActivity);
                        finish();
                        RxBus.getDefault().post(new RepairSuccessEvent());

                    }
                });
        addSubscrebe(subscription);
    }


}

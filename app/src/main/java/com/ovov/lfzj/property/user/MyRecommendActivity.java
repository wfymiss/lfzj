package com.ovov.lfzj.property.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.RecommendListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class MyRecommendActivity extends BaseActivity {

    @BindView(R.id.list_recommend)
    ListView mListRecommend;
    private List<RecommendListInfo> mData;
    private CommonAdapter<RecommendListInfo> mAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MyRecommendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_recommend;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_recommend);
        initList();
        initData();
    }

    private void initData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getInviterList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<RecommendListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ListInfo<RecommendListInfo> listInfo) {
                        dismiss();
                        setRightText("合计人数:"+listInfo.datas().size());
                        mData.clear();
                        mData.addAll(listInfo.datas());
                        mAdapter.notifyDataSetChanged();


                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {
        mData = new ArrayList<>();
        mAdapter = new CommonAdapter<RecommendListInfo>(mActivity, mData, R.layout.item_recommend) {
            @Override
            public void convert(ViewHolder viewHolder, RecommendListInfo s, int i) {


                viewHolder.setText(R.id.tv_nickname, s.mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

                @SuppressLint("StringFormatMatches") String createAt = String.format(getString(R.string.text_register_time, s.created_at));
                viewHolder.setText(R.id.tv_status, createAt);
                //String mobile = String.format(getString(R.string.text_phone_recommend), s.mobile);
                //viewHolder.setText(R.id.tv_phone, mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                if (s.is_user_auth == 1){
                    viewHolder.setText(R.id.tv_phone,String.format(getString(R.string.text_register_status),"已认证"));
                }else {
                    viewHolder.setText(R.id.tv_phone,String.format(getString(R.string.text_register_status),"未认证"));
                }

            }
        };
        mListRecommend.setAdapter(mAdapter);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

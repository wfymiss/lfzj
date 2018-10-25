package com.ovov.lfzj.home.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MessageDetailActivity extends BaseActivity {

    List<BannerBean> noticeList = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv)
    ListView lv;
    private CommonAdapter newsAdapter;
    String id;

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    public void init() {
        id = getIntent().getExtras().get("id").toString();
        initList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (id==13){
//
//                }
            }
        });
    }

    private void initData() {
        Subscription subscription = RetrofitHelper.getInstance().getInfomation(id)
                .compose(RxUtil.<ListInfo<BannerBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<BannerBean>>() {
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
                    public void onNext(ListInfo<BannerBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            newsAdapter.setDatas(listInfoDataInfo.datas());

                        }
                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {


        newsAdapter = new CommonAdapter<BannerBean>(this, noticeList, getLayout()) {

            @Override
            public void convert(ViewHolder viewHolder, BannerBean noticeBean, final int i) {
                switch (id) {
                    case "1":
                        viewHolder.setText(R.id.tv_title, noticeBean.getTitle());
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());

                        break;
                    case "2":
                        viewHolder.setText(R.id.job_name, "发布人：");
                        viewHolder.setText(R.id.version_numb, "地址：");
                        //viewHolder.setText(R.id.tv_bbs_detail, noticeBean.getTitle());
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_version, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "3":
                        viewHolder.setText(R.id.tv_title, "订单编号：");
                        viewHolder.setText(R.id.job_name, "订单状态：");
                        viewHolder.setText(R.id.tv_title_numb, "订单标号");
                        viewHolder.setText(R.id.version_numb, "收货地址：");
                        viewHolder.setText(R.id.tv_reciver, "状态");
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        viewHolder.setText(R.id.tv_version, "shouhuo");
                        break;
                    case "4":
                        viewHolder.setVisible(R.id.tv_reciver, false);
                        viewHolder.setVisible(R.id.tv_title_numb, false);
                        viewHolder.setText(R.id.job_name, noticeBean.getTitle());
                        viewHolder.setText(R.id.tv_version, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "5":
                        viewHolder.setText(R.id.tv_title, "受理情况：");
                        viewHolder.setVisible(R.id.tv_title_numb, false);
                        viewHolder.setVisible(R.id.tv_version, false);
                        viewHolder.setText(R.id.job_name, "订单状态：");
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.version_numb, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "6":
                        viewHolder.setText(R.id.tv_title, "订单编号：");
                        viewHolder.setText(R.id.job_name, "订单状态：");
                        viewHolder.setVisible(R.id.tv_version, false);
                        viewHolder.setText(R.id.tv_title_numb, "订单标号");
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.version_numb, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "7":
                        viewHolder.setText(R.id.tv_title, "保修项目：");
                        viewHolder.setText(R.id.job_name, "工单状态：");
                        viewHolder.setVisible(R.id.tv_version, false);
                        viewHolder.setText(R.id.tv_title_numb, "项目详情");
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.version_numb, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "8":
                        viewHolder.setText(R.id.tv_title, "受理情况：");
                        viewHolder.setVisible(R.id.tv_title_numb, false);
                        viewHolder.setVisible(R.id.tv_version, false);
                        viewHolder.setText(R.id.job_name, "账单状态：");
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.version_numb, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "9":
                        viewHolder.setText(R.id.tv_title, "您有一件快递待领取：");
                        viewHolder.setVisible(R.id.tv_title_numb, false);
                        viewHolder.setVisible(R.id.tv_version, false);
                        viewHolder.setText(R.id.job_name, "快递公司：");
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.version_numb, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "10":
                        viewHolder.setText(R.id.tv_title, "兴建：");
                        viewHolder.setVisible(R.id.tv_title_numb, false);
                        viewHolder.setVisible(R.id.tv_version, false);
                        viewHolder.setText(R.id.job_name, "回复内容：");
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.version_numb, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;
                    case "11":
                        //viewHolder.setText(R.id.tv_bbs_detail, noticeBean.getTitle());
                        viewHolder.setText(R.id.tv_reciver, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_version, noticeBean.getMessage());
                        viewHolder.setText(R.id.tv_time, noticeBean.getTime());
                        break;

                }


            }
        };
        lv.setAdapter(newsAdapter);
    }
    public int getLayout() {

        if (id.equals(1)) {
            return R.layout.bbs_message_item;
        } else {
            return R.layout.job_message_item;
        }

    }

    ;

}
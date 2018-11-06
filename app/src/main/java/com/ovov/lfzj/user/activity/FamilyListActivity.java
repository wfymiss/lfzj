package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.FamilyInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

public class FamilyListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.lv)
    ListView lv;
    TextView textView;
    private List<FamilyInfo.FailyBean> familyInfo = new ArrayList<>();
    private CommonAdapter<FamilyInfo.FailyBean> commonAdapter;


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, FamilyListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_family_list;
    }

    @Override
    public void init() {
        tvTitle.setText("我的家人");
        tvRight.setText("邀请认证");

        initView();
        initdata();

    }

    private void initView() {
        View addheadlayout = View.inflate(this, R.layout.item_house, null);
         textView= addheadlayout.findViewById(R.id.tv_house_name);
        commonAdapter = new CommonAdapter<FamilyInfo.FailyBean>(this, familyInfo, R.layout.familylist_item) {

            @Override
            public void convert(ViewHolder viewHolder, FamilyInfo.FailyBean noticeBean, final int i) {
                if (i==1){
                    viewHolder.setText(R.id.name, "业主");
                }else {
                    viewHolder.setText(R.id.name, "家人");
                }
                viewHolder.setText(R.id.tv_name, noticeBean.getName());
                viewHolder.setText(R.id.tv_phone, noticeBean.getMobile());
                viewHolder.setOnClickListener(R.id.tv_delect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopu(noticeBean.getUser_id(), noticeBean.getRelative_id());


                    }
                });
            }
        };
        lv.addHeaderView(addheadlayout);
        lv.setAdapter(commonAdapter);


    }

    private void showPopu(String user, String relative) {
        final RemindDialogUtil.Builder dialogBuilder = new RemindDialogUtil.Builder(this);
        dialogBuilder.setContent("确认解除家人吗？");
        final RemindDialogUtil dialog = dialogBuilder.Create();
        dialog.setCanceledOnTouchOutside(false);
        dialogBuilder.setConfirmListener(new RemindDialogUtil.Builder.ConfirmClickListener() {
            @Override
            public void onClickListener() {
                dialog.dismiss();
                delectData(user, relative);
            }
        });
        dialog.show();

        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.68);
        lp.alpha = 0.96f;
        dialog.getWindow().setAttributes(lp);
    }

    private void delectData(String user, String relative) {

        Subscription subscription = RetrofitHelper.getInstance().getActfamilydelete(user, relative)
                .compose(RxUtil.<ListInfo<FamilyInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<FamilyInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            //   doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<FamilyInfo> listInfoDataInfo) {
                        if (listInfoDataInfo.code() == 200) {
                            showToast("解除成功");
                        }

                    }

                });
        addSubscrebe(subscription);

    }

    private void initdata() {
        Subscription subscription = RetrofitHelper.getInstance().getfamilylist()
                .compose(RxUtil.<ListInfo<FamilyInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<FamilyInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            //   doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<FamilyInfo> listInfoDataInfo) {
                        textView.setText(listInfoDataInfo.datas().get(0).getHouses());
                        familyInfo.clear();
                        familyInfo.addAll(listInfoDataInfo.datas().get(0).getInfo());

                        commonAdapter.setDatas(listInfoDataInfo.datas().get(0).getInfo());


                    }

                });
        addSubscrebe(subscription);

    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
            return;
        } else {
            FamilyActivity.toActivity(mActivity);
        }

    }
}

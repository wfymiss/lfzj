package com.ovov.lfzj.user.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.ovov.lfzj.base.DelectDialog;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.FamilyInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.RemindDialogUtil;
import com.ovov.lfzj.base.widget.SucceseDialog;
import com.ovov.lfzj.event.DeleteFamilyEvent;
import com.ovov.lfzj.event.HouseEvent;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.bean.NotifiBean;
import com.ovov.lfzj.home.ui.NoticeDetailActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class FamilyListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.lv)
    ListView lv;
    int pos;
    String relative_id;
    String houses_id;
    private List<FamilyInfo> familyInfo = new ArrayList<>();
    private List<FamilyInfo.FailyBean> newslist = new ArrayList<>();
    private CommonAdapter<FamilyInfo> commonAdapter;
    private CommonAdapter<FamilyInfo.FailyBean> familyAdapter;


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
        addRxBusSubscribe(DeleteFamilyEvent.class, new Action1<DeleteFamilyEvent>() {
            @Override
            public void call(DeleteFamilyEvent identityEvent) {
                delectData(identityEvent.user, identityEvent.relative_id, identityEvent.house);
                pos = identityEvent.pos;
            }
        });
    }

    private void initView() {
        commonAdapter = new CommonAdapter<FamilyInfo>(this, familyInfo, R.layout.item_house) {

            @Override
            public void convert(ViewHolder viewHolder, FamilyInfo noticeBean, final int i) {

                viewHolder.setText(R.id.tv_house_name, LoginUserBean.getInstance().getSubname() + "：" + noticeBean.getHouses());
                NoScrollGridView listView = viewHolder.getView(R.id.lv);
                familyAdapter = new CommonAdapter<FamilyInfo.FailyBean>(mActivity, newslist, R.layout.familylist_item) {

                    @Override
                    public void convert(ViewHolder viewHolder, FamilyInfo.FailyBean failyBean, final int pos) {

                        if (pos == 0) {
                            viewHolder.setText(R.id.name, "业主:");
                            viewHolder.getView(R.id.tv_delect).setVisibility(View.INVISIBLE);
                        } else {
                            viewHolder.setText(R.id.name, "家人:");
                        }

                        viewHolder.setText(R.id.tv_name, failyBean.getName());
                        viewHolder.setText(R.id.tv_phone, failyBean.getMobile());

                        viewHolder.setOnClickListener(R.id.tv_delect, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DelectDialog delectDialog = new DelectDialog(mActivity, failyBean.getUser_id(), failyBean.getHouses_id(), failyBean.getRelative_id(), pos);
                                delectDialog.show();

                            }
                        });

                    }
                };
                listView.setAdapter(familyAdapter);
                familyAdapter.setDatas(noticeBean.getInfo());
            }
        };
        lv.setAdapter(commonAdapter);

    }

    private void delectData(String user, String relative, String house) {

        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getActfamilydelete(user, relative, house)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);

                        } else {
                            //doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(DataInfo listInfoDataInfo) {
                        dismiss();

                        if (listInfoDataInfo.code() == 200) {
                            SucceseDialog succeseDialog = new SucceseDialog(mActivity);
                            succeseDialog.show();
                            familyAdapter.remove(pos);
                            commonAdapter.notifyDataSetChanged();
                            familyAdapter.notifyDataSetChanged();
                            lv.notifyAll();

                        }

                    }

                });
        addSubscrebe(subscription);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
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
                        commonAdapter.setDatas(listInfoDataInfo.datas());


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

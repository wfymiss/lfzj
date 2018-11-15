package com.ovov.lfzj.property.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.banner.BannerAdapter;
import com.ovov.lfzj.base.banner.BannerLayout;

import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.LoginoutDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.event.ReLoginEvent;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.repair.RepairActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.LoginActivity;
import com.ovov.lfzj.property.activity.PaymentQueryActivity;
import com.ovov.lfzj.property.home.adapter.HomeGridAdapter;
import com.ovov.lfzj.property.home.repair.WorkerOrderActivity;
import com.squareup.picasso.Picasso;
import com.youzan.androidsdk.YouzanSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Subscription;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.lfgj_banner)
    BannerLayout lfgjBanner;
    @BindView(R.id.home_lfgj_grid)
    NoScrollGridView homeLfgjGrid;
    TagAliasCallback tagAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
        }
    };
    /*private int[] image = {R.mipmap.lfgj_gd, R.mipmap.lfgj_cb, R.mipmap.lfgj_payfee, R.mipmap.lfgj_jy, R.mipmap.lfgj_db, R.mipmap.lfgj_gg, R.mipmap.lfgj_information, R.mipmap.lfgj_phone, R.mipmap.lfgj_db, R.mipmap.icon_service_dispath, R.mipmap.icon_dispatch,R.mipmap.icon_property_commit_workorder,R.mipmap.icon_service_dispath};
    private int[] title = {R.string.gd, R.string.cb, R.string.payfee, R.string.jy, R.string.schema, R.string.sqgg, R.string.nbtz, R.string.phone, R.string.workflow, R.string.dispatch,R.string.zg_dispatch,R.string.property_commit,R.string.text_express};
*/

    private int[] image = {R.mipmap.lfgj_gd, R.mipmap.lfgj_payfee};
    private int[] title = {R.string.gd,  R.string.payfee};

    private List<Map<String, Object>> data_list;
    private ActivityUtils activityUtils;
    private String token = null;
    private String sub_id = null;
    private BannerAdapter<BannerBean> bannerAdapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {

        isLogin();
        initGrid();
        initBanner();
        getUserInfo();
        initBannerData();
        addRxBusSubscribe(ReLoginEvent.class, new Action1<ReLoginEvent>() {
            @Override
            public void call(ReLoginEvent reLoginEvent) {
                LoginUserBean.getInstance().reset();
                LoginUserBean.getInstance().save();
                LoginActivity.toActivity(mActivity);
                JPushInterface.setAlias(mActivity, "", tagAliasCallback);                                //  极光
                YouzanSDK.userLogout(mActivity);
                mActivity.finish();
            }
        });

    }

    private void getUserInfo() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().gethomeList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SubListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SubListBean subListBean) {
                        dismiss();
                        LoginUserBean.getInstance().setUserInfoBean(subListBean.getDatas().getUser());
                        LoginUserBean.getInstance().setAppPermission(subListBean.getDatas().getApp_permission());
                        LoginUserBean.getInstance().setSub_id(subListBean.getDatas().admin_roles.subdistrict_id);
                        LoginUserBean.getInstance().save();

                    }
                });
        addSubscrebe(subscription);
    }

    private void initBanner() {
        bannerAdapter = new BannerAdapter<BannerBean>() {
             @Override
             protected void bind(ViewHolder holder, BannerBean data) {
                 Picasso.with(getContext()).load(data.getImg()).into(holder.mImageView);
             }

         };
       lfgjBanner.setAdapter(bannerAdapter);
    }
    private void initBannerData() {
        Subscription subscription = RetrofitHelper.getInstance().getBanner()
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
                        bannerAdapter.reset(listInfoDataInfo.datas());


                    }

                });
        addSubscrebe(subscription);


    }
    private void initGrid() {

        data_list = new ArrayList<>();
        getbtnData();

        HomeGridAdapter adapter = new HomeGridAdapter(data_list, getContext());
        homeLfgjGrid.setAdapter(adapter);
        homeLfgjGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent;
                switch (position) {
                    case 0:
                        //维修工单界面
                        WorkerOrderActivity.toActivity(getContext());
                        break;
                    case 1:
                        if (activityUtils == null) {
                            activityUtils = new ActivityUtils(HomeFragment.this);
                        }
                        activityUtils.startActivity(PaymentQueryActivity.class);
                        break;
                    case 2:

                        break;
                    case 3:
                        //投诉建议界面
                        break;
                    case 4:              //   备忘事项
                        break;
                    case 5:
                        // 社区公告
                        break;
                    case 6:
                        // 内部通知
                        break;
                    case 7:
                        //常用电话界面
                        break;
                    case 8:
                        // 工作流程
                        break;
                    case 9:
                        //维修工单界面
                        break;
                    case 10:
                        //维修工单界面
                        break;
                    case 11:
                        break;
                    case 12:
                        break;
                    default:
                }
            }
        });
    }

    // 首页GridView 功能菜单
    public List<Map<String, Object>> getbtnData() {
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", title[i]);
            data_list.add(map);
        }
        return data_list;
    }

    private void isLogin() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().isLogin()
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            LoginoutDialog loginoutDialog = new LoginoutDialog(mActivity);
                            loginoutDialog.show();
                        } else {

                            doFailed();
                        }


                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

}

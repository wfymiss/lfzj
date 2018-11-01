package com.ovov.lfzj.home.presenter;


import android.content.Context;
import android.util.Log;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.home.HomeFragment;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.bean.NotifiBean;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.view.HomeView;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import cn.jpush.android.api.JPushInterface;
import rx.Subscription;

public class HomePresenter {
    private HomeView homeView;
    public Context mContext;

    public HomePresenter(HomeView Context) {
        this.homeView = Context;
    }


    public void getNoticeList() {
        Subscription subscription = RetrofitHelper.getInstance().getNoticeList()
                .compose(RxUtil.<ListInfo<NewsBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<NewsBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            homeView.showMsg(dataResultException.errorInfo);
                        } else {

                         //   homeView.doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<NewsBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                             homeView.notifiData(listInfoDataInfo.datas());
                        }
                    }
                });
        homeView.addSubscrebe(subscription);

    }


    public void getNewsList() {
        Subscription subscription = RetrofitHelper.getInstance().getNewsList()
                .compose(RxUtil.<ListInfo<NewsBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<NewsBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            homeView.showMsg(dataResultException.errorInfo);
                        } else {

                        //    homeView.doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<NewsBean> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            homeView.NewsData(listInfoDataInfo.datas());
                        }
                    }
                });
        homeView.addSubscrebe(subscription);

    }


    public void gethomeList() {
        Subscription subscription = RetrofitHelper.getInstance().gethomeList()
                .compose(RxUtil.<SubListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SubListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            homeView.showMsg(dataResultException.errorInfo);
                        } else {

                       //     homeView.doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SubListBean listInfoDataInfo) {
                        if (listInfoDataInfo.getCode().equals("200")) {

                            homeView.getsubList(listInfoDataInfo.getDatas().getSubdistricts());
                            LoginUserBean.getInstance().setUserInfoBean(listInfoDataInfo.getDatas().getUser());
                            LoginUserBean.getInstance().save();
                        }
                    }
                });
        homeView.addSubscrebe(subscription);

    }


}

package com.ovov.lfzj.home.view;

import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.bean.NotifiBean;
import com.ovov.lfzj.home.bean.SubdistrictsBean;

import java.util.List;

import rx.Subscription;

/**
 * Created by kaite on 2018/9/20.
 */

public interface HomeView {



    void notifiData(List<NewsBean> listInfo);

    void NewsData(List<NewsBean> listInfo);
    void getsubList( List<SubdistrictsBean> listInfo);

    void showMsg(String dataResult);

    void addSubscrebe(Subscription subscription);

    void doFailed();
}

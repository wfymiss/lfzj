package com.ovov.lfzj.home.event;

import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class BannerEvent {

    private List<String> bannerimage;

    public BannerEvent(List<String> bannerimage) {
        this.bannerimage = bannerimage;
    }

    public void setBannerimage(List<String> bannerimage) {

        this.bannerimage = bannerimage;
    }

    public List<String> getBannerimage() {

        return bannerimage;
    }
}

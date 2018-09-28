package com.ovov.lfzj.home.bean;

import java.util.List;

/**
 * Created by kaite on 2018/9/20.
 */

public class NewsBean {


    /**
     * id : 2
     * title : 细思恐极！你家门禁卡可能网上买得到，谁都可以随便开
     * summary : 你可能还不知道你家的门禁背后竟然隐藏着这样的隐秘，在二手平台上，有租户转手小区门禁卡，记者购得两张广州的卡实测发现，他们都能开门！
     * images : ["http://catel-link.oss-cn-beijing.aliyuncs.com/news/_my.jpg","http://catel-link.oss-cn-beijing.aliyuncs.com/news/_ch.jpg"]
     * created_at : 2018-09-21 12:32:08
     */

    private String id;
    private String title;
    private String summary;
    private String created_at;
    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}

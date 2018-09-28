package com.ovov.lfzj.event;

/**
 * 钥匙列表小区楼宇弹出框监听
 * Created by Administrator on 2017/10/26.
 */

public class BuildingEvent {
    private String build_id;

    public BuildingEvent(String id){
        this.build_id=id;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }
}

package com.ovov.lfzj.event;

/**
 * Created by Administrator on 2017/12/12.
 */

public class BuildingListEvent {
    private String building;
    private String id;

    public BuildingListEvent(String building, String id) {
        this.building = building;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}

package com.ovov.lfzj.event;

/**
 * Created by Administrator on 2017/12/12.
 */

public class BuildingListEvent {
    private String building;
    private String id;

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {

        return index;
    }

    private String index;

    public BuildingListEvent(String building, String id,String index) {
        this.building = building;
        this.id = id;
        this.index =index;
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

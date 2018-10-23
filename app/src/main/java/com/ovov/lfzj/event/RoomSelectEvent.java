package com.ovov.lfzj.event;

/**
 * Created by kaite on 2018/10/22.
 */

public class RoomSelectEvent {
    public String building_name;
    public String unit;
    public String name;

    public RoomSelectEvent(String building_name, String unit, String name) {
        this.building_name = building_name;
        this.unit = unit;
        this.name = name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }
}

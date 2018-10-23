package com.ovov.lfzj.home.repair;

import android.widget.Switch;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kaite on 2018/10/20.
 */

public class RepairContent implements Serializable {
    private String name;
    private String mobile;
    private String repairArea;
    private String repairLocation;
    private int repairType;
    private String content;
    private List<File> mGrid;
    private int areaType;
    private String house_path;

    public String getHouse_path() {
        return house_path;
    }

    public void setHouse_path(String house_path) {
        this.house_path = house_path;
    }

    public int getAreaType() {
        return areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRepairArea() {
        return repairArea;
    }

    public void setRepairArea(String repairArea) {
        this.repairArea = repairArea;
    }

    public String getRepairLocation() {
        return repairLocation;
    }

    public void setRepairLocation(String repairLocation) {
        this.repairLocation = repairLocation;
    }

    public int getRepairType() {
        return repairType;
    }

    public void setRepairType(int repairType) {
        this.repairType = repairType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<File> getmGrid() {
        return mGrid;
    }

    public void setmGrid(List<File> mGrid) {
        this.mGrid = mGrid;
    }

    public String getRepairTypeString() {
        switch (repairType) {
            case 1:
                return "水";
            case 2:
                return "电";
            case 3:
                return "燃气";
            case 4:
                return "暖";
            case 5:
                return "其他";
            default:
                break;
        }
        return null;
    }
}

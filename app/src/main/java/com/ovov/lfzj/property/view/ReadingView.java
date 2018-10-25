package com.ovov.lfzj.property.view;


import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.RoomListResult;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.property.bean.InformMeterResult;
import com.ovov.lfzj.property.bean.MeterResult;
import com.ovov.lfzj.property.bean.ReadMeterResult;

/**
 * Created by Administrator on 2017/12/1.
 */

public interface ReadingView {
    void setMeterType(MeterResult result);
    void setBuildingList(BuildingListResult result);
    void setRoomList(RoomListResult result);
    void setUnitList(UnitListResult result);
    void setInformMeter(InformMeterResult result);
    void setReadMeterResult(ReadMeterResult result);
    void showMsg(String msg);
}

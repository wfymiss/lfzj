package com.ovov.lfzj.property.presenter;

import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.RoomListResult;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.property.bean.MeterResult;
import com.ovov.lfzj.property.view.ReadingView;

import rx.Subscription;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ReadingPresenter {
    private ReadingView readingView;

    public ReadingPresenter(ReadingView readingView) {
        this.readingView = readingView;
    }

    public void getMeterType() {

        Subscription subscription = RetrofitHelper.getInstance().getMeterType()
                .compose(RxUtil.<MeterResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<MeterResult>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            readingView.showMsg(dataResultException.errorInfo);
                        }

                    }

                    @Override
                    public void onNext(MeterResult listInfoDataInfo) {

                        if (listInfoDataInfo.getData().size() != 0) {
                            readingView.setMeterType(listInfoDataInfo);
                        }
                    }

                });
     //   addSubscrebe(subscription);

    }
    public void getBuildingList() {

        Subscription subscription = RetrofitHelper.getInstance().getBuildingList()
                .compose(RxUtil.<BuildingListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BuildingListResult>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            readingView.showMsg(dataResultException.errorInfo);
                        }

                    }

                    @Override
                    public void onNext(BuildingListResult listInfoDataInfo) {

                        if (listInfoDataInfo.getData().size() != 0) {
                            readingView.setBuildingList(listInfoDataInfo);
                        }
                    }

                });
    }


    public void getUnitList( String building) {
        Subscription subscription = RetrofitHelper.getInstance().getUnitList(building)
                .compose(RxUtil.<UnitListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<UnitListResult>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            readingView.showMsg(dataResultException.errorInfo);
                        }

                    }

                    @Override
                    public void onNext(UnitListResult listInfoDataInfo) {

                        if (listInfoDataInfo.getData().size() != 0) {
                            readingView.setUnitList(listInfoDataInfo);
                        }
                    }

                });
    }


    public void getRoomList( String building, String unit) {

        Subscription subscription = RetrofitHelper.getInstance().getRoomList(building,unit)
                .compose(RxUtil.<RoomListResult>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RoomListResult>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            readingView.showMsg(dataResultException.errorInfo);
                        }

                    }

                    @Override
                    public void onNext(RoomListResult listInfoDataInfo) {

                        if (listInfoDataInfo.getDatas().getHouses_list().size() != 0) {
                            readingView.setRoomList(listInfoDataInfo);
                        }
                    }

                });
    }

//    public void getInformMeterResult(String token, String building_id, String unit, String number, String gid) {
//
//        Subscription subscription = RetrofitHelper.getInstance().getinformationMeter(building_id, unit, number, gid)
//                .compose(RxUtil.<InformMeterResult>rxSchedulerHelper())
//                .subscribe(new CommonSubscriber<InformMeterResult>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof DataResultException) {
//                            DataResultException dataResultException = (DataResultException) e;
//                            readingView.showMsg(dataResultException.errorInfo);
//                        }
//
//                    }
//
//                    @Override
//                    public void onNext(InformMeterResult listInfoDataInfo) {
//
//                        if (listInfoDataInfo.getData()!= null) {
//                            readingView.setInformMeter(listInfoDataInfo);
//                        }
//                    }
//
//                });
//
//
//    }

//    public void getReadMeterResult( String houses_id, String value, String meter_number, String gid, String explains, String created_time) {
//
//
//        Subscription subscription = RetrofitHelper.getInstance().getReadMeterResult( houses_id, value, meter_number, gid, explains, created_time)
//                .compose(RxUtil.<ReadMeterResult>rxSchedulerHelper())
//                .subscribe(new CommonSubscriber<ReadMeterResult>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof DataResultException) {
//                            DataResultException dataResultException = (DataResultException) e;
//                            readingView.showMsg(dataResultException.errorInfo);
//                        }
//
//                    }
//
//                    @Override
//                    public void onNext(ReadMeterResult listInfoDataInfo) {
//
//                        if (listInfoDataInfo.getData()!= null) {
//                            readingView.setReadMeterResult(listInfoDataInfo);
//                        }
//                    }
//
//                });
//    }
}

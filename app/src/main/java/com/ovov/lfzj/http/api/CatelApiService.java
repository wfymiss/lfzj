package com.ovov.lfzj.http.api;


import com.ovov.lfzj.base.bean.ActivityUpImageInfo;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.CheckBean;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.FamilyInfo;
import com.ovov.lfzj.base.bean.GoodListBean;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginBean;
import com.ovov.lfzj.base.bean.MobileInfo;
import com.ovov.lfzj.base.bean.OpenLogUpInfo;
import com.ovov.lfzj.base.bean.PropertyCheckOrderInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.bean.RecommendListInfo;
import com.ovov.lfzj.base.bean.RegisterBean;
import com.ovov.lfzj.base.bean.RoomInfo;
import com.ovov.lfzj.base.bean.RoomListInfo;
import com.ovov.lfzj.base.bean.RoomListResult;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.bean.ShopListBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.bean.UnitInfo;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.base.bean.UpdateBean;
import com.ovov.lfzj.base.bean.UrlBean;
import com.ovov.lfzj.base.bean.VisistorRecordResult;
import com.ovov.lfzj.base.bean.WorkDetailBean;
import com.ovov.lfzj.base.bean.WorkOrderListInfo;
import com.ovov.lfzj.base.bean.WorkOrderUpInfo;
import com.ovov.lfzj.base.bean.WorkerListInfo;
import com.ovov.lfzj.base.bean.YouzanLoginBean;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.HealthDetailBean;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.bean.NewsDetailBean;
import com.ovov.lfzj.home.bean.PayInfo;
import com.ovov.lfzj.home.bean.PayResult;
import com.ovov.lfzj.home.bean.PaymentDetailResult;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.bean.WXPayInfo;
import com.ovov.lfzj.home.bean.WxPaySuccessResult;
import com.ovov.lfzj.market.order.bean.ShopBean;
import com.ovov.lfzj.property.bean.InformMeterResult;
import com.ovov.lfzj.property.bean.MeterResult;
import com.ovov.lfzj.property.bean.PaymentDetailBean;
import com.ovov.lfzj.property.bean.ReadMeterResult;
import com.ovov.lfzj.property.bean.ShopListResult;
import com.ovov.lfzj.user.bean.HealthBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author jzxiang
 *         create at 7/6/17 23:16
 */
public interface CatelApiService {

    //String HOST = "http://app.catel-link.com/";

    String HOST = "http://api_test.catel-link.com/";

    @FormUrlEncoded
    @POST("v1/user/login")
    Observable<DataInfo<LoginBean>> login(@Field("mobile") String mobile,
                                          @Field("password") String password);

    @FormUrlEncoded
    @POST("v1/user/register")
    Observable<DataInfo<RegisterBean>> register(@Field("mobile") String mobile,
                                                @Field("captcha") String captcha,
                                                @Field("password") String password,
                                                @Field("inviter") String recommend);

    @Multipart
    @POST("v1/user/addcomment")
    Observable<DataInfo> addNeighbour(@Part("token") RequestBody token,
                                      @Part("userid") RequestBody userid,
                                      @Part("comment") RequestBody comment,
                                      @Part() List<MultipartBody.Part> part,
                                      @Part("subdistrict_id") RequestBody subdistrict_id);

    @FormUrlEncoded
    @POST("v1/user/comment")
    Observable<ListInfo<SquareListInfo>> getSquareList(@Field("token") String token,
                                                       @Field("userid") String userid,
                                                       @Field("page") int page,
                                                       @Field("id") String id,
                                                       @Field("subdistrict_id") String subdistrict_id);

    @FormUrlEncoded
    @POST("v1/user/recommend")
    Observable<ListInfo<SquareListInfo>> getHomeSquareList(@Field("token") String token,
                                                           @Field("page") int page,
                                                           @Field("id") String id,
                                                           @Field("subdistrict_id") String subdistrict_id);

    @FormUrlEncoded
    @POST("v1/user/commentlist")
    Observable<DataInfo<SquareDetailInfo>> getSquareDetail(@Field("token") String token,
                                                           @Field("id") String id);

    @FormUrlEncoded
    @POST("v1/user/addreply")
    Observable<DataInfo> addSquareComment(@Field("token") String token,
                                          @Field("id") String id,
                                          @Field("content") String content);

    @FormUrlEncoded
    @POST("v1/user/addreply")
    Observable<DataInfo> addSquareReply(@Field("token") String token,
                                        @Field("reply_id") String reply_id,
                                        @Field("content") String content);

    @FormUrlEncoded
    @POST("v1/user/mycomment")
    Observable<ListInfo<SquareListInfo>> getUserSquarelist(@Field("token") String token,
                                                           @Field("userid") String userid,
                                                           @Field("page") int page,
                                                           @Field("id") String id);

    @FormUrlEncoded
    @POST("v1/user/commentzan")
    Observable<DataInfo<String>> squareGood(@Field("token") String token,
                                            @Field("id") String id,
                                            @Field("userid") String userid);


    @FormUrlEncoded
    @POST("v1/question/questionInfo")
    Observable<DataInfo<SquareListInfo>> questionInfo(@Field("id") String id);


    @FormUrlEncoded
    @POST("/v1/entrance/getLog")
    Observable<ListInfo<SquareListInfo>> getLog(@Field("token") String token,
                                                @Field("page") int page);

    @FormUrlEncoded
    @POST("v1/feedBack/feedBackLists")
    Observable<ListInfo<SquareListInfo>> getFeedBackLists(@Field("token") String token,
                                                          @Field("page") int page);


    @FormUrlEncoded
    @POST("v1/feedBack/feedBackInfo")
    Observable<DataInfo<SquareListInfo>> getFeedBackInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("v1/question/questionLists")
    Observable<ListInfo<SquareListInfo>> questionLists(@Field("page") int page);


    @FormUrlEncoded
    @POST("v1/user/loginout")
    Observable<DataInfo> loginout(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/user/is_login")
    Observable<DataInfo> isLogin(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/user/forwardcomment")
    Observable<DataInfo> transimSquare(@Field("token") String token,
                                       @Field("userid") String userid,
                                       @Field("id") String id,
                                       @Field("comment") String comment,
                                       @Field("subdistrict_id") String subdistrict_id);

    // 上传开门日志
    @FormUrlEncoded
    @POST("v1/entrance/logRecord")
    Observable<OpenLogUpInfo> getUpOpenLog(@Field("token") String token,
                                           @Field("subdistrict_id") String sub_id,
                                           @Field("phone") String phone,
                                           @Field("sn_name") String sn_name,
                                           @Field("link") int link,
                                           @Field("result") int result);

    @FormUrlEncoded
    @POST("/v1/entrance/getSubdistrict_buildings")
    Observable<BuildingListResult> getBuildingList(@Field("subdistrict_id") String token);

    // 业主工单验收、确认
    @FormUrlEncoded
    @POST("front/property/index/wo_completed")
    Observable<ServerFeedBackInfo> setOrderConfirm(@Field("token") String tokern,
                                                   @Field("wo_id") String order_id,
                                                   @Field("subdistrict_id") String sub_id);

    // 工单维修评论
    @FormUrlEncoded
    @POST("front/property/index/wo_feedback")
    Observable<ServerFeedBackInfo> setWorkComment(@Field("token") String token,
                                                  @Field("subdistrict_id") String sub_id,
                                                  @Field("wo_id") String wo_id,
                                                  @Field("suggest") String content,
                                                  @Field("rating") int rating);

    //获取单元
    @FormUrlEncoded
    @POST("v1/user/get_unit_list")
    Observable<UnitListResult> getUnitList(@Field("building") String building,
                                           @Field("subdistrict_id") String subdistrict_id);


    //上传反馈
    @FormUrlEncoded
    @POST("v1/feedBack/feedBackAdd")
    Observable<DataInfo<UrlBean>> Upload(@Field("token") String token,
                                         @Field("text") String text);

    //获取业主抄表账单详情
    @FormUrlEncoded
    @POST("front/company/index/informationMeter")
    Observable<InformMeterResult> getinformationMeter(@Field("token") String token,
                                                      @Field("building_id") String building_id,
                                                      @Field("unit") String unit,
                                                      @Field("number") String number,
                                                      @Field("gid") String gid);

    @FormUrlEncoded
    @POST("v1/bills/shopLists")
    Observable<ShopListResult> getDJShopList(@Field("subdistrict_id") String subdistrict_id);


    @FormUrlEncoded
    @POST("v1/bills/billsLists")
    Observable<DataInfo<PaymentDetailBean>> getOwnerPayment(@Field("house_type") String is_shop,
                                                            @Field("status") int status,
                                                            @Field("year") String year,
                                                            @Field("subdistrict_id") String subdistrict_id,
                                                            @Field("house_id") String house_id);


    @FormUrlEncoded
    @POST("v1/bills/billsLists")
    Observable<DataInfo<PaymentDetailBean>> getShopPayment(@Field("subdistrict_id") String subdistrict_id,
                                                           @Field("house_type") String is_shop,
                                                           @Field("status") int status,
                                                           @Field("year") String year,
                                                           @Field("house_id") String house_id);

    //获取房间
    @FormUrlEncoded
    @POST("v1/user/unit_list")
    Observable<RoomListResult> getRoomList(@Field("subdistrict_id") String subdistrict_id,
                                           @Field("building") String building_id,
                                           @Field("unit_id") String unit_id);


    //管家生成抄表生成账单
    @FormUrlEncoded
    @POST("front/company/index/readMeter")
    Observable<ReadMeterResult> getReadMeterResult(@Field("token") String token,
                                                   @Field("houses_id") String houses_id,
                                                   @Field("value") String value,
                                                   @Field("meter_number") String meter_number,
                                                   @Field("gid") String gid,
                                                   @Field("explains") String explains,
                                                   @Field("created_time") String created_time);

    //楼管家提交工单
    @FormUrlEncoded
    @POST("front/Company/index/orderCommit")
    Observable<WorkOrderUpInfo> propertyCommitWorkOrder(@Field("token") String token,
                                                        @Field("position") String position,
                                                        @Field("category") String category,
                                                        @Field("contact") String contact,
                                                        @Field("desc_img") String desc_img,
                                                        @Field("time") String time,
                                                        @Field("phone") String phone,
                                                        @Field("addr") String addr,
                                                        @Field("content") String content,
                                                        @Field("building") String building,
                                                        @Field("unit") String unit,
                                                        @Field("room") String room);

    // 维修提交工单
    @FormUrlEncoded
    @POST("front/property/index/wo_commit")
    Observable<WorkOrderUpInfo> ownerCommitWorkOrder(@Field("token") String token,
                                                     @Field("category") String category,
                                                     @Field("position") String position,
                                                     @Field("did") String did,
                                                     @Field("contact") String contact,
                                                     @Field("phone") String phone,
                                                     @Field("time") String time,
                                                     @Field("addr") String addr,
                                                     @Field("content") String content,
                                                     @Field("subdistrict_id") String subdistrict_id,
                                                     @Field("desc_img") String list_img);

    //发布活动图片
    @Multipart
    @POST("front/activity/index/upload")
    Observable<ActivityUpImageInfo> upLoadActivityImage(@Part("token") String token, @Part MultipartBody.Part part);


    @FormUrlEncoded
    @POST("v1/charge/orderUndone")
    Observable<ListInfo<PropertyPaymentInfo>> getOrderUndone(@Field("token") String token,
                                                             @Field("page") int order_id,
                                                             @Field("subdistrict_id") String sub_id);


    @FormUrlEncoded
    @POST("v1/recommend/index")
    Observable<ListInfo<BannerBean>> getBanner(@Field("token") String token,
                                               @Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/recommend/index")
    Observable<ListInfo<BannerBean>> getShopList(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/user/get_user_info")
    Observable<SubListBean> getHomeList(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/recommend/infomationlist")
    Observable<ListInfo<BannerBean>> getinfomationlist(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/recommend/infomation")
    Observable<ListInfo<BannerBean>> getInfomation(@Field("token") String token,
                                                   @Field("type_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/recommend/infomationdetail")
    Observable<DataInfo<BannerBean>> getInfomationdetail(@Field("token") String token,
                                                         @Field("id") String sub_id);

    @FormUrlEncoded
    @POST("v1/personal/getfamilylist")
    Observable<ListInfo<FamilyInfo>> getfamilylist(@Field("token") String token,
                                                   @Field("subdistrict_id") String sub_id,
                                                   @Field("type") String type);

    @FormUrlEncoded
    @POST("v1/personal/actfamilydelete")
    Observable<DataInfo> getActfamilydelete(@Field("token") String token,
                                            @Field("subdistrict_id") String sub_id,
                                            @Field("user_id") String user_id,
                                            @Field("relative_id") String relative_id,
                                            @Field("houses_id") String houses_id);


    @FormUrlEncoded
    @POST("v1/examination")
    Observable<ListInfo<HealthBean>> getHealthTime(@Field("token") String token,
                                                   @Field("subdistrictr_id") String sub_id,
                                                   @Field("time") String time);


    @FormUrlEncoded
    @POST("v1/examination/examinationlist")
    Observable<HealthDetailBean> getHealthDetail(@Field("token") String time,
                                                 @Field("subdistrictr_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/examination/examinationadd")
    Observable<BannerBean> getHealthOrder(@Field("token") String token,
                                          @Field("subdistrictr_id") String sub_id,
                                          @Field("time_id") String time_id,
                                          @Field("time") String time);

    // 获取业主缴费账单详情
    @FormUrlEncoded
    @POST("v1/charge/orderDetail")
    Observable<DataInfo<PaymentDetailResult>> getPaymentDetail(@Field("token") String token,
                                                               @Field("orderId") String order_id,
                                                               @Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/charge/feeTotalOrder")
    Observable<DataInfo<PropertyCheckOrderInfo>> getCheckOrder(@Field("token") String token,
                                                               @Field("order_ids") String order_ids,

                                                               @Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/charge/orderHistory")
    Observable<ListInfo<PropertyPaymentInfo>> getAlreadyPay(@Field("token") String token,
                                                            @Field("page") int page,
                                                            @Field("subdistrict_id") String sub_id);


    @FormUrlEncoded
    @POST("v1/charge/confirmOrder")
    Observable<DataInfo> confirmPayResult(@Field("token") String token,
                                          @Field("type") String type,
                                          @Field("order_id") String order_id,
                                          @Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/user/user_auth_step1")
    Observable<DataInfo> authStep1(@Field("token") String token,
                                   @Field("mobile") String mobile,
                                   @Field("captcha") String captcha);

    @FormUrlEncoded
    @POST("v1/user/send_sms")
    Observable<DataInfo> sendSms(@Field("mobile") String mobile,
                                 @Field("type") String type);

    @POST("v1/user/subdistrict_list")
    Observable<ListInfo<SublistInfo>> getSubList();

    @FormUrlEncoded
    @POST("v1/user/building_list")
    Observable<ListInfo<UnitInfo>> getUnit(@Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/user/unit_list")
    Observable<DataInfo<RoomInfo>> getRoom(@Field("subdistrict_id") String sub_id,
                                           @Field("building") String buildingid,
                                           @Field("unit_id") String unit_id);

    @FormUrlEncoded
    @POST("v1/user/user_auth_step2")
    Observable<DataInfo> authStep2(@Field("token") String token,
                                   @Field("house_path") String house_path,
                                   @Field("captcha") String captcha,
                                   @Field("mobile") String mobile);

    @Multipart
    @POST("v1/user/userinfoupdate")
    Observable<DataInfo> updateUserinfo(@Part("token") RequestBody token,
                                        @Part("name") RequestBody name,
                                        @Part("birthday") RequestBody birthday,
                                        @Part("signature") RequestBody sign,
                                        @Part("sex") RequestBody sex,
                                        @Part MultipartBody.Part part);

    @FormUrlEncoded
    @POST("v1/user/change_password")
    Observable<DataInfo> findPwd(@Field("mobile") String mobile,
                                 @Field("password") String password,
                                 @Field("captcha") String captcha);


    @FormUrlEncoded
    @POST("v1/notice/noticeRecommend")
    Observable<ListInfo<NewsBean>> getNoticeList(@Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("v1/notice/noticeList")
    Observable<ListInfo<NewsBean>> getHomeNoticeList(@Field("subdistrict_id") String sub_id,
                                                     @Field("page") int page);

    @FormUrlEncoded
    @POST("v1/news/newsRecommend")
    Observable<ListInfo<NewsBean>> getNewsList(@Field("subdistrict_id") String subdistrict_id);

    @FormUrlEncoded
    @POST("/v1/news/newsList")
    Observable<ListInfo<NewsBean>> getHomeNewsList(@Field("subdistrict_id") String subdistrict_id,
                                                   @Field("page") int page);


    @FormUrlEncoded
    @POST("v1/shop/shop_list")
    Observable<ShopListBean<ShopBean>> getOrderList(@Field("mobileByAccessToken") String accessToken,
                                                    @Field("status") int token,
                                                    @Field("page") int page);


    @FormUrlEncoded
    @POST("v1/news/newsDetail")
    Observable<NewsDetailBean> getNewsDetailList(@Field("id") String id);

    @FormUrlEncoded
    @POST("v1/notice/noticeDetail")
    Observable<NewsDetailBean> getNoticeDetailList(@Field("id") String id);

    // 上传访客通行信息
    @FormUrlEncoded
    @POST("v1/entrance/visitorRecord")
    Observable<ServerFeedBackInfo> getUpVisitorInfo(@Field("token") String token,
                                                    @Field("subdistrict_id") String tel,
                                                    @Field("mobile") String moblie,
                                                    @Field("visitor") String visitor,
                                                    @Field("visitor_tel") String visitor_phone,
                                                    @Field("valid_num") String vaild_num,
                                                    @Field("active_time") String active_time);

    // 访客通行日志
    @FormUrlEncoded
    @POST("v1/entrance/getVisitor")
    Observable<VisistorRecordResult> getVisitorLog(@Field("token") String token,
                                                   @Field("page") int page);

    //下载文件
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);


    @FormUrlEncoded
    @POST("v1/system/is_upgrade")
    Observable<DataInfo<UpdateBean>> checkVersion(@Field("app_id") int app_id,
                                                  @Field("version_id") int versionCode);

    @GET("v1/shop/index")
    Observable<DataInfo<UrlBean>> getMarketUrl(@Query("token") String token);

    @POST("v1/recommend/commodityRecommend")
    Observable<ListInfo<ShopBean>> getShoplist();

    @FormUrlEncoded
    @POST("v1/personal/owneraddfamily")
    Observable<DataInfo> addFamily(@Field("token") String token,
                                   @Field("name") String name,
                                   @Field("mobile") String mobile,
                                   @Field("house_path") String house_path);

    @FormUrlEncoded
    @POST("v1/house/get_user_info")
    Observable<DataInfo<MobileInfo>> getMobile(@Field("token") String token,
                                               @Field("house_path") String house_path);

    @Multipart
    @POST("v1/work/workadd")
    Observable<DataInfo> workAdd(@Part("token") RequestBody token,
                                 @Part("phone") RequestBody phone,
                                 @Part("address") RequestBody address,
                                 @Part("position") RequestBody position,
                                 @Part("category") RequestBody category,
                                 @Part("contents") RequestBody content,
                                 @Part("username") RequestBody username,
                                 @Part("subdistrictId") RequestBody subdistrict_id,
                                 @Part() List<MultipartBody.Part> part);

    @FormUrlEncoded
    @POST("v1/user/get_user_house")
    Observable<ListInfo<RoomListInfo>> getUserHouse(@Field("token") String token,
                                                    @Field("subdistrict_id") String subdistrict_id);

    @FormUrlEncoded
    @POST("v1/work/worklist")
    Observable<ListInfo<WorkOrderListInfo>> getWorkList(@Field("token") String token,
                                                        @Field("page") int page,
                                                        @Field("status") int status);

    @FormUrlEncoded
    @POST("v1/work/workstaff")
    Observable<DataInfo<WorkDetailBean>> getWorkorderDetail(@Field("token") String token,
                                                            @Field("wid") String wid);

    @FormUrlEncoded
    @POST("v1/user/change_role")
    Observable<DataInfo> changeRole(@Field("token") String token,
                                    @Field("login_type") int login_type);

    @POST("v1/work/get_works")
    Observable<ListInfo<WorkerListInfo>> getWorks();

    @FormUrlEncoded
    @POST("v1/work/workdispatch")
    Observable<DataInfo> workDispath(@Field("token") String token,
                                     @Field("wid") String wid,
                                     @Field("worker_id") String worker_id);

    @FormUrlEncoded
    @POST("v1/work/workreceipt")
    Observable<DataInfo> workReceipt(@Field("token") String token,
                                     @Field("wid") String wid);

    @FormUrlEncoded
    @POST("v1/work/workercancels")
    Observable<DataInfo> workCancel(@Field("token") String token,
                                    @Field("wid") String wid,
                                    @Field("contents") String reason,
                                    @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("v1/work/propertycancellation")
    Observable<DataInfo> cancelWorkOrder(@Field("token") String token,
                                         @Field("wid") String wid,
                                         @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("v1/work/confirmwork")
    Observable<DataInfo> workerCommit(@Field("token") String token,
                                      @Field("wid") String wid,
                                      @Field("material_cost") String material_cost,
                                      @Field("failure_briefing") String trouble);

    @FormUrlEncoded
    @POST("v1/work/workcheck")
    Observable<DataInfo<CheckBean>> workerOrderCheck(@Field("token") String token,
                                                     @Field("wid") String wid);

    @FormUrlEncoded
    @POST("v1/work/ownerevaluate")
    Observable<DataInfo> repairComment(@Field("token") String token,
                                       @Field("wid") String wid,
                                       @Field("evaluation_content") String content,
                                       @Field("door_speed") String ratingSpeed,
                                       @Field("service_attitude") String ratingAttitude,
                                       @Field("repair_technology") String ratingTec);

    @FormUrlEncoded
    @POST("v1/work/ownercanceling")
    Observable<DataInfo> ownerCancelWorkerOrder(@Field("token") String token,
                                                @Field("wid") String wid,
                                                @Field("contents") String reason,
                                                @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("v1/user/commentdel")
    Observable<DataInfo> squareDelete(@Field("token") String token,
                                      @Field("id") String id);

    @FormUrlEncoded
    @POST("v1/user/zanlist")
    Observable<ListInfo<GoodListBean>> goodList(@Field("token") String token,
                                                @Field("id") String id);

    @FormUrlEncoded
    @POST("v1/shop/yzLogin")
    Observable<DataInfo<YouzanLoginBean>> youzanLogin(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("v1/user/get_inviter_list")
    Observable<ListInfo<RecommendListInfo>> getInviterList(@Field("token") String token);


}

package com.ovov.lfzj.http.api;


import com.ovov.lfzj.base.bean.ActivityUpImageInfo;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginBean;
import com.ovov.lfzj.base.bean.OpenLogUpInfo;
import com.ovov.lfzj.base.bean.PropertyCheckOrderInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
import com.ovov.lfzj.base.bean.RegisterBean;
import com.ovov.lfzj.base.bean.RoomInfo;
import com.ovov.lfzj.base.bean.RoomListResult;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.bean.ShopListBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.bean.StoreOrderCommentInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.bean.UnitInfo;
import com.ovov.lfzj.base.bean.UnitListResult;
import com.ovov.lfzj.base.bean.UpdateBean;
import com.ovov.lfzj.base.bean.UrlBean;
import com.ovov.lfzj.base.bean.VisistorRecordResult;
import com.ovov.lfzj.base.bean.WorkOrderUpInfo;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.bean.NewsDetailBean;
import com.ovov.lfzj.home.bean.NotifiBean;
import com.ovov.lfzj.home.bean.PayInfo;
import com.ovov.lfzj.home.bean.PayResult;
import com.ovov.lfzj.home.bean.PaymentDetailResult;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.bean.WXPayInfo;
import com.ovov.lfzj.home.bean.WxPaySuccessResult;
import com.ovov.lfzj.market.order.bean.ShopBean;

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

    String HOST = "http://app.catel-link.com/";

    @FormUrlEncoded
    @POST("v1/user/login")
    Observable<DataInfo<LoginBean>> login(@Field("mobile") String mobile,
                                          @Field("password") String password);

    @FormUrlEncoded
    @POST("v1/user/register")
    Observable<DataInfo<RegisterBean>> register(@Field("mobile") String mobile,
                                                @Field("captcha") String captcha,
                                                @Field("password") String password);

    @Multipart
    @POST("v1/user/addcomment")
    Observable<DataInfo> addNeighbour(@Part("token") RequestBody token,
                                      @Part("userid") RequestBody userid,
                                      @Part("comment") RequestBody comment,
                                      @Part() List<MultipartBody.Part> part);

    @FormUrlEncoded
    @POST("v1/user/comment")
    Observable<ListInfo<SquareListInfo>> getSquareList(@Field("token") String token,
                                                       @Field("userid") String userid,
                                                       @Field("page") int page,
                                                       @Field("id") String id);

    @FormUrlEncoded
    @POST("v1/user/recommend")
    Observable<ListInfo<SquareListInfo>> getHomeSquareList(@Field("token") String token,
                                                           @Field("page") int page,
                                                           @Field("id") String id);

    @FormUrlEncoded
    @POST("v1/user/commentlist")
    Observable<DataInfo<SquareDetailInfo>> getSquareDetail(@Field("token") String token,
                                                           @Field("id") String id,
                                                           @Field("userid") String userid);

    @FormUrlEncoded
    @POST("v1/user/addreply")
    Observable<DataInfo> addSquareComment(@Field("token") String token,
                                          @Field("id") String id,
                                          @Field("userid") String userid,
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
                                       @Field("comment") String comment);


    // 上传开门日志
    @FormUrlEncoded
    @POST("/v1/entrance/logRecord")
    Observable<OpenLogUpInfo> getUpOpenLog(@Field("token") String token,
                                           @Field("subdistrict_id") String sub_id,
                                           @Field("sn_name") String sn_name,
                                           @Field("link") String link,
                                           @Field("result") String result,
                                           @Field("phone") String phone);

    @FormUrlEncoded
    @POST("front/company/index/building_list")
    Observable<BuildingListResult> getBuildingList(@Field("token") String token);

    // 业主工单验收、确认
    @FormUrlEncoded
    @POST("front/property/index/wo_completed")
    Observable<ServerFeedBackInfo> setOrderConfirm(@Field("token") String tokern,
                                                   @Field("wo_id") String order_id,
                                                   @Field("subdistrict_id") String sub_id);

    // 工单维修评论
    @FormUrlEncoded
    @POST("front/property/index/wo_feedback")
    Observable<ServerFeedBackInfo> setWorkComment(@Field("token") String token, @Field("subdistrict_id") String sub_id,
                                                  @Field("wo_id") String wo_id, @Field("suggest") String content, @Field("rating") int rating);

    //获取单元
    @FormUrlEncoded
    @POST("front/company/index/unit_list")
    Observable<UnitListResult> getUnitList(@Field("token") String token,
                                           @Field("building") String building);

    //获取房间
    @FormUrlEncoded
    @POST("front/company/index/room_list")
    Observable<RoomListResult> getRoomList(@Field("token") String token,
                                           @Field("building") String building,
                                           @Field("unit") String unit);

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


    // 支付宝支付商品订单接口
    @FormUrlEncoded
    @POST("front/emall/pay/orderHandel")
    Observable<PayInfo> getPayInfo(@Field("token") String token,
                                   @Field("order_id") String orderId,
                                   @Field("order_num") String orderNum,
                                   @Field("type") String payType,
                                   @Field("subdistrict_id") String sub_id,
                                   @Field("version") String version);

    // 支付宝——缴费账单合并支付
    @FormUrlEncoded
    @POST("front/Emall/pay/orderPrototal")
    Observable<PayInfo> getAlipayCheckOrder(@Field("token") String token,
                                            @Field("order_id") String orderId,
                                            @Field("order_num") String order_num,
                                            @Field("type") String type,
                                            @Field("subdistrict_id") String subdistrict_id,
                                            @Field("version") String version_num);

    // 支付宝跳蚤市场商品订单支付接口
    @FormUrlEncoded
    @POST("front/Emall/Pay/fleaOrder")
    Observable<PayInfo> getAlipayPayFleaOrder(@Field("token") String token,
                                              @Field("order_id") String orderId,
                                              @Field("order_num") String order_num,
                                              @Field("type") String type,
                                              @Field("subdistrict_id") String subdistrict_id);

    // 支付宝物业缴费、工单、家政服务订单支付接口
    @FormUrlEncoded
    @POST("front/Emall/Pay/propertyFeeOrder")
    Observable<PayInfo> getAliPayPayOrder(@Field("token") String token,
                                          @Field("order_id") String orderId,
                                          @Field("order_num") String order_num,
                                          @Field("type") String type,
                                          @Field("subdistrict_id") String sub_id);

    //支付宝支付结果请求回调接口
    @FormUrlEncoded
    @POST("front/Emall/Pay/comfirmSuccess")
    Observable<PayResult> getAliPayResult(@Field("token") String token,
                                          @Field("order_id") String order_id,
                                          @Field("order_num") String order_number,
                                          @Field("subdistrict_id") String sub_id,
                                          @Field("type") String type);

    // 微信支付商城商品订单、物业缴费、工单、家政付费接口接口
    @FormUrlEncoded
    @POST("front/Pay/pay/createOrder")
    Observable<WXPayInfo> getWXPayInfo(@Field("token") String token,
                                       @Field("order_id") String order_id,
                                       @Field("order_num") String order_num,
                                       @Field("type") String type,
                                       @Field("subdistrict_id") String sub_id,
                                       @Field("version") String version_num);

    // 微信支付结果回调
    @FormUrlEncoded
    @POST("front/Pay/pay/confirmOrder")
    Observable<WxPaySuccessResult> getWxPayResult(@Field("token") String token,
                                                  @Field("order_id") String order_id,
                                                  @Field("order_num") String order_num,
                                                  @Field("type") String type,
                                                  @Field("subdistrict_id") String sub_id);


    @FormUrlEncoded
    @POST("v1/charge/orderUndone")
    Observable<ListInfo<PropertyPaymentInfo>> getOrderUndone(@Field("token") String token,
                                                             @Field("page") int order_id,
                                                             @Field("subdistrict_id") String sub_id);


    @FormUrlEncoded
    @POST("v1/recommend/index")
    Observable<ListInfo<BannerBean>> getBanner(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/recommend/index")
    Observable<ListInfo<BannerBean>> getShopList(@Field("token") String token);

    @FormUrlEncoded
    @POST("/v1/user/get_user_info")
    Observable<SubListBean> getHomeList(@Field("token") String token);

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
                                          @Field("order_number") String ordernumber,
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
                                           @Field("building_id") String buildingid,
                                           @Field("unit_id") String unit_id);

    @FormUrlEncoded
    @POST("v1/user/user_auth_step2")
    Observable<DataInfo> authStep2(@Field("token") String token,
                                   @Field("house_path") String house_path);

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
    @POST("/v1/notice/noticeRecommend")
    Observable<ListInfo<NewsBean>> getNoticeList(@Field("subdistrict_id") String sub_id);

    @FormUrlEncoded
    @POST("/v1/news/newsRecommend")
    Observable<ListInfo<NewsBean>> getNewsList(@Field("subdistrict_id") String subdistrict_id);


    @FormUrlEncoded
    @POST("v1/shop/shop_list")
    Observable<ShopListBean<ShopBean>> getOrderList(@Field("mobileByAccessToken") String accessToken,
                                                    @Field("status") int token,
                                                    @Field("page") int page);


    @FormUrlEncoded
    @POST("/v1/news/newsDetail")
    Observable<NewsDetailBean> getNewsDetailList(@Field("id") String id);

    @FormUrlEncoded
    @POST("/v1/notice/noticeDetail")
    Observable<NewsDetailBean> getNoticeDetailList(@Field("id") String id);

    // 上传访客通行信息
    @FormUrlEncoded
    @POST("/v1/entrance/visitorRecord")
    Observable<ServerFeedBackInfo> getUpVisitorInfo(@Field("token") String token,
                                                    @Field("subdistrict_id") String tel,
                                                    @Field("mobile" )String moblie,
                                                    @Field("visitor") String visitor,
                                                    @Field("visitor_tel") String visitor_phone,
                                                    @Field("valid_num") String vaild_num,
                                                    @Field("active_time") String active_time);

    // 访客通行日志
    @FormUrlEncoded
    @POST("/v1/entrance/getVisitor")
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


}

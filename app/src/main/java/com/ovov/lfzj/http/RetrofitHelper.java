package com.ovov.lfzj.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ovov.lfzj.base.BaseApplication;
import com.ovov.lfzj.base.bean.ActivityUpImageInfo;
import com.ovov.lfzj.base.bean.BuildingListResult;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.GoodListBean;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginBean;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.MobileInfo;
import com.ovov.lfzj.base.bean.OpenLogUpInfo;
import com.ovov.lfzj.base.bean.PropertyCheckOrderInfo;
import com.ovov.lfzj.base.bean.PropertyPaymentInfo;
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
import com.ovov.lfzj.base.utils.NetWorkUtil;
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
import com.ovov.lfzj.http.api.CatelApiService;
import com.ovov.lfzj.market.order.bean.ShopBean;
import com.ovov.lfzj.property.bean.InformMeterResult;
import com.ovov.lfzj.property.bean.MeterResult;
import com.ovov.lfzj.property.bean.PaymentDetailBean;
import com.ovov.lfzj.property.bean.ReadMeterResult;
import com.ovov.lfzj.property.bean.ShopListResult;
import com.ovov.lfzj.user.bean.HealthBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author jzxiang
 *         create at 7/6/17 23:16
 */
public class RetrofitHelper {

    public static final String PATH_DATA = BaseApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
    private static RetrofitHelper mRetrofitHelper = null;
    private static Retrofit mRetrofit;
    private static CatelApiService mApiService;

    private RetrofitHelper() {

    }

    public static RetrofitHelper getInstance() {
        if (mRetrofitHelper == null) {
            synchronized (RetrofitHelper.class) {
                if (mRetrofitHelper == null) mRetrofitHelper = new RetrofitHelper();
                mRetrofit = createRetrofit(new Retrofit.Builder(), createOkHttpClient(new OkHttpClient.Builder()), CatelApiService.HOST);
                mApiService = mRetrofit.create(CatelApiService.class);
            }
        }

        return mRetrofitHelper;
    }

    private static OkHttpClient createOkHttpClient(OkHttpClient.Builder builder) {
        // HttpLoggingInterceptor 打印请求到的json字符串和查看log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        File cacheFile = new File(RetrofitHelper.PATH_CACHE); //缓存文件夹
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); // 创建缓存对象 缓存大小为50M
        // 自定义缓存拦截器
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!NetWorkUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE) // FORCE_CACHE只取本地的缓存 FORCE_NETWORK常量用来强制使用网络请求
                            //.header("x-access-token", LoginUserBean.getInstance().getAccess_token()) // <-- this is the important line
                            .build();
                } else {
                    if (!TextUtils.isEmpty(LoginUserBean.getInstance().getAccess_token())) {
                        Request.Builder requestBuilder = request.newBuilder();
                        //.header("x-access-token", LoginUserBean.getInstance().getAccess_token()); // <-- this is the important line
                        request = requestBuilder.build();
                    }
                }

                Response response = chain.proceed(request);

                if (NetWorkUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };

        builder.addInterceptor(cacheInterceptor);
        //设置缓存
        //builder.addNetworkInterceptor(cacheInterceptor);
        //builder.addInterceptor(cacheInterceptor);
        //builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    private static Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();//使用 gson coverter，统一日期请求格式
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create(gson)) //GsonConverterFactory.create()
                .addConverterFactory(MyGsonConverterFactory.create())
                .build();
    }

    private String getToken() {
        return LoginUserBean.getInstance().getAccess_token();
    }

    private String getUserId() {
        return LoginUserBean.getInstance().getUserId();
    }

    private String getSubId() {
        return LoginUserBean.getInstance().getSub_id();
    }

    public Observable<DataInfo<LoginBean>> login(String mobile, String password) {
        return mApiService.login(mobile, password);
    }

    public Observable<DataInfo<RegisterBean>> register(String mobile, String verify, String password) {
        return mApiService.register(mobile, verify, password);
    }

    public Observable<OpenLogUpInfo> openLogUp(  String sn_name, int open_type, int open_status) {
        return mApiService.getUpOpenLog(getToken(), LoginUserBean.getInstance().getSub_id(),LoginUserBean.getInstance().getPhone(), sn_name, open_type, open_status);

    }

    public Observable<BuildingListResult> getBuildingList() {
        return mApiService.getBuildingList("2");
    }

    public Observable<ServerFeedBackInfo> setOrderConfirm(String orderId, String subId) {
        return mApiService.setOrderConfirm(getToken(), orderId, subId);
    }

    public Observable<ServerFeedBackInfo> addComment(String sub_id, String wo_id, String content, int rat) {
        return mApiService.setWorkComment(getToken(), sub_id, wo_id, content, rat);
    }

    public Observable<UnitListResult> getUnitList(String building) {
        return mApiService.getUnitList(building, "2");
    }


    public Observable<DataInfo<UrlBean>> Upload(String building) {
        return mApiService.Upload(getToken(),building);
    }

    public Observable<RoomListResult> getRoomList(String building, String unit) {
        return mApiService.getRoomList("1[[", building, unit);
    }

    public Observable<WorkOrderUpInfo> ownerCommitWorkeroeder(String category, String posistion, String did, String contact, String phone, String time, String addr, String content, String sub_id, String list_img) {
        return mApiService.ownerCommitWorkOrder(getToken(), category, posistion, did, contact, phone, time, addr, content, sub_id, list_img);
    }

    public Observable<WorkOrderUpInfo> propertyCommitWorkeroeder(String position, String category, String contact, String desc_img, String time, String phone, String addr, String content, String building, String unit, String room) {
        return mApiService.propertyCommitWorkOrder(getToken(), position, category, contact, desc_img, time, phone, addr, content, building, unit, room);
    }

    public Observable<ActivityUpImageInfo> uploadImage(MultipartBody.Part part) {
        return mApiService.upLoadActivityImage(getToken(), part);
    }

    public Observable<DataInfo> addNeighbour(RequestBody content, List<MultipartBody.Part> parts) {
        RequestBody mUserId = RequestBody.create(MediaType.parse("multipart/form-data"), getUserId());
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), getToken());
        return mApiService.addNeighbour(token, mUserId, content, parts);

    }

    public Observable<ListInfo<SquareListInfo>> getSquareList(int page, String id) {
        return mApiService.getSquareList(getToken(), getUserId(), page, id);
    }

    public Observable<DataInfo<SquareDetailInfo>> getSquareDetail(String id) {
        return mApiService.getSquareDetail(getToken(), id);
    }

    public Observable<ListInfo<SquareListInfo>> getHomeSquareList(int page, String id) {
        return mApiService.getHomeSquareList(getToken(), page, id);
    }

    public Observable<ListInfo<SquareListInfo>> getLog(int page) {
        return mApiService.getLog(getToken(), page);
    }

    public Observable<ListInfo<SquareListInfo>> getFeedBackLists(int page) {
        return mApiService.getFeedBackLists(getToken(), page);
    }
    public Observable<DataInfo<SquareListInfo>> getFeedBackInfo(String page) {
        return mApiService.getFeedBackInfo(page);
    }
    public Observable<ListInfo<SquareListInfo>> questionLists(int page) {
        return mApiService.questionLists( page);
    }


    public Observable<DataInfo> addSquareComment(String id, String content) {
        return mApiService.addSquareComment(getToken(), id,content);
    }

    public Observable<DataInfo<String>> squareGood(String id) {
        return mApiService.squareGood(getToken(), id, getUserId());
    }
  public Observable<DataInfo<SquareListInfo>> questionInfo(String id) {
        return mApiService.questionInfo( id);
    }

    public Observable<DataInfo> loginout() {
        return mApiService.loginout(getToken());
    }

    public Observable<DataInfo> isLogin() {
        return mApiService.isLogin(getToken());
    }

    public Observable<DataInfo> transimSquare(String id, String content) {
        return mApiService.transimSquare(getToken(), getUserId(), id, content);
    }

    public Observable<ListInfo<SquareListInfo>> getUserSquareList(String userid, int page, String id) {
        return mApiService.getUserSquarelist(getToken(), userid, page, id);
    }

    public Observable<ListInfo<PropertyPaymentInfo>> getOrderUndone(int page) {
        return mApiService.getOrderUndone(getToken(), page, LoginUserBean.getInstance().getSub_id());
    }


    public Observable<DataInfo<PaymentDetailResult>> getPaymentDetail(String orderid) {
        return mApiService.getPaymentDetail(getToken(), orderid, LoginUserBean.getInstance().getSub_id());
    }

    public Observable<DataInfo<PropertyCheckOrderInfo>> getCheckOrder(String orderids) {
        return mApiService.getCheckOrder(getToken(), orderids, LoginUserBean.getInstance().getSub_id());


    }

    public Observable<ListInfo<PropertyPaymentInfo>> getAlreadyPay(int page) {
        return mApiService.getAlreadyPay(getToken(), page, LoginUserBean.getInstance().getSub_id());


    }

    public Observable<ReadMeterResult> getReadMeterResult(String houses_id, String value, String meter_number, String gid, String explains, String created_time) {
        return mApiService.getReadMeterResult(getToken(), houses_id, value, meter_number, gid, explains, created_time);


    }

    public Observable<DataInfo> confirmPayResult(String type, String order_id, String
            order_number) {
        return mApiService.confirmPayResult(getToken(), type, order_id, order_number, LoginUserBean.getInstance().getSub_id());


    }

    public Observable<DataInfo> sendSms(String mobile, String type) {
        return mApiService.sendSms(mobile, type);
    }

    public Observable<DataInfo> authStep1(String mobile, String verfy) {
        return mApiService.authStep1(getToken(), mobile, verfy);
    }

    public Observable<ListInfo<SublistInfo>> getSubList() {
        return mApiService.getSubList();
    }

    public Observable<ListInfo<UnitInfo>> getUnit(String sub_id) {
        return mApiService.getUnit(sub_id);
    }

    public Observable<DataInfo<RoomInfo>> getRoom(String sub_id, String
            building_id, String unit_id) {
        return mApiService.getRoom(sub_id, building_id, unit_id);
    }

    public Observable<DataInfo> authStep2(String house_path, String captcha, String mobile) {
        return mApiService.authStep2(getToken(), house_path, captcha, mobile);
    }

    public Observable<DataInfo> userInfoUpdate(String name, String birthday, String
            sign, String sex, MultipartBody.Part part) {
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), getToken());
        RequestBody nickname = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody mBirthday = RequestBody.create(MediaType.parse("multipart/form-data"), birthday);
        RequestBody mSign = RequestBody.create(MediaType.parse("multipart/form-data"), sign);
        RequestBody mSex = RequestBody.create(MediaType.parse("multipart/form-data"), sex);
        return mApiService.updateUserinfo(token, nickname, mBirthday, mSign, mSex, part);

    }

    public Observable<ListInfo<BannerBean>> getBanner() {
        return mApiService.getBanner(getToken());
    }

    public Observable<ListInfo<HealthBean>> getHealthTime(String time) {
        return mApiService.getHealthTime(getToken(), "1", time);
    }

    public Observable<HealthDetailBean> getHealthDetail() {
        return mApiService.getHealthDetail(getToken(), "1");
    }

    public Observable<BannerBean> getHealthOrder(String Time_id, String time) {
        return mApiService.getHealthOrder(getToken(), "1", Time_id, time);
    }

    public Observable<SubListBean> gethomeList() {
        return mApiService.getHomeList(getToken());
    }

    public Observable<DataInfo> findPwd(String mobile, String pwd, String verfy) {
        return mApiService.findPwd(mobile, pwd, verfy);
    }

    public Observable<ListInfo<BannerBean>> getinfomationlist() {
        return mApiService.getinfomationlist(getToken());
    }

    public Observable<ListInfo<BannerBean>> getInfomation(String id) {
        return mApiService.getInfomation(getToken(), id);
    }
    public Observable<DataInfo<BannerBean>> getInfomationdetail(String id) {
        return mApiService.getInfomationdetail(getToken(), id);
    }

    public Observable<ListInfo<NewsBean>> getNoticeList() {
        return mApiService.getNoticeList(LoginUserBean.getInstance().getSub_id());
    }

    public Observable<ListInfo<NewsBean>> getNewsList() {
        return mApiService.getNewsList(LoginUserBean.getInstance().getSub_id());
    }

    public Observable<NewsDetailBean> getNewsDetailList(String id) {
        return mApiService.getNewsDetailList(id);
    }

    public Observable<NewsDetailBean> getNoticeDetailList(String id) {
        return mApiService.getNoticeDetailList(id);
    }

    public Observable<ShopListBean<ShopBean>> getOrderList(int page, int status) {
        return mApiService.getOrderList(LoginUserBean.getInstance().getPhone(), status, page);
    }

    public Observable<ServerFeedBackInfo> getUpVisitorInfo(String sub_id, String v_name,
                                                           String v_phone, String v_num, String active_time) {
        return mApiService.getUpVisitorInfo(getToken(), sub_id, LoginUserBean.getInstance().getPhone(), v_name, v_phone, v_num, active_time);

    }

    public Observable<VisistorRecordResult> getVisitorLog(int page) {
        return mApiService.getVisitorLog(getToken(), page);
    }

    public Observable<DataInfo<UpdateBean>> checkVersion(int versionCode) {
        return mApiService.checkVersion(1, versionCode);
    }

    public Observable<DataInfo<UrlBean>> getMarketUrl() {
        return mApiService.getMarketUrl(getToken());
    }


    public Observable<ShopListResult> getDJShopList() {
        return mApiService.getDJShopList("1");
    }

    public Observable<DataInfo<PaymentDetailBean>> getOwnerPayment(String is_shop, int status, String year, String houseid) {
        return mApiService.getOwnerPayment(is_shop, status, year, "1", houseid);
    }

    public Observable<DataInfo> addFamily(String name, String mobile) {
        return mApiService.addFamily(getToken(), getSubId(), name, mobile);
    }

    public Observable<DataInfo<PaymentDetailBean>> getShopPayment(String is_shop, int status, String year, String houseid) {
        return mApiService.getShopPayment("1", is_shop, status, year, houseid);
    }

    public Observable<InformMeterResult> getinformationMeter(String building_id, String unit, String number, String gid) {
        return mApiService.getinformationMeter(getToken(), building_id, unit, number, gid);
    }

    public Observable<DataInfo<MobileInfo>> getMobile(String house_path) {
        return mApiService.getMobile(getToken(), house_path);
    }

    public Observable<ListInfo<RoomListInfo>> getUserHouse() {
        return mApiService.getUserHouse(getToken(), getSubId());
    }

    public Observable<ListInfo<WorkOrderListInfo>> getWorkList(int status, int page) {
        return mApiService.getWorkList(getToken(), page, status);
    }


    public Observable<ListInfo<WorkerListInfo>> getWorks() {
        return mApiService.getWorks();
    }

    public Observable<DataInfo> workerDispath(String wid, String worker_id) {
        return mApiService.workDispath(getToken(), wid, worker_id);
    }

    public Observable<DataInfo> workAdd(String phone, String name, String address, int posistion, int category, String content, List<MultipartBody.Part> parts) {
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), getToken());
        RequestBody mName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody mAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody mPosistion = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(posistion));
        RequestBody mCategory = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(category));
        RequestBody mContent = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        RequestBody mPhone = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
        RequestBody mSubId = RequestBody.create(MediaType.parse("multipart/form-data"), getSubId());

        Log.e("accesstoken", getSubId());
        return mApiService.workAdd(token, mPhone, mAddress, mPosistion, mCategory, mContent, mName, mSubId, parts);

    }


    public Observable<DataInfo<WorkDetailBean>> getWorkDetail(String wid) {
        return mApiService.getWorkorderDetail(getToken(), wid);
    }

    public Observable<DataInfo> changeRole(int login_type) {
        return mApiService.changeRole(getToken(), login_type);
    }
    public Observable<DataInfo> workReciept(String wid){
        return mApiService.workReceipt(getToken(),wid);
    }
    public Observable<DataInfo> workerCancels(String wid,String time,String reason,String remark){
        return mApiService.workCancel(getToken(),wid,reason,remark);
    }

    public Observable<DataInfo> cancelWorkOrder(String wid,String remarks){
        return mApiService.cancelWorkOrder(getToken(),wid,remarks);
    }

    public Observable<DataInfo> workerConfirm(String wid,String cost,String trouble){
        return mApiService.workerCommit(getToken(),wid,cost,trouble);
    }

    public Observable<DataInfo> workerOrderCheck(String wid){
        return mApiService.workerOrderCheck(getToken(),wid);
    }

    public Observable<DataInfo> repairComment(String wid,String content,String ratingSpeed,String ratingAttitude,String ratingTec){
        return mApiService.repairComment(getToken(),wid,content,ratingSpeed,ratingAttitude,ratingTec);
    }

    public Observable<DataInfo> ownerCancelWorkerOrder(String wid,String reason,String remarks){
        return mApiService.ownerCancelWorkerOrder(getToken(),wid,reason,remarks);
    }

    public Observable<DataInfo> addSquareReply(String reply_id,String content){
        return mApiService.addSquareReply(getToken(),reply_id,content);
    }

    public Observable<DataInfo> deleteSquare(String id){
        return mApiService.squareDelete(getToken(),id);
    }

    public Observable<ListInfo<GoodListBean>> getGoodList(String id){
        return mApiService.goodList(getToken(),id);
    }

}

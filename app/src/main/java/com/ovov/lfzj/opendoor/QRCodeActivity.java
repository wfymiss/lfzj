package com.ovov.lfzj.opendoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lingyun.qr.handler.QRUtils;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.event.QRCodeEvent;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.bean.NewsBean;
import com.ovov.lfzj.home.ui.NewsDetailActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;

/**
 * 二维码扫描生成
 */
public class QRCodeActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    ListView recyclerview;
    @BindView(R.id.srly)
    SmartRefreshLayout mActivityListSwf;
    private static int QRC_WIDTH = 600;    //定义二维码长度
    private static int QRC_HEIGHT = 600;   //定义二维码宽度
    private List<String> keys = new ArrayList<String>();
    private String keykey = null;    // 门禁钥匙
    private int page;
    private CommonAdapter mAdaptet;

    private List<NewsBean> noticeList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_qrcode);
        initList();
        mActivityListSwf.setEnableLoadmore(false);
        mActivityListSwf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
                //  initBanner();
            }
        });
        mActivityListSwf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });

        mActivityListSwf.autoRefresh();


//        postKeyList();    //  进入开门页面解析本地钥匙解析本地钥匙
//        initQrCode();     // 生成二维码钥匙

    }

    private void initList() {
        mAdaptet = new CommonAdapter<NewsBean>(QRCodeActivity.this, noticeList, R.layout.news_item) {


            @Override
            public void convert(ViewHolder viewHolder, NewsBean noticeBean, final int i) {
                viewHolder.setText(R.id.tv_title, noticeBean.getTitle());
                viewHolder.setText(R.id.tv_comment, noticeBean.getSummary());
                viewHolder.setText(R.id.tv_time, noticeBean.getCreated_at());

            }
        };
        recyclerview.setAdapter(mAdaptet);
    }

    private void initData(final int type) {
        if (type == REFRESH) {
            page = 1;
        } else if (type == LOADMORE) {
            page = page + 1;
        }

        Subscription subscription = RetrofitHelper.getInstance().getLog(page)
                .compose(RxUtil.<ListInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SquareListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (type == REFRESH) {
                            mActivityListSwf.finishRefresh(false);

                        } else {
                            mActivityListSwf.finishLoadmore(false);
                        }

                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<SquareListInfo> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            if (listInfoDataInfo.datas().size() < 0) {
                                mActivityListSwf.setEnableLoadmore(false);
                            }
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh(true);
                                mAdaptet.setDatas(listInfoDataInfo.datas());

                            } else {
                                mActivityListSwf.finishLoadmore(true);
                                mAdaptet.addDatas(listInfoDataInfo.datas());
                            }
                        } else {
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh(false);

                            } else {
                                mActivityListSwf.finishLoadmore(false);
                            }
                        }


                    }
                });
        addSubscrebe(subscription);


    }

//    private void postKeyList() {
//        SharedPreferences spf = this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
//        String keyjson = spf.getString("keyjson", "");
//        Json(keyjson);     // 解析钥匙
//    }
//
//    //  从本地获取key
//    private void Json(String keyjson) {
//        try {
//            JSONObject object = new JSONObject(keyjson);
//            JSONArray array = object.getJSONArray("datas");
//            if (array != null && array.length() > 0) {
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object2 = array.getJSONObject(i);
//                    keykey = object2.getString("key");
//                    keys.add(keykey);      // 钥匙集合列表
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void initQrCode() {
//        if (keys != null && keys.size() > 0) {
//            QRUtils.loadConfig(this.getApplicationContext());
//            String qrStr = null;
//            qrStr = QRUtils.createDoorControlQR(this,
//                    "0123456788", keys, 10, 10, 0, "12341234");    //  生成钥匙字符串
//            Bitmap contentbitmap = ceateBitmap(qrStr);
//            if (contentbitmap != null) {
//                ivQrcode.setImageBitmap(contentbitmap);       // 显示二维码
//            }
////        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang);
////        Bitmap codebitmap = logoCodeBitmap(contentbitmap, logoBitmap);
////        if (codebitmap != null) {
////            ivQrcode.setImageBitmap(codebitmap);       // 显示二维码（包含logo）
////        }
//        } else {
//            String key_noneMsg = "请在钥匙列表更新钥匙";
//            EventBus.getDefault().post(new QRCodeEvent(key_noneMsg));
//        }
//    }
//
//    // 生成钥匙二维码
//    private Bitmap ceateBitmap(String qrStr) {
//        Bitmap bitmap = null;
//        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        try {
//            BitMatrix matrix = new QRCodeWriter().encode(qrStr, BarcodeFormat.QR_CODE, QRC_WIDTH, QRC_HEIGHT, hints);      //文本，编码格式，长度，宽度，第五个参数可选择，生成文本编码
//            int[] pixs = new int[QRC_WIDTH * QRC_HEIGHT];      // 二维码int 数组
//            for (int y = 0; y < QRC_HEIGHT; y++) {
//                for (int x = 0; x < QRC_WIDTH; x++) {
//                    if (matrix.get(x, y)) {
//                        pixs[y * QRC_WIDTH + x] = 0xff000000;
//                    } else {
//                        pixs[y * QRC_WIDTH + x] = 0xffffffff;
//                    }
//                }
//            }
//            bitmap = Bitmap.createBitmap(QRC_WIDTH, QRC_HEIGHT, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixs, 0, QRC_WIDTH, 0, 0, QRC_WIDTH, QRC_HEIGHT);         // 二维码像素
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    /**
//     * 向二维码中心添加logo 图片
//     *
//     * @param contentbitmap
//     * @param logoBitmap
//     * @return
//     */
//    private Bitmap logoCodeBitmap(Bitmap contentbitmap, Bitmap logoBitmap) {
//        int cBitmapWidth = contentbitmap.getWidth();    //  内容生成二维码的宽度
//        int cBitmapHtight = contentbitmap.getHeight();
//        int logoBitmapWidth = logoBitmap.getWidth();     // 二维码中间图标的高度
//        int logoBitmapHeight = logoBitmap.getHeight();
//        Bitmap blankBitmap = Bitmap.createBitmap(cBitmapWidth, cBitmapHtight, Bitmap.Config.ARGB_8888);   // 创建空的画板图片
//        Canvas canvas = new Canvas(blankBitmap);
//        canvas.drawBitmap(contentbitmap, 0, 0, null);   //将内容生成的二维码画到画板上
//        canvas.save(Canvas.ALL_SAVE_FLAG);   //保存二维码图片
//        float scaleSize = 1.0f;
//        // 获取logo图标的绘制缩放比例
//        while ((logoBitmapWidth / scaleSize) > (cBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (cBitmapHtight / 5)) {
//            scaleSize *= 2;
//        }
//        float sx = 1.0f / scaleSize;    //logo 缩放比例
//        canvas.scale(sx, sx, cBitmapWidth / 2, cBitmapHtight / 2);  //前两个参数表示宽高的缩放比例，大于1表示放大，小于1表示缩小，后两个参数表示缩放的中心点
//        canvas.drawBitmap(logoBitmap, (cBitmapWidth - logoBitmapWidth) / 2, (cBitmapHtight - logoBitmapHeight) / 2, null);
//        ;
//        canvas.restore();  // 调用canvas的restore方法将画布恢复为原来的状态
//        return blankBitmap;
//    }
//


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:

                finish();
                break;
        }
    }
}
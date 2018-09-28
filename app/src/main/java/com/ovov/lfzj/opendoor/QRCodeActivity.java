package com.ovov.lfzj.opendoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lingyun.qr.handler.QRUtils;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.event.QRCodeEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码扫描生成
 */
public class QRCodeActivity extends BaseActivity {
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;

    private static int QRC_WIDTH = 600;    //定义二维码长度
    private static int QRC_HEIGHT = 600;   //定义二维码宽度
    private List<String> keys = new ArrayList<String>();
    private String keykey = null;    // 门禁钥匙


    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_qrcode);
        postKeyList();    //  进入开门页面解析本地钥匙解析本地钥匙
        initQrCode();     // 生成二维码钥匙

    }


    private void postKeyList() {
        SharedPreferences spf = this.getSharedPreferences("key_list", Context.MODE_PRIVATE);
        String keyjson = spf.getString("keyjson", "");
        Json(keyjson);     // 解析钥匙
    }

    //  从本地获取key
    private void Json(String keyjson) {
        try {
            JSONObject object = new JSONObject(keyjson);
            JSONArray array = object.getJSONArray("datas");
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    keykey = object2.getString("key");
                    keys.add(keykey);      // 钥匙集合列表
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initQrCode() {
        if (keys != null && keys.size() > 0) {
            QRUtils.loadConfig(this.getApplicationContext());
            String qrStr = null;
            qrStr = QRUtils.createDoorControlQR(this,
                    "0123456788", keys, 10, 10, 0, "12341234");    //  生成钥匙字符串
            Bitmap contentbitmap = ceateBitmap(qrStr);
            if (contentbitmap != null) {
                ivQrcode.setImageBitmap(contentbitmap);       // 显示二维码
            }
//        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang);
//        Bitmap codebitmap = logoCodeBitmap(contentbitmap, logoBitmap);
//        if (codebitmap != null) {
//            ivQrcode.setImageBitmap(codebitmap);       // 显示二维码（包含logo）
//        }
        } else {
            String key_noneMsg = "请在钥匙列表更新钥匙";
            EventBus.getDefault().post(new QRCodeEvent(key_noneMsg));
        }
    }

    // 生成钥匙二维码
    private Bitmap ceateBitmap(String qrStr) {
        Bitmap bitmap = null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix matrix = new QRCodeWriter().encode(qrStr, BarcodeFormat.QR_CODE, QRC_WIDTH, QRC_HEIGHT, hints);      //文本，编码格式，长度，宽度，第五个参数可选择，生成文本编码
            int[] pixs = new int[QRC_WIDTH * QRC_HEIGHT];      // 二维码int 数组
            for (int y = 0; y < QRC_HEIGHT; y++) {
                for (int x = 0; x < QRC_WIDTH; x++) {
                    if (matrix.get(x, y)) {
                        pixs[y * QRC_WIDTH + x] = 0xff000000;
                    } else {
                        pixs[y * QRC_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QRC_WIDTH, QRC_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixs, 0, QRC_WIDTH, 0, 0, QRC_WIDTH, QRC_HEIGHT);         // 二维码像素
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 向二维码中心添加logo 图片
     *
     * @param contentbitmap
     * @param logoBitmap
     * @return
     */
    private Bitmap logoCodeBitmap(Bitmap contentbitmap, Bitmap logoBitmap) {
        int cBitmapWidth = contentbitmap.getWidth();    //  内容生成二维码的宽度
        int cBitmapHtight = contentbitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();     // 二维码中间图标的高度
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(cBitmapWidth, cBitmapHtight, Bitmap.Config.ARGB_8888);   // 创建空的画板图片
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(contentbitmap, 0, 0, null);   //将内容生成的二维码画到画板上
        canvas.save(Canvas.ALL_SAVE_FLAG);   //保存二维码图片
        float scaleSize = 1.0f;
        // 获取logo图标的绘制缩放比例
        while ((logoBitmapWidth / scaleSize) > (cBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (cBitmapHtight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;    //logo 缩放比例
        canvas.scale(sx, sx, cBitmapWidth / 2, cBitmapHtight / 2);  //前两个参数表示宽高的缩放比例，大于1表示放大，小于1表示缩小，后两个参数表示缩放的中心点
        canvas.drawBitmap(logoBitmap, (cBitmapWidth - logoBitmapWidth) / 2, (cBitmapHtight - logoBitmapHeight) / 2, null);
        ;
        canvas.restore();  // 调用canvas的restore方法将画布恢复为原来的状态
        return blankBitmap;
    }



    @OnClick({R.id.iv_back, R.id.btn_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_refresh:
                initQrCode();
                break;
        }
    }
}
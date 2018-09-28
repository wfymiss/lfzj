package com.ovov.lfzj.base.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ovov.lfzj.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static cn.sharesdk.wechat.friends.Wechat.NAME;
import static com.ovov.lfzj.base.utils.UIUtils.getResources;

/**
 * Created by Administrator on 2018/1/1.
 */

public class ShareAppPopup  extends PopupWindow implements View.OnClickListener{
    private View mView;
    private String url;
    private String title;
    private String content;
    private String pic;
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            dismiss();
            //大部分的回调方法都处于网络线程，因此可以简单默认为回调方法都不在主线程.
            Toast.makeText(mContext, "分享成功！", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("TAG", throwable.getMessage());
            Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };
    private final Context mContext;
    private Bitmap bmm;
    private Bitmap bitmap;

    public ShareAppPopup(Context context, String url, String title, String content, String pic) {
        super(context);
        init(context);   //布局
        setPopupWindow();   //布局属性
        mContext = context;
        this.url = url;
        this.title = title;
        this.content = content;
        this.pic = pic;

    }

    /**
     * 初始化布局
     */
    private void init(Context context) {
        LayoutInflater infalter = LayoutInflater.from(context);
        mView = infalter.inflate(R.layout.layout_share_app, null);    //绑定布局
        mView.findViewById(R.id.re_pyq).setOnClickListener(this);
        mView.findViewById(R.id.re_wechat).setOnClickListener(this);

        mView.findViewById(R.id.tv_cancel_share).setOnClickListener(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);



    }

    private void setPopupWindow() {
        this.setContentView(mView); //设置View
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);  //弹出窗宽度
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT); //弹出高度
        this.setFocusable(true);  //弹出窗可触摸
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));   //设置背景透明
        this.setAnimationStyle(R.style.mypopupwindow_anim_style);   //弹出动画
    }

    private static final String TAG = "ShareAppPopup";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_pyq:

                WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                sp.setTitle(title);  //分享标题
                sp.setText(content);   //分享文本
                sp.setUrl(url);
                sp.setImageData(bitmap);
                //sp.setUrl(url);   //网友点进链接后，可以看到分享的详情
                /*if (!TextUtils.isEmpty("http://file03.sg560.com/upimg01/2016/03/840617/Title/1244507471580637840617.jpg"))
                    sp.setImageUrl("http://file03.sg560.com/upimg01/2016/03/840617/Title/1244507471580637840617.jpg");*/
                //sp.setImagePath(String.valueOf("android.resource://" + mContext.getPackageName() + "/" + R.mipmap.splash_logo));
                Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                wechatMoments.setPlatformActionListener(platformActionListener); // 设置分享事件回调
                // 执行分享
                wechatMoments.share(sp);
                break;
            case R.id.re_wechat:
                Wechat.ShareParams wechatshare = new Wechat.ShareParams();
                wechatshare.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                wechatshare.setTitle(title); //分享标题
                wechatshare.setText(content);   //分享文本
                wechatshare.setUrl(url);
                wechatshare.setImageData(bitmap);
                //wechatshare.setImagePath("android.resource://" + mContext.getPackageName() + "/" + R.mipmap.splash_logo);
                /*if (!TextUtils.isEmpty("http://file03.sg560.com/upimg01/2016/03/840617/Title/1244507471580637840617.jpg"))
                    wechatshare.setImageUrl("http://file03.sg560.com/upimg01/2016/03/840617/Title/1244507471580637840617.jpg");*/
                //wechatshare.setImageUrl(UserBean.getInstance().getHead_pic());
                // wechatshare.setUrl("http://www.baidu.com");   //网友点进链接后，可以看到分享的详情
                Platform wechat = ShareSDK.getPlatform(NAME);
                wechat.setPlatformActionListener(platformActionListener); // 设置分享事件回调
                // 执行分享
                wechat.share(wechatshare);
                break;

            case R.id.tv_cancel_share:
                dismiss();
                break;
        }
    }
}

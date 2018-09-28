package com.ovov.lfzj;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;

import android.support.multidex.MultiDex;
import android.util.Log;


import com.mob.MobSDK;
import com.ovov.lfzj.base.BaseApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


/**
 * Created by jzxiang on 22/12/2017.
 */
public class CatelApplication extends BaseApplication  {
    public static int REFRESH = 1;
    public static int LOADMORE = 2;
    public static int isGood = 1;
    public static int noGood = 0;
    public static int SQUARE_FRAGMENT_IDENTITY = 1;
    public static int SQUARE_DETAIL_IDENTITY = 2;
    public static int HOME_FRAGMENT_IDENTITY = 3;
    public static int MAIN_ACTIVITY_IDENTITY = 4;
    public static int NEIGHBOUR_IDENTITY = 5;
    public static int UAER_FRAGMENT_IDENTITY = 5;


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }


    @Override
    public String getBuglyKey() {
        return null;
    }

    @Override
    public void init() {

        //安卓7.0相机权限判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        MobSDK.init(this);
       /* GSYVideoType.setRenderType(GSYVideoType.TEXTURE);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        MobSDK.init(this);
*/

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

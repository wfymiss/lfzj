package com.ovov.lfzj.base.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;

/**
 * Created by kaite on 2018/10/13.
 */

public class JavaSccriptFinishActivity {
    private Activity mActivity;

    public JavaSccriptFinishActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
    @JavascriptInterface
    public void finish(){
        mActivity.finish();
    }
}

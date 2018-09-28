package com.ovov.lfzj.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;


/**
 * @author 郭阳鹏
 * @date 创建时间：2017/3/13
 * @description 和UI操作相关的类
 */
public class UIUtils {
    private static final String TAG = "UIUtils";

    /**
     * 上下文的获取
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 获取资源
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }


    /**
     * dip2px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(int px) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     * 开启activity
     *
     * @param intent
     */
    public static void startActivity(Intent intent) {
        getContext().startActivity(intent);
    }

    public static void displayToast(String str) {
        Toast.makeText(UIUtils.getContext(), str, Toast.LENGTH_SHORT).show();
    }


    public static void showFailed() {
        displayToast(getContext().getString(R.string.do_failed));
    }

    public static void showSuccess() {
        displayToast(getContext().getString(R.string.do_success));
    }

    public static void viewHanlderWEqualHeight(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        params.height = params.width;
        view.setLayoutParams(params);
    }


    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    public static void openActivity(Activity activity, Class<?> pClass) {
        openActivity(activity, pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    public static void openActivity(Activity activity, Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    public static void openActivity(Activity activity, String pAction) {
        openActivity(activity, pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    public static void openActivity(Activity activity, String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        activity.startActivity(intent);
    }

    /**
     * 添加view
     *
     * @param resId
     * @return
     */
    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 验证邮箱格式是否正确
     */
    public static boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    public static File getFacePath(Context context, String faceId) {
        String file = "/%s.jpg";
        String path = getBaseFile() + String.format(file, faceId);
        Log.e(TAG, "getFacePath: " + path);
        return new File(path);
    }

    public static String getBaseFile() {
//        File fileadhoc = new File(getSDPath()+"/Adhoc");
        String fileDir = "";
        if (hasSdcard()) {
            File fileadhoc = Environment.getExternalStoragePublicDirectory("Makeys");
            if (!fileadhoc.exists()) {
                fileadhoc.mkdirs();
            }
            fileDir = fileadhoc.getAbsolutePath();
        }

        return fileDir;
    }

    // 返回是否安装sdcard并且可以读写
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
//        T.i("SDCARD 挂载状态 " + status);
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    public static boolean showHint(TextView... editTexts) {
        for (TextView editText : editTexts) {
            String text = editText.getText().toString();
            if (TextUtils.isEmpty(text)) {
                String hint = editText.getHint().toString();
                displayToast(hint);
                return true;
            }
        }
        return false;
    }

    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        opt.inSampleSize = 1;

        int size = Math.max(bmpHeght, bmpWidth);

        if (size > 1024) {
            opt.inSampleSize = size / 1024 + 1;
        }
//        if (bmpWidth > bmpHeght) {
//            if (bmpWidth > 1024)
//                opt.inSampleSize = bmpWidth / 1024;
//        } else {
//            if (bmpHeght > 1024)
//                opt.inSampleSize = bmpHeght / 1024;
//        }

        Log.e(TAG, "getScaleBitmap: " + opt.inSampleSize);
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        return bmp;
    }

    public static final int TAKE_PHOTO_REQUEST_CODE = 1001;

    public static File toCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定存储照片的路径
        File file = getTempImage();
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
        return file;
    }

    public static File getTempImage() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory(), "hub_img.jpg");
            try {
                if (!tempFile.exists())
                    tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return tempFile;
        }
        return null;
    }

    public static boolean save(File path, Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        return true;
    }

    public static void changeLocale(String language) {
        Log.e(TAG, "changeLocale: " + language);

        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = getLocal(language);
        resources.updateConfiguration(config, dm);
    }

    private static Locale getLocal(String language) {
        switch (language) {
            case "zh_cn":
                return Locale.CHINA;
            case "zh_tw":
                return Locale.TAIWAN;
        }
        return Locale.getDefault();
    }

//    //白色可以替换成其他浅色系
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static void myStatusBar(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {//MIUI
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
//                    activity.getWindow().setStatusBarColor(Color.WHITE);
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
//                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//                    tintManager.setStatusBarTintEnabled(true);
//                    tintManager.setStatusBarTintResource(android.R.color.white);
//                }
//            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {//Flyme
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
//                    activity.getWindow().setStatusBarColor(Color.WHITE);
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
//                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//                    tintManager.setStatusBarTintEnabled(true);
//                    tintManager.setStatusBarTintResource(android.R.color.white);
//                }
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0
//                activity.getWindow().setStatusBarColor(Color.WHITE);
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//        }
//    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    //删除文件后更新数据库  通知媒体库更新文件夹
    public static void updateFileFromDatabase(Context context, List<String> list) throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] paths = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                paths[i] = list.get(i);
            }
            MediaScannerConnection.scanFile(context, paths, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("TAG", "Scanned " + path + ":");
                    Log.i("TAG", "-> uri=" + uri);
                }
            });
            context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Video.Media.DATA + "=?", paths);

        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }


}

package com.ovov.lfzj;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.ovov.lfzj.base.BaseMainActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.UpdateBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.StatusBarUtils;
import com.ovov.lfzj.base.utils.Tools;
import com.ovov.lfzj.base.widget.CommonProgressDialog;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.base.widget.UpdateDialog;
import com.ovov.lfzj.event.DownloadEvent;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.event.LoginOutEvent;
import com.ovov.lfzj.event.MainIdentityEvent;
import com.ovov.lfzj.event.Recievertype;
import com.ovov.lfzj.event.RevieverEvent;
import com.ovov.lfzj.event.SquareDetailIdentityEvent;
import com.ovov.lfzj.event.SwitchEvent;
import com.ovov.lfzj.home.HomeFragment;
import com.ovov.lfzj.home.payment.activity.PayMentRecordActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.login.LoginActivity;
import com.ovov.lfzj.market.MarketFragment;
import com.ovov.lfzj.neighbour.NeighbourFragment;
import com.ovov.lfzj.opendoor.OpendoorActivity;
import com.ovov.lfzj.user.UserFragment;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.MAIN_ACTIVITY_IDENTITY;

public class MainActivity extends BaseMainActivity {

    private CommonProgressDialog pBar;
    public static final String BASE_FILE = Environment.getExternalStorageDirectory().getPath() + "/lfzj/";
    private String path;
    private static String DOWNLOAD_NAME = "乐福院子";
    private RxPermissions rxPermission;
    private MyReceiver receiver;

    private ActivityUtils activityUtils;

    TagAliasCallback tagAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
        }
    };
    private String phone;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    private void registerBroadcast() {
        // 注册广播接收者
        Log.i("hhhh", "rrrrrrrr");
        receiver = new MyReceiver();
        Log.i("hhhh", "yyyyyyyyyyyyyyyyy");
        IntentFilter filter = new IntentFilter();
        filter.addAction("exit_app");
        this.registerReceiver(receiver, filter);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("hhhh", "dddddddd" + intent.getAction());
            if (intent.getAction().equals("exit_app")) {
                Log.i("hhhh", "ffffffffffff" + intent.getAction());
                finish();
            }
        }
    }

    @Override
    public void init() {
        super.init();
        //极光推送
        registerBroadcast();
        JPushInterface.setAlias(this, phone, tagAliasCallback);                                //  极光
        StatusBarUtils.setStatusBar(this, false, false);
        FileUtils.createOrExistsDir(BASE_FILE);
        initFragment(0);
        switchContent(1, 0);
        for (int i = 0; i < getFragmentCount(); i++) {
            mBottomViews[i] = findViewById(mMenuIds[i]);
            final int finalI = i;
            mBottomViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuItemClicked(finalI);
                }
            });
        }
        rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                        }
                    }
                });

        mBottomViews[0].setSelected(true);
        addRxBusSubscribe(IdentityEvent.class, new Action1<IdentityEvent>() {
            @Override
            public void call(IdentityEvent identityEvent) {
                finish();
            }
        });
        addRxBusSubscribe(MainIdentityEvent.class, new Action1<MainIdentityEvent>() {
            @Override
            public void call(MainIdentityEvent mainIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
                finish();
            }
        });
        addRxBusSubscribe(SquareDetailIdentityEvent.class, new Action1<SquareDetailIdentityEvent>() {
            @Override
            public void call(SquareDetailIdentityEvent toIdentityEvent) {
                finish();
            }
        });
        addRxBusSubscribe(DownloadEvent.class, new Action1<DownloadEvent>() {
            @Override
            public void call(DownloadEvent downloadEvent) {
                pBar = new CommonProgressDialog(MainActivity.this);
                pBar.setCanceledOnTouchOutside(false);
                pBar.setTitle("正在下载");
                pBar.setCustomTitle(LayoutInflater.from(
                        MainActivity.this).inflate(
                        R.layout.title_dialog, null));
                pBar.setMessage("正在下载");
                pBar.setIndeterminate(true);
                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pBar.setCancelable(true);
                // downFile(URLData.DOWNLOAD_URL);
                final DownloadTask downloadTask = new DownloadTask(
                        MainActivity.this);
                downloadTask.execute(downloadEvent.url);
            }
        });
        checkVersion();
        addRxBusSubscribe(SwitchEvent.class, new Action1<SwitchEvent>() {
            @Override
            public void call(SwitchEvent switchEvent) {
                menuItemClicked(1);
            }
        });
        addRxBusSubscribe(LoginOutEvent.class, new Action1<LoginOutEvent>() {
            @Override
            public void call(LoginOutEvent loginOutEvent) {
                LoginUserBean.getInstance().reset();
                LoginUserBean.getInstance().save();
                JPushInterface.deleteAlias(mActivity, 1);
                LoginActivity.toActivity(mActivity);

                for (int i = 0; i < mActivities.size(); i++) {
                    if (mActivities.get(i) != null && !mActivities.get(i).isFinishing()) {
                        mActivities.get(i).finish();
                    }
                }
            }
        });


    }

    private void checkVersion() {
        Subscription subscription = RetrofitHelper.getInstance().checkVersion(Tools.getVersion(mActivity))
                .compose(RxUtil.<DataInfo<UpdateBean>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<UpdateBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(DataInfo<UpdateBean> updateBeanDataInfo) {
                        if (updateBeanDataInfo.datas().needUpdate()) {
                            UpdateDialog updateDialog = new UpdateDialog(mActivity, updateBeanDataInfo.datas().apk_url, updateBeanDataInfo.datas().upgrade_point);
                            updateDialog.show();
                        }
                    }
                });
        addSubscrebe(subscription);
    }


    //收到推送以后点击事件的处理
    @Subscribe
    public void onEventMainThread(RevieverEvent event) {

        if (event.getType().equals(Recievertype.CREATE_FEE)) {
            if (activityUtils == null)
                activityUtils = new ActivityUtils(this);
//            activityUtils.startActivity(PayMentRecordActivity.class);
        } else if (event.getType().equals(Recievertype.OWNER_WORK_ORDER)) {
            if (activityUtils == null)
                activityUtils = new ActivityUtils(this);
//            activityUtils.startActivity(WorkerOrderActivity.class);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getFragmentCount() {
        return 4;
    }

    @Override
    public int getDefaultIndex() {
        return 0;
    }

    @Override
    public void initFragment(int index) {
        if (mFragments[index] != null)
            return;

        switch (index) {
            case 0:
                mFragments[index] = HomeFragment.newInstance();
                break;
            case 1:
                mFragments[index] = MarketFragment.newInstance();
                break;
            case 2:
                mFragments[index] = NeighbourFragment.newInstance();
                break;
            case 3:
                mFragments[index] = UserFragment.newInstance();
                break;
        }
    }

    @Override
    public int[] getMenuIds() {
        return new int[]{
                R.id.tv_home,
                R.id.tv_market,
                R.id.tv_neighbour,
                R.id.tv_user
        };
    }

    @Override
    public boolean menuClicked(int index) {
        return false;
    }

    @Override
    public void onBackPressed() {
        /*if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }*/
        /*if (mCurrent == 1 && MarketFragment.newInstance().canGoback()){
            MarketFragment.newInstance().goBack();
        }*/
        super.onBackPressed();
    }


    @OnClick(R.id.iv_open)
    public void onViewClicked() {
        // OpendoorActivity.toActivity(mActivity);
        if (LoginUserBean.getInstance().isIs_auth()) {
            OpendoorActivity.toActivity(mActivity);
        } else {
            IdentityDialog identityDialog = new IdentityDialog(mActivity, MAIN_ACTIVITY_IDENTITY);
            identityDialog.show();
        }


    }


    private void intall() {
        File apkFile = new File(Environment
                .getExternalStorageDirectory(), DOWNLOAD_NAME);
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= 26) {
                boolean b = getPackageManager().canRequestPackageInstalls();
                if (b) {
                    Uri apkUri = FileProvider.getUriForFile(this, "com.leFu.fileProvider", apkFile);//在AndroidManifest中的android:authorities值
                    Log.e("uri...", apkFile.getPath() + "");
                    Log.e("uri...", apkUri + "");
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    startActivity(install);
                } else {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 111);
                }
            } else {
                Uri apkUri = FileProvider.getUriForFile(this, "com.leFu.fileProvider", apkFile);//在AndroidManifest中的android:authorities值
                Log.e("uri...", apkFile.getPath() + "");
                Log.e("uri...", apkUri + "");
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                startActivity(install);
            }

        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param object     要接受结果的Activity、Fragment。
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        if (requestCode == 111) {
            File apkFile = new File(Environment
                    .getExternalStorageDirectory(), DOWNLOAD_NAME);
            Uri apkUri = FileProvider.getUriForFile(this, "com.leFu.fileProvider", apkFile);//在AndroidManifest中的android:authorities值
            Log.e("uri...", apkFile.getPath() + "");
            Log.e("uri...", apkUri + "");
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        }
    }

    /**
     * 下载应用
     *
     * @author Administrator
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            DOWNLOAD_NAME);
                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            pBar.dismiss();


            intall();

        }
    }

}

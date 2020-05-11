package com.yz.baiduai;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yz.baiduai.manage.MediaLoader;
import com.yz.utils.ToastHelper;
import com.yz.utils.app.Density;

import java.util.Locale;

public class SampleApplicationLike extends DefaultApplicationLike {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static final String TAG = "Tinker.SampleApplicationLike";

    public SampleApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true

       /* if (0 != (getApplication().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
            Log.e("DEBUG", "程序被修改为可调试状态！！！");
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
*/
        mContext = getApplication();
        //Bugly初始化
        Bugly.init(getApplication(), "5aad3b8e30", true);
        Beta.autoCheckUpgrade = true;
        Beta.canAutoDownloadPatch = true;
        Beta.canAutoPatch = true;
        Beta.canNotifyUserRestart = true;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.enableNotification = true;
        Beta.smallIconId = R.drawable.ic_launcher_background;
      /*  Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Logger.i("补丁下载地址" + patchFile);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Logger.i(
                        String.format(Locale.getDefault(), "%s %d%%",
                                Beta.strNotificationDownloading,
                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Logger.i("补丁下载成功");
            }

            @Override
            public void onDownloadFailure(String msg) {
                Logger.i("补丁下载失败");

            }

            @Override
            public void onApplySuccess(String msg) {
                Logger.i("补丁应用成功");
            }

            @Override
            public void onApplyFailure(String msg) {
                Logger.i("补丁应用失败");
            }

            @Override
            public void onPatchRollback() {
                Logger.i("补丁回滚");
            }
        };*/
        //捕获未捕获的异常
//        MyUncaughtException.getInstance().init(this);

        //初始化Logger日志
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        ToastHelper.initToast(getApplication());

        Density.setDensity(getApplication(), 375);

        Album.initialize(AlbumConfig.newBuilder(getApplication())
                .setAlbumLoader(new MediaLoader())
                .build());
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}

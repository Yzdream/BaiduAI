package com.yz.baiduai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yz.baiduai.manage.MediaLoader;
import com.yz.baiduai.common.CustomApplication;
import com.yz.utils.app.Density;
import com.yz.utils.exception.MyUncaughtException;
import com.yz.utils.ToastHelper;

public class MyApplication extends CustomApplication {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
            Log.e("DEBUG", "程序被修改为可调试状态！！！");
//            android.os.Process.killProcess(android.os.Process.myPid());
        }

        //捕获未捕获的异常
        MyUncaughtException.getInstance().init(this);

        //初始化Logger日志
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        ToastHelper.initToast(mContext);

        Density.setDensity(this, 375);

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .build());

    }

    public static Context getContext() {
        return mContext;
    }

}

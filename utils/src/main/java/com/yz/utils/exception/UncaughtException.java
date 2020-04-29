package com.yz.utils.exception;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;


import com.yz.utils.app.AppUtils;
import com.yz.utils.BuildConfig;
import com.yz.utils.FileUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UncaughtException {

    @SuppressLint("StaticFieldLeak")
    private static UncaughtException instance;

    private static Context mContext;

    /**
     * 获取单例
     *
     * @return 异常处理类
     */
    public static UncaughtException getInstance(Context context) {
        if (instance == null) {
            synchronized (UncaughtException.class) {
                if (instance == null) {
                    instance = new UncaughtException();
                    mContext = context;
                }
            }
        }
        return instance;
    }

    public void exceptionThrowable(Throwable throwable){
        try {
            StringWriter sw = new StringWriter();
            PrintWriter err = new PrintWriter(sw);

            Field[] fields = Build.class.getFields();
            for (Field f : fields) {
                sw.write(f.getName() + ":" + f.get(null) + "\n");// 静态属性
            }

            throwable.printStackTrace(err);
            String errorLog = sw.toString();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy-MM-dd");
            String datePath = pathFormat.format(new Date());

            //本地error日志路径  getExternalCacheDir 获取的是Android/data/包名/Cache/  目录
            String filePath = mContext.getExternalCacheDir() + "/Error/" + datePath + "/";
            String fileName = "log.txt";
            //创建日志文件
            FileUtils.writeTxtToFile(errorLog, filePath, fileName);
            sw.close();
            err.close();

            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
           } else {
                //友盟上传错误
//                MobclickAgent.reportError(mContext, throwable);
                // 退出进程
                PackageManager pm = mContext.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(AppUtils.getPackageName(mContext));
                mContext.startActivity(intent);
                // 自杀 重生
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

}

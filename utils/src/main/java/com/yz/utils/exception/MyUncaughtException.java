package com.yz.utils.exception;

import android.annotation.SuppressLint;
import android.content.Context;

public class MyUncaughtException implements Thread.UncaughtExceptionHandler {

    @SuppressLint("StaticFieldLeak")
    private static MyUncaughtException instance;

    private Context mContext;

    /**
     * 获取单例
     *
     * @return 捕获异常类
     */
    public static MyUncaughtException getInstance() {
        if (instance == null) {
            synchronized (MyUncaughtException.class) {
                if (instance == null) {
                    instance = new MyUncaughtException();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     */
    public void init(Context context) {
        this.mContext = context;
        Thread.currentThread().setUncaughtExceptionHandler(this);
    }

    /**
     * 捕获的异常
     *
     * @param thread    线程
     * @param throwable 异常
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        UncaughtException.getInstance(mContext).exceptionThrowable(throwable);
    }
}

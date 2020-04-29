package com.yz.baiduai.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.List;

public class CustomApplication extends BaseApplication {

    private static final int MAX_SLEEP_TIME =  10 * 60 * 1000; //应用退到后台后最大的休眠唤起时间
    // 正常状态
    public static final int STATE_NORMAL = 0;
    // 从后台回到前台
    public static final int STATE_BACK_TO_FRONT = 1;
    // 从前台进入后台
    public static final int STATE_FRONT_TO_BACK = 2;

    // APP状态
    private static int sAppState = STATE_NORMAL;
    // 标记程序是否已进入后台(依据onStop回调)
    private boolean flag;
    // 标记程序是否已进入后台(依据onTrimMemory回调)
    private boolean background;
    // 从前台进入后台的时间
    private static long frontToBackTime;
    // 从后台返回前台的时间
    private static long backToFrontTime;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (background || flag) {
                    background = false;
                    flag = false;
                    sAppState = STATE_BACK_TO_FRONT;
                    backToFrontTime = System.currentTimeMillis();
                    if (canShowAd()) {
                       //从后台到前台时可进行操作 如：重新打开欢迎界面
                    }
                } else {
                    sAppState = STATE_NORMAL;
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                //判断当前activity是否处于前台
                if (!isCurAppTop(activity)) {
                    // 从前台进入后台
                    sAppState = STATE_FRONT_TO_BACK;
                    frontToBackTime = System.currentTimeMillis();
                    flag = true;
                } else {
                    // 否则是正常状态
                    sAppState = STATE_NORMAL;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // TRIM_MEMORY_UI_HIDDEN是UI不可见的回调, 通常程序进入后台后都会触发此回调,大部分手机多是回调这个参数
        // TRIM_MEMORY_BACKGROUND也是程序进入后台的回调, 不同厂商不太一样, 魅族手机就是回调这个参数
        if (level == Application.TRIM_MEMORY_UI_HIDDEN || level == TRIM_MEMORY_BACKGROUND) {
            background = true;
        } else if (level == Application.TRIM_MEMORY_COMPLETE) {
            background = !isCurAppTop(this);
        }
        if (background) {
            frontToBackTime = System.currentTimeMillis();
            sAppState = STATE_FRONT_TO_BACK;
        } else {
            sAppState = STATE_NORMAL;
        }

    }

    /**
     * 进入后台间隔10分钟以后可以再次打开
     *
     * @return 是否能显示广告
     */
    public static boolean canShowAd() {
        return sAppState == STATE_BACK_TO_FRONT && (backToFrontTime - frontToBackTime) > MAX_SLEEP_TIME;
    }

    /**
     * 判断当前程序是否前台进程
     *
     * @param context
     * @return
     */
    private boolean isCurAppTop(Context context) {
        if (context == null) {
            return false;
        }try {
            String curPackageName = context.getPackageName();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            if (list != null && list.size() > 0) {
                ActivityManager.RunningTaskInfo info = list.get(0);
                String topPackageName = info.topActivity.getPackageName();
                String basePackageName = info.baseActivity.getPackageName();
                if (topPackageName.equals(curPackageName) && basePackageName.equals(curPackageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}


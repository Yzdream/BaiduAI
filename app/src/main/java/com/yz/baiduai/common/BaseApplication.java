package com.yz.baiduai.common;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {

    private static List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        //记录每个创建的activity
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });
    }

    // 退出所有
    public void finishAll() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }


    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activities) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }
        finishSingleActivity(tempActivity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activities.contains(activity)) {
                activities.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }


}

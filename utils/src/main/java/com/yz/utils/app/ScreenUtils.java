package com.yz.utils.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ScreenUtils {

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return 宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);//将当前窗口的信息放在DisplayMetrics类中
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取分辨率
     *
     * @param context 上下文
     * @return 分辨率
     */
    public static int getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return width * height;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            @SuppressLint("PrivateApi") Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity 窗体
     * @return Bitmap
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity 窗体
     * @return Bitmap
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕亮度的模式
     * 2. SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * 3. SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public static int getScreenMode(Activity activity) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenMode;
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public static void setScreenMode(Activity activity, int paramInt) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获得当前屏幕亮度值  0--255
     */
    public static int getScreenBrightness(Activity activity) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 设置屏幕亮度
     *
     * @param activity   窗体对象
     * @param brightness 亮度值 0-255之间
     */
    public static void setWindowBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = (brightness <= 0 ? 1 : (brightness > 255 ? 255 : brightness)) / 255f;
        window.setAttributes(lp);
    }

    /**
     * 改变亮度设置窗体外阴影 可用于popwindow弹窗
     *
     * @param activity 窗体
     * @param isShadow 是否阴影效果
     */
    public static void windowIsShadow(Activity activity, boolean isShadow) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = isShadow ? 0.7f : 1.0f;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static int getViewHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

}

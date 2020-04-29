package com.yz.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BarUtils {

    public BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me..."); //不支持操作的异常
    }

    public static class StatusBarView extends View {
        public StatusBarView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public StatusBarView(Context context) {
            super(context);
        }
    }

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    /**
     * 设置“沉浸式”包含状态栏和导航栏 沉浸
     * <P>sdk>=19才有“沉浸式”功能，在这为了兼容性更简单明了，建议使用该方法（>=21）
     *
     * @param activity
     * @param statusBar        设置状态栏 沉浸
     * @param navigationBar    设置导航栏+ 状态栏 沉浸
     * @param isLightStatusBar 是否设置状态栏图标文字为黑色
     */
    private static void setTranslucentBar(Activity activity, Boolean statusBar, Boolean navigationBar, boolean isLightStatusBar) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (!statusBar && !navigationBar) return;
            View decorView = activity.getWindow().getDecorView();
            int option = 0;
            if (statusBar) //设置状态栏 沉浸
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (navigationBar) { //设置导航栏+状态栏 沉浸
                option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
            //设置状态栏图片文字为黑色
            if (isLightStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(option);
            //设置状态栏图片文字为黑色  小米、魅族  方法
            if (isLightStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                }
            }

//            if (isLightStatusBar)
//                StatusBarLightMode(activity,statusBar,navigationBar);
        }
    }

    public static void setStatusBar(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Android 6.0以上
                if (dark) {
                    //http://www.miui.com/thread-8946673-1-1.html 官方说加上额外两个属性 ，测试不需要，否则状态栏高度变化
                    Window window = activity.getWindow();
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*&& Build.VERSION.SDK_INT <= Build.VERSION_CODES.M*/) {
                //5.0 5.1 6.0改变状态栏颜色
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().setStatusBarColor(Color.parseColor("#33464646"));
            }

        }

        if (dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), dark)) {
                //小米
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                //Flyme
            }
        }
    }


    private int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    /**
     * 设置状态栏沉浸
     */
    public static void setTranslucentStatusBar(Activity activity) {
        setTranslucentBar(activity, true, false, false);
    }

    public static void setTranslucentStatusBar(Activity activity, boolean isLightStatusBar) {
        setTranslucentBar(activity, true, false, isLightStatusBar);
    }

    public static void setTranslucentStatusBar(Activity activity, boolean navigationBar, boolean isLightStatusBar) {
        setTranslucentBar(activity, true, navigationBar, isLightStatusBar);
    }

    public static void setTranslucentStatusBar(Activity activity, boolean statusBar, boolean navigationBar, boolean isLightStatusBar) {
        setTranslucentBar(activity, statusBar, navigationBar, isLightStatusBar);
    }

    /**
     * 设置状态栏+ 导航栏沉浸
     */
    public static void setTranslucentStatusBar2NavigationBar(Activity activity) {
        setTranslucentBar(activity, true, true, false);
    }


    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setStatusBarColor(Activity activity, int color) {
        setStatusBarColor(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色，其颜色透明度
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */

    public static void setStatusBarColor(Activity activity, int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        }
    }

    /**
     * 通过窗口对象获取状态栏高度（有局限性）
     *
     * @param activity
     * @return
     */
    @Deprecated
    public static int getStatusBarHeight2(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 通过资源ID获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 通过反射获取状态栏高度
     *
     * @param activity
     * @return
     */
    @SuppressLint("PrivateApi")
    public static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = getStatusBarHeight2(activity);
        if (0 == statusBarHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int id = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusBarHeight = activity.getResources().getDimensionPixelSize(id);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }


    /**
     * 设置状态栏纯色,不加半透明效果
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColorNoTranslucent(Activity activity, int color) {
        setStatusBarColor(activity, color, 0);
    }


    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 兼容到4.1.x
     * 设置状态栏为透明
     *
     * @param activity 窗体
     */
    public static void setStatusBarTranslucent(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (null != contentView) {
            View childView = contentView.getChildAt(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 设置透明状态栏,这样才能让 ContentView 向上
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // 设置状态栏颜色
                window.setStatusBarColor(Color.TRANSPARENT);
                if (childView != null) {
                    // 注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View .
                    // 使其不为系统 View 预留空间.
                    ViewCompat.setFitsSystemWindows(childView, false);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (childView != null) {
                    ViewCompat.setFitsSystemWindows(childView, false);
                }

                int statusBarHeight = contentView.getTop();
                // 需要设置这个 flag 才能设置状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 避免多次调用该方法时,多次移除了 View
                if (childView != null && childView.getLayoutParams() != null
                        && childView.getLayoutParams().height == statusBarHeight) {
                    // 移除假的 View.
                    contentView.removeView(childView);
                    childView = contentView.getChildAt(0);
                }
                if (childView != null) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) childView.getLayoutParams();
                    // 清除 ChildView 的 marginTop 属性
                    if (lp != null && lp.topMargin >= statusBarHeight) {
                        lp.topMargin -= statusBarHeight;
                        childView.setLayoutParams(lp);
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }


    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private static StatusBarView createStatusBarView(Activity activity, int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


    /**
     * 隐藏状态栏
     * <p>也就是设置全屏，一定要在setContentView之前调用，否则报错</p>
     * <p>此方法Activity可以继承AppCompatActivity</p>
     * <p>启动的时候状态栏会显示一下再隐藏，比如QQ的欢迎界面</p>
     * <p>在配置文件中Activity加属性android:theme="@android:style/Theme.NoTitleBar.Fullscreen"</p>
     * <p>如加了以上配置Activity不能继承AppCompatActivity，会报错</p>
     *
     * @param activity activity
     */
    public static void hideStatusBar(Activity activity) {
        //activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 判断状态栏是否存在
     *
     * @param activity activity
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isStatusBarExists(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        return (params.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 获取是否存在NavigationBar（虚拟按键）
     *
     * @param context
     * @return
     */
    public static boolean isHaveNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return int
     */
    public static int getNavigationBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 获取ActionBar高度
     *
     * @param activity activity
     * @return ActionBar高度
     */
    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    /**
     * 显示通知栏
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>}</p>
     *
     * @param context        上下文
     * @param isSettingPanel {@code true}: 打开设置<br>{@code false}: 打开通知
     */
    public static void showNotificationBar(Context context, boolean isSettingPanel) {
        @SuppressLint("ObsoleteSdkInt")
        String methodName = (Build.VERSION.SDK_INT <= 16) ? "expand"
                : (isSettingPanel ? "expandSettingsPanel" : "expandNotificationsPanel");
        invokePanels(context, methodName);
    }

    /**
     * 隐藏通知栏
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>}</p>
     *
     * @param context 上下文
     */
    public static void hideNotificationBar(Context context) {
        String methodName = (Build.VERSION.SDK_INT <= 16) ? "collapse" : "collapsePanels";
        invokePanels(context, methodName);
    }

    /**
     * 反射唤醒通知栏
     *
     * @param context    上下文
     * @param methodName 方法名
     */
    private static void invokePanels(Context context, String methodName) {
        try {
            @SuppressLint("WrongConstant")
            Object service = context.getSystemService("statusbar");
            @SuppressLint("PrivateApi")
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName);
            expand.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity, boolean statusbar, boolean navigationBar) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            result = 3;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                }
            }
        }
        return result;
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void StatusBarDarkMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (MIUISetStatusBarLightMode(activity.getWindow(), false)) {
                } else if (FlymeSetStatusBarLightMode(activity.getWindow(), false)) {
                }
            }
        }
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
                e.printStackTrace();
            }
        }
        return result;
    }

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
                @SuppressLint("PrivateApi")
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
                e.printStackTrace();
            }
        }
        return result;
    }
}

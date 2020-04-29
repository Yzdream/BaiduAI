package com.yz.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private static Toast mToast;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static void initToast(Context context) {
        mContext = context;
    }

    @SuppressLint("ShowToast")
    public static void showShortToast(int resId) {
        if (mContext == null) return;
        String text = mContext.getString(resId);
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void showLongToast(int resId) {
        if (mContext == null) return;
        String text = mContext.getString(resId);
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void showShortToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void showLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}



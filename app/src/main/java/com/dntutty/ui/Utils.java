package com.dntutty.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;



public class Utils {
    private static Utils utils;

    private static final float STANDARD_WIDTH = 1080;
    private static final float STANDARD_HEIGHT = 1920;

    //这里是屏幕显示宽高,即设备屏幕的宽高
    private int mDispalyWidth;
    private int mDispalyHeight;


    public static Utils getInstance(Context context) {
        if (utils == null) {
            utils = new Utils(context.getApplicationContext());//避免内存泄漏
        }
        return utils;
    }

    private Utils(Context context) {
        //获取屏幕的宽高
        if (mDispalyWidth == 0 || mDispalyHeight == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                //判断是否横屏,该width像素值为当前屏幕方向的水平像素值,height为垂直方向,当width>height,则可判断横屏
                if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                    mDispalyWidth = displayMetrics.heightPixels;
                    mDispalyHeight = displayMetrics.widthPixels;
                } else {
                    mDispalyWidth = displayMetrics.widthPixels;
                    mDispalyHeight = displayMetrics.heightPixels - getStatusBarHeight(context);
                }
            }
        }
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int resID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0) {
            return context.getResources().getDimensionPixelSize(resID);
        }
        return 0;
    }

    //获取水平方向的缩放比例
    public float getHorizontalScale() {
        return mDispalyWidth / STANDARD_WIDTH;
    }

    //获取垂直方向的缩放比例
    public float getVerticalScale() {
        return mDispalyHeight / STANDARD_HEIGHT;
    }
}

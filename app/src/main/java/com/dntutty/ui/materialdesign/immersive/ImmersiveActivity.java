package com.dntutty.ui.materialdesign.immersive;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.dntutty.ui.BuildConfig;
import com.dntutty.ui.R;

import java.lang.reflect.Field;

public class ImmersiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersive);
        immersive();
    }

    private void immersive() {
        //Android 系统小于4.4是不支持沉浸式的
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上可以通过重绘状态栏实现沉浸式
            //清除状态栏半透明和导航栏半透明标识
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int visibilty = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE             //与SYSTEM_UI_FLAG_HIDE_NAVIGATION同时使用,可使底部导航栏不会触摸显示
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;      //该FLAG用于隐藏底部导航栏,单独设置时,如果由用户交互,会强制清除该标志,显示底部导航栏
            int oldVisibilty = getWindow().getDecorView().getSystemUiVisibility();
            getWindow().getDecorView().setSystemUiVisibility(visibilty | oldVisibilty);

        } else {
            //5.0以下直接通过窗口添加标识,透明状态栏即可实现沉浸式状态栏,但是该模式在5.0以下及以上是半透明,带有阴影
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public int getSystemBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int height = context.getResources().getDimensionPixelSize(resId);
        if (height != -1) {
            return height;
        } else {
        return getValue(context, "com.android.internal.R$dimen", "status_bar_height", 32);
        }
    }

    private int getValue(Context context, String className, String systemBarHeight, int defalutHeight) {
        try {
            Class<?> clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            Field field = clazz.getField(systemBarHeight);
            int id = Integer.parseInt(field.get(obj).toString());

            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defalutHeight;
    }
}

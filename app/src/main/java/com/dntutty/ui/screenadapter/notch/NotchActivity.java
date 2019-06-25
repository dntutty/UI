package com.dntutty.ui.screenadapter.notch;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.dntutty.ui.R;

public class NotchActivity extends AppCompatActivity {

    //华为,小米,oppo
    //1,判断手机厂商
    // 2.判断手机是否有刘海,
    // 3.设置是否让内容区域延伸进刘海
    // 4.是否需要避让刘海区域,通过添加子view的margin或这父布局添加padding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Density.setDensity(getApplication(),this);

        //1.设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //判断手机是否是刘海屏
        boolean hasDisplayCutout = hasDisplayCutout(window);

        if (hasDisplayCutout) {
            //2.让内容区域延伸进刘海
            WindowManager.LayoutParams params = window.getAttributes();
            /**
             *@see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT  全屏模式,内容下移,非全屏模式不受影响
             *@see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 内容区域延伸到刘海区域,
             *@see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER   不允许内容延伸到刘海区域
             */
//        params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            window.setAttributes(params);

            //3.设置沉浸式
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(visibility | flags);
        }
        setContentView(R.layout.activity_notch);
    }

    private boolean hasDisplayCutout(Window window) {
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowInsets windowInsets = rootView.getRootWindowInsets();
            if (windowInsets != null) {
                displayCutout = windowInsets.getDisplayCutout();
                if (displayCutout != null) {
                    if (displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0 && displayCutout.getSafeInsetTop() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

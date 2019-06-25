package com.dntutty.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.dntutty.ui.screenadapter.density.Density;

public class UiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //屏幕密度适配
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                Density.setDensity(UiApplication.this,activity);
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

            }
        });
    }


}

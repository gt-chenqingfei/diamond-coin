package com.lovecoin.ediamond.api.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by k on 2017/3/3.
 */

public class BackgroundUtils {

    private static Callback callback;

    public static void init(Application application) {
        application.unregisterActivityLifecycleCallbacks(getCallback());
        application.registerActivityLifecycleCallbacks(getCallback());
    }

    public static boolean isForeground() {
        return getCallback().appCount > 0;
    }

    private static Callback getCallback() {
        if (callback == null) {
            callback = new Callback();
        }
        return callback;
    }

    private static class Callback implements Application.ActivityLifecycleCallbacks {

        public int appCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            appCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            appCount = appCount > 0 ? appCount - 1 : 0;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}


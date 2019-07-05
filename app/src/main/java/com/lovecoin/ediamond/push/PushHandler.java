package com.lovecoin.ediamond.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lovecoin.ediamond.app.App;
import com.lovecoin.ediamond.data.UserHelper;
import com.lovecoin.ediamond.ui.main.MainActivityStarter;

/**
 * Created on 2017/11/29.
 */

public class PushHandler {

    public static String PUSH_KEY = "type";
    public static String PUSH_VALUE_ERROR = "-1";

    public static String getPushValue(@NonNull Bundle bundle) {
        return bundle.getString(PUSH_KEY, PUSH_VALUE_ERROR);
    }

    public static boolean hasPush(@Nullable Bundle bundle) {
        if (bundle == null
                || bundle.isEmpty()
                || !bundle.containsKey(PUSH_KEY)) {
            return false;
        }
        String action = getPushValue(bundle);
        return !PUSH_VALUE_ERROR.equals(action);
    }

    public static boolean handlePush(Context context, Bundle bundle) {
        if (hasPush(bundle) && App.getInstance().isMainStart() && UserHelper.getInstance().isLogin()) {
            routePush(context, getPushValue(bundle));
            return true;
        }

        return false;
    }

    public static void routePush(Context context, String aciton) {
        //Logger.d("route push %s", aciton);
        if (App.getInstance().isMainStart() && UserHelper.getInstance().isLogin()) {
            MainActivityStarter.startWithFlags(App.getInstance(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
    }
}

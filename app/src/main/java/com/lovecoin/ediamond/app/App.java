package com.lovecoin.ediamond.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lovecoin.ediamond.BuildConfig;
import com.lovecoin.ediamond.MyEventBusIndex;
import com.lovecoin.ediamond.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class App extends Application {

    private static App instance;
    private boolean mainStart = false;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.app_f7f9fe, R.color.app_3e4f6c, R.color.app_c4cfe1);//全局设置主题颜色
                return new ClassicsHeader(context)
                        .setTimeFormat(new SimpleDateFormat("'Last Update' M-d HH:mm", Locale.getDefault()))
                        .setTextSizeTitle(11)
                        .setTextSizeTime(8)
                        .setEnableLastTime(false);
            }
        });

        ClassicsHeader.REFRESH_HEADER_PULLDOWN = "Pull down to refresh";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "Refreshing...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "Loading...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "Release to refresh";
        ClassicsHeader.REFRESH_HEADER_FINISH = "Refresh complete";
        ClassicsHeader.REFRESH_HEADER_FAILED = "Refresh failed";

    }

    // palpay token 应用内一次性数据
    private String palpalClientToken;

    public String getPalpalClientToken() {
        return palpalClientToken;
    }

    public void setPalpalClientToken(String palpalClientToken) {
        this.palpalClientToken = palpalClientToken;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Utils.init(this);
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
//        CrashReport.initCrashReport(getApplicationContext(), "09319afe48", BuildConfig.DEBUG);
        ToastUtils.setBgColor(0xb2000000);
        ToastUtils.setMsgColor(0xffffffff);
    }

    public boolean isMainStart() {
        return mainStart;
    }

    public void setMainStart(boolean mainStart) {
        this.mainStart = mainStart;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

}

package com.lovecoin.ediamond.ui.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.allenliu.versionchecklib.callback.APKDownloadListener;
import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.lovecoin.ediamond.BuildConfig;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.resp.BaseInfo;
import com.lovecoin.ediamond.api.entity.resp.CheckVersionResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.data.UserHelper;
import com.lovecoin.ediamond.push.PushHandler;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.login.LoginActivity;
import com.lovecoin.ediamond.ui.main.MainActivityStarter;
import com.lovecoin.ediamond.utils.VersionCompare;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.File;
import java.util.UUID;

import activitystarter.MakeActivityStarter;

/**
 * 闪屏页面
 */
@MakeActivityStarter
public class SplashActivity extends BaseActivity {
    public static final String PREF_VERSION = "VERSION";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        if (PushHandler.handlePush(this, getIntent().getExtras())) {
            //Logger.e("splash handle push");
            this.finish();
        }
    }

    @Override
    protected void initView() {
        cleanCacheIfNeed();
        new Handler().postDelayed(() -> {
            if (UserHelper.getInstance().isLogin()) {
                //Logger.i("has login");
                checkVersion();
            } else {
                //Logger.i("not login");
                UserHelper.getInstance().clear();
                getBaseInfo();
            }
        }, BuildConfig.DEBUG ? 1000 : 3000);
    }

    private void startMainActivity() {
        Intent intent = MainActivityStarter.getIntent(this);
        Bundle extras = getIntent().getExtras();
        if (PushHandler.hasPush(extras)) {
            intent.putExtra(PushHandler.PUSH_KEY, PushHandler.getPushValue(extras));
        }
        startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    private void getBaseInfo() {
        Api.getInstance()
                .getBaseInfo(UUID.randomUUID().toString())
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<BaseInfo>() {
                    @Override
                    protected void onHandleSuccess(BaseInfo info) {
                        //Logger.i("getbaseinfo token & secretKey");
                        SPUtils.getInstance().put(PREF_VERSION, AppUtils.getAppVersionCode());
                        UserHelper.getInstance().setToken(info.getToken());
                        UserHelper.getInstance().setSecretKey(info.getSecretKey());
                        checkVersion();
                    }
                });
    }

    @Override
    public boolean setNavColor() {
        return false;
    }

    private void goMainOrLogin() {
        if (UserHelper.getInstance().isLogin()) {
            goMain();
        } else {
            goLogin();
        }
    }

    private void goLogin() {
        LoginActivity.start(SplashActivity.this);
        SplashActivity.this.finish();
    }

    private void goMain() {
        startMainActivity();
        this.finish();
    }

    private void checkVersion() {
        Api.getInstance().checkVersion()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<CheckVersionResp>() {
                    @Override
                    protected void onHandleSuccess(CheckVersionResp resp) {
                        boolean needUpdate = VersionCompare.needUpdate(resp.getAndroidVersionNo(), AppUtils.getAppVersionName());
                        if (!needUpdate) {
                            goMainOrLogin();
                            return;
                        }

                        download(resp);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    protected void onHandleError(String code, String msg) {

                    }
                });
    }


    private void download(CheckVersionResp resp) {
        DownloadBuilder builder = AllenVersionChecker.getInstance()
                .downloadOnly(crateUIData(resp.getAndroidDownloadUrl(), resp.getAndroidDownloadContent()));
        builder.setForceRedownload(true);
        if (resp.getMust()) {
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                }
            });

        }
        builder.excuteMission(SplashActivity.this);
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData(String url, String content) {
        UIData uiData = UIData.create();
        uiData.setTitle("New Version");
        uiData.setDownloadUrl(url);
        uiData.setContent(content);
        return uiData;
    }

    /**
     * 针对v1.0.0版本
     */
    private void cleanCacheIfNeed() {
        int version = SPUtils.getInstance().getInt(PREF_VERSION, 0);
        if (version <= 0) {
            UserHelper.getInstance().clear();
        }
    }

}

package com.lovecoin.ediamond.api;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.entity.BaseResp;
import com.lovecoin.ediamond.data.UserHelper;
import com.lovecoin.ediamond.ui.entry.SplashActivityStarter;
import com.lovecoin.ediamond.ui.entry.login.LoginActivityStarter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<BaseResp<T>> {

    private static final String TAG = "BaseObserver";

    private static List<String> errorCodeNeedRestart = new ArrayList<>();

    {
        errorCodeNeedRestart.add("00002"); // 00002：对不起，请登录
        errorCodeNeedRestart.add("00005"); // "code":"00005","msg":"未能识别的TOKEN"
        errorCodeNeedRestart.add("00006"); // "code":"00006","msg":"未能识别的密匙"
    }

    protected BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResp<T> value) {
        if (value.isSuccess()) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getCode(), value.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        ToastUtils.showShort(R.string.tips_net_error);
        Logger.t(TAG).e("error:" + e.toString());
        onAfter();
    }

    @Override
    public void onComplete() {
        onAfter();
    }

    protected void onAfter() {
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String code, String msg) {

        if (errorCodeNeedRestart.contains(code)) {
            // TODO: 2018/5/10  添加提示语
            UserHelper.getInstance().clear();
            SplashActivityStarter.startWithFlags(Utils.getApp(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            switch (code) {
                case "00002":
                    ToastUtils.showLong(R.string.str_account_login_into_other_device);
                    break;

                case "00005":
                case "00006":
                    ToastUtils.showLong(R.string.str_login_expired);
                    break;
            }

        } else {
            ToastUtils.showShort(msg);
        }
    }
}

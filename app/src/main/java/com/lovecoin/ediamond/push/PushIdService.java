package com.lovecoin.ediamond.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.RegisterPushParams;
import com.lovecoin.ediamond.data.UserHelper;

import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/10/31 0031.
 */

public class PushIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        //Logger.d("token %s", token);
        sendRegistrationToServer(token);
    }

    public static void sendRegistrationToServer(String token) {
        if (!UserHelper.getInstance().isLogin()) {
            return;
        }
        Api.getInstance()
           .registerPush(new RegisterPushParams(token))
           .subscribeOn(Schedulers.io())
           .observeOn(Schedulers.io())
           .subscribe(new BaseObserver<Object>() {
               @Override
               protected void onHandleSuccess(Object o) {
                   //Logger.d("send token success %s", o);
               }

               @Override
               public void onError(Throwable e) {

               }

               @Override
               protected void onHandleError(String code, String msg) {

               }
           });

    }
}

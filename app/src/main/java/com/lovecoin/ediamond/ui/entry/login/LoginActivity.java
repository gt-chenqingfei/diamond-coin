package com.lovecoin.ediamond.ui.entry.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.LoginByFbParams;
import com.lovecoin.ediamond.api.entity.resp.LoginResponse;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.data.UserHelper;
import com.lovecoin.ediamond.event.StartMainEvent;
import com.lovecoin.ediamond.ui.dialog.Loading;
import com.lovecoin.ediamond.ui.entry.bind.BindMobileActivityStarter;
import com.lovecoin.ediamond.ui.entry.information.SettingProfileActivityStarter;
import com.lovecoin.ediamond.ui.entry.reg.RegActivityStarter;
import com.lovecoin.ediamond.ui.main.MainActivityStarter;
import com.lovecoin.extra.facebook.UserProfileTracker;
import com.lovecoin.extra.facebook.callback.GetUserCallback;
import com.lovecoin.extra.facebook.entity.User;
import com.lovecoin.extra.facebook.entity.UserFriendsResp;
import com.lovecoin.extra.facebook.request.UserRequest;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Collection;

import activitystarter.MakeActivityStarter;
import butterknife.ButterKnife;
import butterknife.OnClick;

@MakeActivityStarter
public class LoginActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    private QMUITipDialog loadingDialog;

    private CallbackManager callbackManager;
    private User user;
    private UserFriendsResp userFriendsResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initFacebook();
        EventBus.getDefault().register(this);
    }


    @OnClick({R.id.sign_up_tv, R.id.phone_layout, R.id.facebook_layout})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.phone_layout:
                LoginByPhoneActivityStarter.start(this);
                break;
            case R.id.facebook_layout:
                LoginManager.getInstance().logOut();
                Collection<String> permissions = Arrays.asList("public_profile", "email");
                LoginManager.getInstance().logInWithReadPermissions(this, permissions);
//                facebookLogin();
                break;
            case R.id.sign_up_tv:
                RegActivityStarter.start(this);
                break;
        }
    }

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Logger.i("facebook login onSuccess");
                        //Logger.d(loginResult.getAccessToken());
                        checkUserProfile();
                    }

                    @Override
                    public void onCancel() {
                        //Logger.i("facebook login onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //Logger.i("facebook login onError");
                        //Logger.e(exception, exception.toString());
                    }
                });
    }

    private void checkUserProfile() {
        Profile profile = Profile.getCurrentProfile();

        if (profile == null) {
            new UserProfileTracker((oldProfile, currentProfile) -> {
                getFacebookInfo();
            });
        } else {
            getFacebookInfo();
        }
    }

    private void getFacebookInfo() {

//        UserFriendsRequest.makeRequest(new GetUserFriendsCallback(new GetUserFriendsCallback.IGetUserFriendsResponse() {
//            @Override
//            public void onCompleted(UserFriendsResp userFriendsResp) {
//                LoginActivity.this.userFriendsResp = userFriendsResp;
//                //Logger.d("user friends %s", userFriendsResp.summary.total_count);
//                tryLogin();
//            }
//
//            @Override
//            public void onError() {
//                Logger.e("error");
//            }
//        }).getCallback());

        UserRequest.makeUserRequest(new GetUserCallback(new GetUserCallback.IGetUserResponse() {
            @Override
            public void onCompleted(User user) {
                LoginActivity.this.user = user;
                tryLogin();
            }

            @Override
            public void onError() {
                Logger.e("error");
            }
        }).getCallback());
    }

    private void tryLogin() {
        if (user != null) {
//            facebookLogin(user, userFriendsResp);
            showLoading();
            loginByFB(user);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callbackManager != null) {
            LoginManager.getInstance().unregisterCallback(callbackManager);
        }
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goMain() {
        UserHelper.getInstance().setLogin(true);
        MainActivityStarter.start(this);
        this.finish();
    }

    private void loginByFB(User user) {
        LoginByFbParams loginByFbParams = new LoginByFbParams()
                .setFbAccount(user.getId())
                .setFbNickname(user.getName())
                .setAvatar(user.getPicture())
                .setGender(user.getGender());

        Api.getInstance()
                .loginByFb(loginByFbParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<LoginResponse>() {
                    @Override
                    protected void onHandleSuccess(LoginResponse info) {
                        dismissLoading();
                        goMainIfNeed(info, LoginResponse.PLATFORM_FB, user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }
                });
    }

    private void goMainIfNeed(LoginResponse info, String platform, User user) {
        if (info == null) {
            return;
        }

        if (!TextUtils.isEmpty(info.getUserId())) {
            if (info.getProfileFlag() == LoginResponse.FLAG_NO) {
                goMain();
                return;
            }
            goProfileComplete(info);
            return;
        }

        goBindMobile(platform, user.getId());
    }

    private void goProfileComplete(LoginResponse info) {
        SettingProfileActivityStarter.start(this, info.getAvatar(),
                info.getNickname(), info.getGender(),R.string.activity_set_profile_title);
    }

    private void goBindMobile(String platform, String accountId) {
        BindMobileActivityStarter.start(this, platform, accountId);
    }

    public void showLoading() {
        dismissLoading();

        loadingDialog = Loading.showLoading(this);
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartMainEvent(StartMainEvent event) {
        goMain();
    }

}

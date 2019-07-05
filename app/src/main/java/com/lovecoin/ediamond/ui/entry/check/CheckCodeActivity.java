package com.lovecoin.ediamond.ui.entry.check;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.BindMobileParams;
import com.lovecoin.ediamond.api.entity.params.LoginByPhoneParams;
import com.lovecoin.ediamond.api.entity.params.ValidateUserMobileParams;
import com.lovecoin.ediamond.api.entity.resp.LoginResponse;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.BindMobileEvent;
import com.lovecoin.ediamond.event.StartMainEvent;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.information.SettingProfileActivityStarter;
import com.lovecoin.ediamond.utils.CommonCountDownTimer;
import com.lovecoin.ediamond.widget.IdentifyingCodeView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import activitystarter.Arg;
import butterknife.BindView;
import butterknife.OnClick;

public class CheckCodeActivity extends BaseActivity {
    public static final int MODE_REG = 1;
    public static final int MODE_LOGIN = 2;
    public static final int MODE_BIND = 3;

    @Arg(optional = true)
    String platform;
    @Arg(optional = true)
    String accountId;
    @Arg
    String phone;
    @Arg
    String areaCode;
    @Arg(optional = true)
    int mode = MODE_REG;

    @BindView(R.id.reg_phone_tv)
    TextView regPhoneTv;
    @BindView(R.id.action_btn)
    Button actionBtn;
    @BindView(R.id.check_reg_layout)
    TextView checkRegLayout;
    @BindView(R.id.check_count_down_tv)
    TextView checkCountDownTv;
    @BindView(R.id.check_login_layout)
    LinearLayout checkLoginLayout;
    @BindView(R.id.check_code_view)
    IdentifyingCodeView identifyingCodeView;

    private CommonCountDownTimer countDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_code;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        switch (mode) {
            case MODE_REG:
                setToolbarTitle(getString(R.string.str_title_register));
                actionBtn.setText(getString(R.string.str_title_register));
                checkRegLayout.setVisibility(View.VISIBLE);
                break;
            case MODE_LOGIN:
                setToolbarTitle(getString(R.string.str_title_login));
                actionBtn.setText(getString(R.string.str_title_login));
                checkLoginLayout.setVisibility(View.VISIBLE);
                break;
            case MODE_BIND:
                setToolbarTitle(getString(R.string.str_title_bind_phone_number));
                actionBtn.setText(R.string.str_btn_submit);
                checkLoginLayout.setVisibility(View.VISIBLE);
                break;
        }

        identifyingCodeView.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String content = identifyingCodeView.getTextContent();
                actionBtn.setEnabled(content.trim().length() == 4);
            }

            @Override
            public void deleteContent() {
                String content = identifyingCodeView.getTextContent();
                actionBtn.setEnabled(content.trim().length() == 4);
            }
        });

    }

    @Override
    protected void initData() {
        regPhoneTv.setText(String.format("%s%s", areaCode, phone));
        countDownTimer = new CommonCountDownTimer(60 * 1000, new CommonCountDownTimer.CommonCountDownCallback() {
            @Override
            public void onTimerTick(long millisUntilFinished) {
                checkCountDownTv.setText(getString(R.string.repeate_verification_code_after_27_seconds, "" + millisUntilFinished / 1000));
            }

            @Override
            public void onTimerFinish() {
                checkCountDownTv.setVisibility(View.GONE);
            }
        });
        countDownTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.action_btn)
    public void onRegClick(View view) {
        String code = identifyingCodeView.getTextContent();
        if (code.length() < 4) {
            ToastUtils.showShort("Please check code");
            return;
        }
        switch (mode) {
            case MODE_REG:
                validCode(code);
                break;
            case MODE_LOGIN:
                loginByPhone(code);
                break;
            case MODE_BIND:
                bindMobile(code);
                break;
        }
    }

    @OnClick(R.id.change_phone_tv)
    public void onChangePhoneClick(View view) {
        this.finish();
    }


    private void bindMobile(String vCode) {
        showLoading();
        BindMobileParams bindMobileParams = new BindMobileParams()
                .setAccountId(accountId)
                .setMobile(phone)
                .setAreaCode(areaCode)
                .setvCode(vCode)
                .setPlatform(platform);

        Api.getInstance().bindMobile(bindMobileParams).compose(RxSchedulers.compose()).subscribe(new BaseObserver<LoginResponse>() {

            @Override
            protected void onHandleSuccess(LoginResponse loginResponse) {
                dismissLoading();
                goMainIfNeed(loginResponse);
            }

            @Override
            public void onError(Throwable e) {
                dismissLoading();
                super.onError(e);
            }

            @Override
            protected void onHandleError(String code, String msg) {
                super.onHandleError(code, msg);
                dismissLoading();
            }
        });
    }

    private void goMainIfNeed(LoginResponse loginResponse) {
        if (loginResponse.getProfileFlag() == LoginResponse.FLAG_NO) {
            goMain();
            return;
        }
        goProfileComplete(loginResponse);
    }

    private void goProfileComplete(LoginResponse loginResponse) {
        SettingProfileActivityStarter.start(this, loginResponse.getAvatar(),
                loginResponse.getNickname(), loginResponse.getGender(), R.string.activity_set_profile_title);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartMainEvent(StartMainEvent event) {
        this.finish();
    }

    private void goMain() {
        EventBus.getDefault().post(new StartMainEvent());
        CheckCodeActivity.this.finish();
    }

    private void validCode(String code) {
        showLoading();
        ValidateUserMobileParams params = new ValidateUserMobileParams(areaCode, phone, code);
        Api.getInstance()
                .validateUserMobile(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object s) {
                        dismissLoading();
                        ToastUtils.showShort("Bind Success");
                        EventBus.getDefault().postSticky(new BindMobileEvent());
                        CheckCodeActivity.this.finish();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoading();
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

    private void loginByPhone(String vCode) {
        showLoading();
        LoginByPhoneParams params = new LoginByPhoneParams()
                .setAreaCode(areaCode)
                .setMobile(phone)
                .setvCode(vCode);
        Api.getInstance()
                .loginByPhone(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<LoginResponse>() {
                    @Override
                    protected void onHandleSuccess(LoginResponse response) {
                        dismissLoading();
                        goMainIfNeed(response);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoading();
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


}

package com.lovecoin.ediamond.ui.entry.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.GetValidCodeParams;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.BindMobileEvent;
import com.lovecoin.ediamond.event.StartMainEvent;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.bind.BindMobileActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivityStarter;
import com.lovecoin.ediamond.ui.entry.common.CountryCodeHelper;
import com.lovecoin.ediamond.ui.entry.face.FaceIdActivityStarter;
import com.lovecoin.ediamond.utils.PhoneNumberUtils;
import com.lovecoin.ediamond.widget.CenterTabFrameLayout;
import com.lovecoin.extra.area.CountryActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 密码登录和验证码登录共用页面
 * <p>
 * v1.0 已废弃
 */
@MakeActivityStarter
public class LoginByPhoneActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    CenterTabFrameLayout tabLayout;

    @BindView(R.id.country_code_tv)
    TextView countryCodeTv;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.pwd_layout)
    FrameLayout pwdLayout;
    @BindView(R.id.pwd_et)
    EditText pwdEt;
    @BindView(R.id.action_btn)
    Button actionBtn;

    private int tabSelection = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_by_phone;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        changeUi();
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validPhoneNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validPhoneNumber() {
        boolean isValid = false;
        String phone = phoneEt.getText().toString();
        String countryCode = countryCodeTv.getText().toString();
        if (!TextUtils.isEmpty(countryCode)) {
            countryCode = countryCode.replace("+", "");
            isValid = PhoneNumberUtils.validPhoneNumber(phone, Integer.parseInt(countryCode));
        }
        actionBtn.setEnabled(isValid);
    }


    @OnClick(R.id.country_code_tv)
    public void onCountryCodeClick() {
        CountryActivity.startForResult(this);
    }

    @Override
    protected void initData() {
        tabLayout.setOnTabClickListener(new CenterTabFrameLayout.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                tabSelection = position;
                changeUi();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void changeUi() {
        if (isUsedPwdMode()) {
            pwdLayout.setVisibility(View.VISIBLE);
            actionBtn.setText(R.string.str_title_login);
        } else {
            pwdLayout.setVisibility(View.GONE);
            actionBtn.setText(R.string.str_btn_next);
        }
    }

    public boolean isUsedPwdMode() {
        return tabSelection == 0;
    }

    @OnCheckedChanged(R.id.pwd_cb)
    public void onPwdCheckedChanged(boolean checked) {
        if (checked) {
            pwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            pwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        pwdEt.setSelection(pwdEt.getText().length());
    }

    @OnClick(R.id.action_btn)
    public void onNextClick(View view) {
        String countryCode = countryCodeTv.getText().toString();
        String phone = phoneEt.getText().toString();
        String pwd = pwdEt.getText().toString();
        if (isUsedPwdMode()) {

            if (phone.length() != 11 || pwd.length() < 6) {
                ToastUtils.showShort("check phone or pwd");
            } else {
                // todo login
                FaceIdActivityStarter.start(this);
            }
        } else {
            getCode(countryCode, phone);
        }
    }

    private void getCode(String countryCode, String mobile) {
        showLoading();
        GetValidCodeParams getValidCodeParams = new GetValidCodeParams("4", countryCode, mobile);
        Api.getInstance()
                .getValidCode(getValidCodeParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object s) {
                        dismissLoading();
                        CheckCodeActivityStarter.start(LoginByPhoneActivity.this, mobile, countryCode, CheckCodeActivity.MODE_LOGIN);
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String countryNumber = bundle.getString("countryNumber");
            countryCodeTv.setText(countryNumber);
            validPhoneNumber();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartMainEvent(StartMainEvent event) {
        this.finish();
    }
}

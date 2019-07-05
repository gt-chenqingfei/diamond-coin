package com.lovecoin.ediamond.ui.entry.bind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivityStarter;
import com.lovecoin.ediamond.utils.PhoneNumberUtils;
import com.lovecoin.extra.area.CountryActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手机号绑定
 */
@MakeActivityStarter
public class BindMobileActivity extends BaseActivity {

    @Arg(optional = true)
    String platform;
    @Arg(optional = true)
    String accountId;

    @BindView(R.id.country_code_tv)
    TextView countryCodeTv;
    @BindView(R.id.pwd_layout)
    View pwdLayout;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.pwd_et)
    EditText pwdEt;
    @BindView(R.id.action_btn)
    Button actionBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        pwdLayout.setVisibility(View.GONE);
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validPhoneNumber();
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

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.action_btn)
    public void onRegClick(View view) {
        String phone = phoneEt.getText().toString();
        if (phone.length() == 0) {
            ToastUtils.showShort("Please Check Mobile");
        } else {
            getCode(countryCodeTv.getText().toString(), phone);
        }
    }

    @OnClick(R.id.country_code_tv)
    public void onCountryCodeClick() {
        CountryActivity.startForResult(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartMainEvent(StartMainEvent e) {
        this.finish();
    }

    private void getCode(String countryCode, String mobile) {
        showLoading();
        String code = countryCodeTv.getText().toString();
        GetValidCodeParams getValidCodeParams = new GetValidCodeParams("5", code, mobile);
        Api.getInstance()
                .getValidCode(getValidCodeParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object s) {
                        dismissLoading();
                        CheckCodeActivityStarter.start(BindMobileActivity.this,
                                platform, accountId, mobile, countryCode, CheckCodeActivity.MODE_BIND);

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
            //Logger.d(countryName + countryNumber);
            countryCodeTv.setText(countryNumber);
            validPhoneNumber();
        }
    }

}

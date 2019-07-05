package com.lovecoin.ediamond.ui.entry.reg;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivityStarter;
import com.lovecoin.ediamond.ui.entry.common.CountryCodeHelper;
import com.lovecoin.extra.area.CountryActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 手机号注册
 */
@MakeActivityStarter
public class RegActivity extends BaseActivity {

    @BindView(R.id.country_code_tv)
    TextView countryCodeTv;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.pwd_et)
    EditText pwdEt;
    @BindView(R.id.pwd_cb)
    CheckBox pwdCb;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
//        CountryCodeHelper.initCountryCodeSpinner(this, countryCodeSpinner);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.country_code_tv)
    public void onCountryCodeClick() {
        CountryActivity.startForResult(this);
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
    public void onRegClick(View view) {
        String phone = phoneEt.getText().toString();
        String pwd = pwdEt.getText().toString();
        if (phone.length() != 11 || pwd.length() < 6) {
            ToastUtils.showShort("check phone or pwd");
        } else {
            String code = countryCodeTv.getText().toString();
            CheckCodeActivityStarter.start(this, code, phone, pwd, CheckCodeActivity.MODE_REG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String countryName = bundle.getString("countryName");
            String countryNumber = bundle.getString("countryNumber");
            //Logger.d(countryName + countryNumber);
            countryCodeTv.setText(countryNumber);
        }
    }
}

package com.lovecoin.ediamond.ui.entry.bind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivity;
import com.lovecoin.ediamond.ui.entry.check.CheckCodeActivityStarter;
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
public class BindActivity extends BaseActivity {

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
                actionBtn.setEnabled(s.toString().length() != 0);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.action_btn)
    public void onRegClick(View view) {
        String phone = phoneEt.getText().toString();
        if (phone.length() == 0) {
            ToastUtils.showShort("Please Check Mobile");
        } else {
            String code = countryCodeTv.getText().toString().substring(1);
            getCode(code, phone);
        }
    }

    @OnClick(R.id.country_code_tv)
    public void onCountryCodeClick() {
        CountryActivity.startForResult(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindMobileEvent e) {
        this.finish();
    }

    private void getCode(String countryCode, String mobile) {
        GetValidCodeParams getValidCodeParams = new GetValidCodeParams("2", countryCode, mobile);
        Api.getInstance()
                .getValidCode(getValidCodeParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object s) {
                        CheckCodeActivityStarter.start(BindActivity.this,
                                countryCode, mobile, "", CheckCodeActivity.MODE_BIND);
                    }
                });
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

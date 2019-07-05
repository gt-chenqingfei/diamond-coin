package com.lovecoin.ediamond.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.Utils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.app.Const;
import com.lovecoin.ediamond.data.UserHelper;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.SplashActivityStarter;
import com.lovecoin.ediamond.ui.entry.contact.ContactActivityStarter;
import com.lovecoin.ediamond.ui.entry.login.LoginActivityStarter;
import com.lovecoin.ediamond.ui.webview.WebActivityStarter;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 */
@MakeActivityStarter
public class SettingActivity extends BaseActivity {

    @BindView(R.id.layout_faq)
    RelativeLayout layoutFaq;
    @BindView(R.id.layout_about)
    RelativeLayout layoutAbout;
    @BindView(R.id.layout_sign_out)
    RelativeLayout layoutSignOut;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.layout_faq)
    public void faq() {
        WebActivityStarter.start(this, "https://help.ediamond.love/faq.html");
    }

    @OnClick(R.id.layout_terms)
    public void terms() {
        WebActivityStarter.start(this, "https://help.ediamond.love/terms-of-service.html");
    }

    @OnClick(R.id.layout_privacy)
    public void privacy() {
        WebActivityStarter.start(this, "https://help.ediamond.love/privacy-policy.html");
    }

    @OnClick(R.id.layout_contact)
    public void contact() {
        // TODO 邮件发送暂时调用系统的
        // ContactActivityStarter.start(this);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Const.EMAIL_ADDRESS));
        startActivity(Intent.createChooser(intent, "Select email application."));
    }

    @OnClick(R.id.layout_about)
    public void about() {
        AboutActivityStarter.start(this);
    }

    @OnClick(R.id.layout_sign_out)
    public void signOut() {
        Api.getInstance()
                .logout()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    protected void onHandleSuccess(Boolean aBoolean) {
                        UserHelper.getInstance().clear();
                        SplashActivityStarter.startWithFlags(Utils.getApp(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                   LoginActivityStarter.startWithFlags(Utils.getApp(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                });

    }

}

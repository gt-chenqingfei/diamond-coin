package com.lovecoin.ediamond.ui.entry.contact;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;

@MakeActivityStarter
public class ContactActivity extends BaseActivity {

    @BindView(R.id.tv_email_address)
    TextView tv_email_address;
    @BindView(R.id.et_send_content)
    EditText et_send_content;
    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        String mailAddress = "xxx@gmail.com";
        tv_email_address.setText("To : " + mailAddress);
        toolbar.setMenu("Send", 0);
        et_send_content.setHint("Discribe your problem");
        toolbar.setMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable sendContent = et_send_content.getText();
                if (StringUtils.isEmpty(sendContent)) {
                    ToastUtils.showShort("请输入内容");
                    return;
                } else {
                    ToastUtils.showShort("发送内容：" + sendContent.toString());
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}

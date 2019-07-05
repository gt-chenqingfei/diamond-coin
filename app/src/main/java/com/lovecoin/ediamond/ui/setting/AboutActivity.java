package com.lovecoin.ediamond.ui.setting;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.MakeActivityStarter;

/**
 * 设置页面
 */
@MakeActivityStarter
public class AboutActivity extends BaseActivity {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
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


}

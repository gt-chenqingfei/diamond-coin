package com.lovecoin.ediamond.ui.entry.face;

import android.os.Handler;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.main.MainActivityStarter;

import activitystarter.MakeActivityStarter;

/**
 * 人脸识别，注册后验证
 *
 * v1.0 已废弃
 */
@MakeActivityStarter
public class FaceIdActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_id;
    }

    @Override
    protected void onBeforeSetContentLayout() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        ToastUtils.showShort("人脸识别");
        new Handler().postDelayed(() -> {
            MainActivityStarter.start(this);
        }, 1000);
    }
}

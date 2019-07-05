package com.lovecoin.ediamond.ui.guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.SPUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/22.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@MakeActivityStarter
public class GuideActivity extends BaseActivity {
    public static final String TYPE_HAS_RELATION = "type_has_relation";
    public static final String TYPE_NOT_HAS_RELATION = "type_not_has_relation";
    public static final String PREF_KEY_GUIDE = "USER_GUIDE";

    @Arg
    String type;

    @BindView(R.id.activity_guide_frag_container)
    FrameLayout mFragContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        if (TextUtils.isEmpty(type)) {
            this.finish();
            return;
        }

//        if (shown) {
//            this.finish();
//        }
    }

    @Override
    protected void initView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = getFragmentByType();
        if (fragment == null) {
            this.finish();
            return;
        }
        transaction.add(R.id.activity_guide_frag_container, fragment);
        transaction.commit();
    }

    @Override
    protected void initData() {

    }

    private Fragment getFragmentByType() {
        Fragment fragment = null;

        switch (type) {
            case TYPE_HAS_RELATION:
                fragment = new GuideHasRelationFragment();
                break;

            case TYPE_NOT_HAS_RELATION:
                fragment = new GuideNotHasRelationFragment();
                break;
        }

        return fragment;
    }
}

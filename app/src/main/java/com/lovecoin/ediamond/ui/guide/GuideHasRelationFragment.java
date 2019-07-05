package com.lovecoin.ediamond.ui.guide;

import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/22.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class GuideHasRelationFragment extends BaseFragment {

    @BindView(R.id.main_center_iv_action)
    ImageView mIvAction;

    @Override
    protected void onBeforeSetContentLayout() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guide_has_relation;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.main_center_iv_action)
    public void onActionClick(View v) {
        getActivity().finish();
    }

    @Override
    protected void initData() {
        SPUtils.getInstance(GuideActivity.PREF_KEY_GUIDE).put(GuideActivity.TYPE_HAS_RELATION, true);
    }
}

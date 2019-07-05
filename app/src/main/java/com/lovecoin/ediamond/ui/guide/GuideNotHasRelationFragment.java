package com.lovecoin.ediamond.ui.guide;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/22.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class GuideNotHasRelationFragment extends BaseFragment {

    @BindView(R.id.iv_guide)
    ImageView mIvGuide;

    @BindView(R.id.iv_bg)
    ImageView mIvBg;

    @BindView(R.id.iv_guide_customer)
    ImageView ivGuideCustomer;

    @BindView(R.id.cl_guide_bottom)
    ViewGroup ivGuideBottom;
    private boolean isInstallMeIssager;
    private int index = 0;
    private int[] guideImgRes = {};

    private int[] guideImgResNotInstallMessager =
            {R.drawable.guide_un_02, R.drawable.guide_un_03, R.drawable.guide_un_04, R.drawable.guide_un_05, R.drawable.guide_un_06, R.drawable.guide_un_07, R.drawable.guide_un_08, R.drawable.guide_un_09};
    private int[] guideImgResInstallMessager = {R.drawable.guide_un_02, R.drawable.guide_un_03, R.drawable.guide_un_04, R.drawable.guide_un_06, R.drawable.guide_un_07, R.drawable.guide_un_08, R.drawable.guide_un_09};

    @Override
    protected void onBeforeSetContentLayout() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guide_not_has_relation;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        SPUtils.getInstance(GuideActivity.PREF_KEY_GUIDE).put(GuideActivity.TYPE_NOT_HAS_RELATION, true);

        isInstallMeIssager = MessageDialog.canShow(ShareLinkContent.class);
        if (isInstallMeIssager) {
            guideImgRes = guideImgResInstallMessager;
        } else {
            guideImgRes = guideImgResNotInstallMessager;
        }
    }

    @OnClick(R.id.iv_guide_customer)
    public void onActionClick(View v) {
        ivGuideBottom.setVisibility(View.GONE);
        ivGuideCustomer.setVisibility(View.GONE);
        mIvGuide.setVisibility(View.VISIBLE);
        performPage();
    }

    @OnClick(R.id.iv_guide)
    public void onGuideClick(View view) {
        performPage();
    }

    private void performPage() {
        if (index >= guideImgRes.length) {
            getActivity().finish();
            return;
        }
        int drawable = guideImgRes[index];
        refreshGuideBottom();
        index++;

        mIvGuide.setImageResource(drawable);
    }

    private void refreshGuideBottom() {
        if (isInstallMeIssager) {
            refreshGuideBottomInstallMessager();
        } else {
            refreshGuideBottomNotInstallMessager();
        }

    }

    private void refreshGuideBottomInstallMessager() {
        if (index == 0 || index == 5 || index == 6) {
            ivGuideBottom.setVisibility(View.VISIBLE);
        } else {
            ivGuideBottom.setVisibility(View.GONE);
        }
    }

    private void refreshGuideBottomNotInstallMessager() {
        if (index == 0 || index == 6 || index == 7) {
            ivGuideBottom.setVisibility(View.VISIBLE);
        } else {
            ivGuideBottom.setVisibility(View.GONE);
        }
    }
}

package com.lovecoin.ediamond.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.dialog.Loading;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import activitystarter.ActivityStarter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/10/25 0025.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar) CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setNavColor() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, getNavColorRes()));
        }
        ActivityStarter.fill(this);

        if (!hasToolbar()) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        onBeforeSetContentLayout();

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        }

        if (hasToolbar()) {
            initToolbar();
        }

        initView();
        initData();
    }

    public boolean setNavColor() {
        return false;
    }

    public int getNavColorRes() {
        return R.color.app_f7f9fe;
    }

    protected abstract int getLayoutId();

    protected abstract void onBeforeSetContentLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected boolean hasToolbar() {
        return true;
    }

    protected View inflateView(int resId) {
        return getLayoutInflater().inflate(resId, null);
    }

    protected boolean showBackButton() {
        return true;
    }

    protected void init(Bundle savedInstanceState) {
    }

    protected void initToolbar() {
        if (toolbar == null)
            return;

        //back button
        toolbar.enableBack(showBackButton());
        toolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
    }

    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    private QMUITipDialog loadingDialog;

    public void showLoading() {
        dismissLoading();

        loadingDialog = Loading.showLoading(this);
        loadingDialog.show();

    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }
}

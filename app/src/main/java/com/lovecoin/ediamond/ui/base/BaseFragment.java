package com.lovecoin.ediamond.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activitystarter.ActivityStarter;
import butterknife.ButterKnife;

/**
 * Created on 2017/10/26 0026.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityStarter.fill(this);

        onBeforeSetContentLayout();
        View view = null;

        if (getLayoutId() != 0) {
            view = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, view);
        }

        initView();
        initData();
        return view;
    }

    protected abstract void onBeforeSetContentLayout();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

}

package com.lovecoin.ediamond.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.lovecoin.ediamond.R;

/**
 * Created on 2017/10/26 0026.
 */

public class CenterTabFrameLayout extends FrameLayout {

    private RadioButton tabItem1;
    private RadioButton tabItem2;
    private OnTabClickListener onTabClickListener;
    private int width;

    public CenterTabFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public CenterTabFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenterTabFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CenterTabFrameLayout setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
        return this;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_center_tab_layout, this);
        tabItem1 = view.findViewById(R.id.tab_item_1);
        tabItem2 = view.findViewById(R.id.tab_item_2);

        tabItem1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    return;
                }
                onTabClick(0);
                tabItem1.animate()
                        .translationXBy(width - tabItem1.getWidth() / 2)
                        .start();
                tabItem2.animate()
                        .translationXBy(width - tabItem2.getWidth() / 2)
                        .start();
                tabItem2.setChecked(false);
            }
        });
        tabItem2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    return;
                }
                onTabClick(1);
                tabItem1.animate()
                        .translationXBy(tabItem1.getWidth() / 2 - width)
                        .start();
                tabItem2.animate()
                        .translationXBy(tabItem2.getWidth() / 2 - width)
                        .start();
                tabItem1.setChecked(false);
            }
        });
    }

    public void onTabClick(int position) {
        if (onTabClickListener != null) {
            onTabClickListener.onTabClick(position);
        }
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w / 2;
    }
}

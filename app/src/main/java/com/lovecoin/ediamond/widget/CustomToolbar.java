package com.lovecoin.ediamond.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovecoin.ediamond.R;

public class CustomToolbar extends RelativeLayout {
    private String title;
    private float titleSize;
    private int titleColor;
    private String menu;
    private int menuIcon;

    private ImageView backIv;
    private TextView titleTv;
    private TextView menuTv;

    public CustomToolbar(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomToolbar, defStyle, 0);

        title = a.getString(R.styleable.CustomToolbar_title);
        menu = a.getString(R.styleable.CustomToolbar_menuText);
        menuIcon = a.getResourceId(R.styleable.CustomToolbar_menuIcon, -1);
        titleColor = a.getColor(R.styleable.CustomToolbar_titleColor, getResources().getColor(R.color.app_3e4f6c));
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        titleSize = a.getDimension(R.styleable.CustomToolbar_titleSize, 16);

        a.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.view_custom_toolbar, this);
        backIv = findViewById(R.id.toolbar_back_iv);
        titleTv = findViewById(R.id.toolbar_title_tv);
        menuTv = findViewById(R.id.toolbar_menu_tv);

        setMenu(menu, menuIcon);
        setTitle(title);
        setTitleColor(titleColor);
        setTitleSize(titleSize);
    }

    public CustomToolbar setTitle(String title) {
        this.title = title;
        titleTv.setText(title);
        return this;
    }

    public CustomToolbar setMenu(String menu, int menuIcon) {
        this.menu = menu;
        this.menuIcon = menuIcon;
        menuTv.setText(menu);
        if (menuIcon > 0) {
            menuTv.setCompoundDrawablesRelativeWithIntrinsicBounds(menuIcon, 0, 0, 0);
        }
        return this;
    }

    public CustomToolbar setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        titleTv.setTextSize(titleSize);
        return this;
    }

    public CustomToolbar setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        titleTv.setTextColor(titleColor);
        return this;
    }

    public CustomToolbar enableBack(boolean enable) {
        backIv.setVisibility(enable ? View.VISIBLE : View.GONE);
        return this;
    }

    public CustomToolbar setOnBackClickListener(View.OnClickListener onClickListener) {
        backIv.setOnClickListener(onClickListener);
        return this;
    }

    public CustomToolbar setMenuClickListener(View.OnClickListener onClickListener) {
        menuTv.setOnClickListener(onClickListener);
        return this;
    }
}

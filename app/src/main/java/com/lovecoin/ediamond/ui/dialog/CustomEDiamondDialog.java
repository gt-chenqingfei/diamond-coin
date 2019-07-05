package com.lovecoin.ediamond.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lovecoin.ediamond.R;

/**
 * 自定义全局 Dialog
 * <p>
 * Created by ZhaiDongyang on 2018/6/1
 */
public class CustomEDiamondDialog extends Dialog implements DialogInterface.OnKeyListener {

    private Context mContext;
    private View mView;// 传入一个布局，使其可以操作布局中的控件
    private boolean mCanceledOnTouchOutside;// 点击外部区域是否关闭，默认点击不关闭，设置 true 关闭
    private boolean mCanceledOnBackKey;// 点击返回键是否关闭，默认点击关闭，设置 true 不关闭
    private boolean mCanceledOnBackKeyActivity = false;// 点击返回键关闭页面和 Dialog
    private int mHeight, mWidth;// 默认设置 Dialog 最大宽度，也手动可以设置其宽和高

    private CustomEDiamondDialog(Builder builder) {
        super(builder.mContext);
        mContext = builder.mContext;
        mView = builder.mView;
        mCanceledOnTouchOutside = builder.canceledOnTouchOutside;
        mCanceledOnBackKey = builder.canceledOnBackKey;
        mHeight = builder.height;
        mWidth = builder.width;
    }

    public CustomEDiamondDialog(Builder builder, int resStyle) {
        super(builder.mContext, resStyle);
        mContext = builder.mContext;
        mView = builder.mView;
        mCanceledOnTouchOutside = builder.canceledOnTouchOutside;
        mCanceledOnBackKey = builder.canceledOnBackKey;
        mHeight = builder.height;
        mWidth = builder.width;
    }

    private void setDialogWidthHeight(CustomEDiamondDialog dialog) {
        WindowManager wm = dialog.getWindow().getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int windowWidth = point.x;

        int windowHorizontalPadding = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_horizontal_margin);
        int maxWidth = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_max_width);
        int calculatedWidth = windowWidth - (windowHorizontalPadding * 2);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window dialogWindow = dialog.getWindow();
        lp.copyFrom(dialogWindow.getAttributes());
        lp.gravity = Gravity.CENTER;

        if (mWidth == 0) lp.width = Math.min(maxWidth, calculatedWidth);
        else lp.width = mWidth;
        if (mHeight != 0) lp.height = mHeight;

        lp.dimAmount = 0.7f;
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        setOnKeyListener(this);
        setDialogWidthHeight(this);
    }

    public View getView() {
        return mView;
    }

    public String getTextContent(int textView) {
        TextView textViewContent = mView.findViewById(textView);
        return textViewContent.getText().toString();
    }

    public static final class Builder {
        private Context mContext;
        private View mView;
        private boolean canceledOnTouchOutside = false;
        private boolean canceledOnBackKey = false;
        private int height;
        private int width;
        private int resStyle = -1;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder mContext(Context context) {
            this.mContext = context;
            return this;
        }

        public Builder style(int style) {
            this.resStyle = style;
            return this;
        }

        public Builder view(int view) {
            mView = LayoutInflater.from(mContext).inflate(view, null);
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder canceledOnBackKey(boolean canceledOnBackKey) {
            this.canceledOnBackKey = canceledOnBackKey;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder setViewOnClick(int view, View.OnClickListener listener) {
            mView.findViewById(view).setOnClickListener(listener);
            return this;
        }

        public Builder setViewVisibility(int view, int isGone) {
            mView.findViewById(view).setVisibility(isGone);
            return this;
        }

        public Builder setViewContent(int view, String content) {
            TextView textView = mView.findViewById(view);
            textView.setText(content);
            return this;
        }

        /**
         * 对全局 Dialog 使用特定的样式，可以自定义
         *
         * @return
         */
        public CustomEDiamondDialog build() {
            if (resStyle == -1) {
                return new CustomEDiamondDialog(this, R.style.CustomDialog);
            } else {
                return new CustomEDiamondDialog(this, resStyle);
            }
        }
    }

    public boolean canceledOnBackKeyActivity(boolean canceledOnBackKeyActivity) {
        this.mCanceledOnBackKeyActivity = canceledOnBackKeyActivity;
        return  mCanceledOnBackKeyActivity;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
            if (mCanceledOnBackKeyActivity) {
                ((Activity)mContext).finish();
                return false;
            } else {
                return mCanceledOnBackKey;
            }
        return false;
    }
}

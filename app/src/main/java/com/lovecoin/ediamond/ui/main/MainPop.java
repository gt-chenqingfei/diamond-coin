package com.lovecoin.ediamond.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.lovecoin.ediamond.R;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

/**
 * Created on 2017/11/12 0012.
 */

public class MainPop {

    public static void showLeft(Context context, View view) {

        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_main_left, null);

        EasyPopup popWindow = new EasyPopup(context)
                .setContentView(contentView)
                .setFocusAndOutsideEnable(true)//是否允许点击PopupWindow之外的地方消失
                .createPopup();
        popWindow.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.ALIGN_RIGHT,  0, -100);
    }

    public static void showRight(Context context, View view) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_main_right, null);

        EasyPopup popWindow = new EasyPopup(context)
                .setContentView(contentView)
                .setFocusAndOutsideEnable(true)//是否允许点击PopupWindow之外的地方消失
                .createPopup();
        popWindow.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.ALIGN_LEFT, 0, -100);
    }

    public static void showCancel(Context context, View view, PopupWindow.OnDismissListener dismissListener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_main_cancel, null);

        EasyPopup popWindow = new EasyPopup(context)
                .setContentView(contentView)
                .setFocusAndOutsideEnable(true)//是否允许点击PopupWindow之外的地方消失
                .setOnDismissListener(dismissListener)
                .createPopup();
        popWindow.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.CENTER);
    }

}

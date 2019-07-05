package com.lovecoin.ediamond.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.SizeUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.widget.JustifyTextView;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

/**
 * Created on 2017/11/12 0012.
 */

public class MainTips {

    public static void show(Context context, View view, String content, PopupWindow.OnDismissListener dismissListener) {

        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_main_tips, null);
        JustifyTextView contentText = contentView.findViewById(R.id.main_tips_tv);
        contentText.setText(content);
        EasyPopup popWindow = new EasyPopup(context)
                .setContentView(contentView)
                .setWidth(SizeUtils.dp2px(240))
                .setOnDismissListener(dismissListener)
                .setFocusAndOutsideEnable(true)//是否允许点击PopupWindow之外的地方消失
                .createPopup();

        popWindow.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.CENTER);
    }

}

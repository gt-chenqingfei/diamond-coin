package com.lovecoin.ediamond.ui.dialog;

import android.content.Context;

import com.lovecoin.ediamond.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class Loading {

    public static QMUITipDialog showTips(Context context, String tips) {
        return new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
    }

    public static QMUITipDialog showLoading(Context context) {
        return new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(context.getString(R.string.tips_loading))
                .create();
    }
}

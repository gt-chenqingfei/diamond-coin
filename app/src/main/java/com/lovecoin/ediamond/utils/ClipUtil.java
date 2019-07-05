package com.lovecoin.ediamond.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;

/**
 * @author qingfei.chen
 * @since 2018/5/26.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ClipUtil {
    public static void copyText(String copiedText, Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, copiedText));
        ToastUtils.showShort(R.string.str_copied);
    }
}

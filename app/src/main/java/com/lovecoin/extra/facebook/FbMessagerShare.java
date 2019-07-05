package com.lovecoin.extra.facebook;

import android.net.Uri;

import com.blankj.utilcode.util.ToastUtils;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.lovecoin.ediamond.R;

public class FbMessagerShare {

    public static boolean share(MessageDialog messageDialog, String url) {
        if (messageDialog == null) {
            return false;
        }
        if (MessageDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(url))
                    .build();
            messageDialog.show(linkContent);
            return true;
        }

        return false;
    }
}

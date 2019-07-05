package com.lovecoin.ediamond.api.utils;

import android.util.Base64;

import com.blankj.utilcode.util.StringUtils;
import com.lovecoin.ediamond.app.Const;

/**
 * 接口加密解密
 */

public class EncryptUtils {

    /**
     * 还原 api message， base64->3ds->message
     */
    public static String decode(String message, String key) {
        if (StringUtils.isEmpty(message)) {
            return "";
        }
        byte[] decode = Base64.decode(message.getBytes(), Base64.DEFAULT);
        try {
            byte[] bytes = Java3DES.des3DecodeCBC(key, Const.DES_KEY_IV, decode);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符串 -> 3ds -> base64
     */
    public static String encode(String message, String key) {

        if (StringUtils.isEmpty(message)) {
            return "";
        }

        String result = "";
        try {
            byte[] bytes = Java3DES.des3EncodeCBC(key,
                    Const.DES_KEY_IV,
                    message.getBytes());
            return Base64.encodeToString(bytes, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

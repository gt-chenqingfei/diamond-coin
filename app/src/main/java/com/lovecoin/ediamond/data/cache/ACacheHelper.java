package com.lovecoin.ediamond.data.cache;

import com.blankj.utilcode.util.Utils;

/**
 * Created on 2017/11/20 0020.
 */

public class ACacheHelper {
    public static ACache get() {
        return ACache.get(Utils.getApp());
    }
}

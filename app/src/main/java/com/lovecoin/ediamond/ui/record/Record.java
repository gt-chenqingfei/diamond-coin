package com.lovecoin.ediamond.ui.record;

import android.support.annotation.Keep;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created on 2017/11/9 0009.
 */
@Keep
public class Record implements MultiItemEntity {
    public static final int TYPE_DATE = 5;
    public static final int TYPE_0 = 0;
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_EMPTY = 1101001;

    public String date;
    public String time;
    public String coin;
    public String value;
    public int type;

    @Override
    public int getItemType() {
        return type;
    }
}

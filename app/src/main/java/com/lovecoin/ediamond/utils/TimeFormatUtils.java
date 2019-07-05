package com.lovecoin.ediamond.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 只是格式化此应用内的时间，显示英文且要跟当前时区一致。
 * 如果无此特殊需求，则可以直接用 com.blankj.utilcode.util.TimeUtils 工具类，能满足大部分需求。
 *
 * Created by ZhaiDongyang on 2018/6/13
 */
public class TimeFormatUtils {

    // 30/06 07:00 PM
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm a", Locale.ENGLISH);
    // 30/06/2018 07:00 PM
    private static SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.ENGLISH);

    public TimeFormatUtils() {
        DATE_FORMAT_YEAR.setTimeZone(TimeZone.getDefault());
        DATE_FORMAT.setTimeZone(TimeZone.getDefault());
    }

    public static String millis2String(long millis) {
        return DATE_FORMAT.format(new Date(millis));
    }

    public static String millis2StringYear(long millis) {
        return DATE_FORMAT_YEAR.format(new Date(millis));
    }

}

package com.lovecoin.ediamond.api.entity.params;

/**
 * Created on 2017/11/27.
 */

public class MyRecordsParams {

    public static final int TYPE_SEND = 0;
    public static final int TYPE_RECEIVE = 1;
    public static final int TYPE_BUY = 2;

    private String type;
    private String pageNo;
    private String tYear;
    private String tMonth;

    public MyRecordsParams(String type, String pageNo, String tYear, String tMonth) {
        this.type = type;
        this.pageNo = pageNo;
        this.tYear = tYear;
        this.tMonth = tMonth;
    }
}

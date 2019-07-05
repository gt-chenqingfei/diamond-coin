package com.lovecoin.ediamond.api.entity.params;

/**
 * Created on 2017/11/24.
 */

public class MyRecordCountParams {
    private String tYear;// :年份,
    private String tMonth;// :月份

    public MyRecordCountParams(String tYear, String tMonth) {
        this.tYear = tYear;
        this.tMonth = tMonth;
    }

    public String gettYear() {
        return tYear;
    }

    public void settYear(String tYear) {
        this.tYear = tYear;
    }

    public String gettMonth() {
        return tMonth;
    }

    public void settMonth(String tMonth) {
        this.tMonth = tMonth;
    }
}

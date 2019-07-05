package com.lovecoin.ediamond.app;

/**
 * 应用常量
 */

public interface Const {

    byte[] DES_KEY_IV = "eDiamond".getBytes();

    //email address
    String EMAIL_ADDRESS = "ask@ediamond.love";

    // 提币记录页面需要的常量
    int TRADE_STATUS_PENDING = 0;
    int TRADE_STATUS_FINISHED = 1;
    int TRADE_STATUS_FAILED = -1;
    String TRADE_STATUS_PENDING_CONTENT = "Pending";
    String TRADE_STATUS_FINISHED_CONTENT = "Succeed";
    String TRADE_STATUS_FAILED_CONTENT = "Failed";
    String TRADE_UNIT = " eDiamonds";
}

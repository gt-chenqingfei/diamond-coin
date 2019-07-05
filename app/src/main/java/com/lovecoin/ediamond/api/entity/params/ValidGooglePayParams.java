package com.lovecoin.ediamond.api.entity.params;

/**
 * Created on 2018/1/8.
 */

public class ValidGooglePayParams {

    private String jsonPurchaseInfo;
    private String signature;

    public ValidGooglePayParams(String jsonPurchaseInfo, String signature) {
        this.jsonPurchaseInfo = jsonPurchaseInfo;
        this.signature = signature;
    }
}

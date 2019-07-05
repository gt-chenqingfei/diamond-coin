package com.lovecoin.ediamond.api.entity.params;

/**
 * Created on 2017/11/28.
 */

public class RegisterPushParams {

    private String pushToken;//:推送标识，
    private String osType = "1";//：推送系统类型（0：ios，1：android）

    public RegisterPushParams(String pushToken) {
        this.pushToken = pushToken;
    }
}

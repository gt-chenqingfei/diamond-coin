package com.lovecoin.ediamond.api.entity.resp;

/**
 * Created on 2017/11/15 0015.
 */

public class BaseInfo {
    private String token;
    private String secretKey;

    public String getToken() {
        return token;
    }

    public BaseInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public BaseInfo setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}

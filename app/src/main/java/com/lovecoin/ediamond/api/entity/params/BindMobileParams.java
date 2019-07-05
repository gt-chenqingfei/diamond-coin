package com.lovecoin.ediamond.api.entity.params;

public class BindMobileParams {

    private String platform;
    private String accountId;
    private String areaCode;
    private String mobile;
    private String vCode;

    public String getPlatform() {
        return platform;
    }

    public BindMobileParams setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public BindMobileParams setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public BindMobileParams setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public BindMobileParams setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getvCode() {
        return vCode;

    }

    public BindMobileParams setvCode(String vCode) {
        this.vCode = vCode;
        return this;
    }
}

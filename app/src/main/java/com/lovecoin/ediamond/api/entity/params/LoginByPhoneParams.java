package com.lovecoin.ediamond.api.entity.params;

public class LoginByPhoneParams {

    private String vCode;
    private String mobile;
    private String areaCode;

    public String getMobile() {
        return mobile;
    }

    public LoginByPhoneParams setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }


    public String getvCode() {
        return vCode;
    }

    public LoginByPhoneParams setvCode(String vCode) {
        this.vCode = vCode;
        return this;
    }


    public String getAreaCode() {
        return areaCode;
    }

    public LoginByPhoneParams setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }
}

package com.lovecoin.ediamond.api.entity.params;


public class ValidateUserMobileParams {
    private String areaCode;
    private String mobile;
    private String vCode;

    public ValidateUserMobileParams(String areaCode, String mobile, String vCode) {
        this.areaCode = areaCode;
        this.mobile = mobile;
        this.vCode = vCode;
    }
}

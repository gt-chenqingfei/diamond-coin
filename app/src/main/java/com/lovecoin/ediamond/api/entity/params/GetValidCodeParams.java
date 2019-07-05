package com.lovecoin.ediamond.api.entity.params;


public class GetValidCodeParams {
    private String type;
    private String areaCode;
    private String mobile;


    public GetValidCodeParams(String type, String areaCode, String mobile) {
        this.type = type;
        this.areaCode = areaCode;
        this.mobile = mobile;
    }
}

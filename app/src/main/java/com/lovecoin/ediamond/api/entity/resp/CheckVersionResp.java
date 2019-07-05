package com.lovecoin.ediamond.api.entity.resp;

/**
 * Created on 2017/12/21.
 */

public class CheckVersionResp {

    private boolean must;//;//true/false,
    private String iosVersionNo;////ios版本号
    private String iosDownloadUrl;////ios下载地址
    private String androidVersionNo;////android版本号
    private String androidDownloadUrl;////android下载地址
    private String androidDownloadContent;
    private String iosDownloadContent;

    public boolean getMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }

    public String getIosVersionNo() {
        return iosVersionNo;
    }

    public void setIosVersionNo(String iosVersionNo) {
        this.iosVersionNo = iosVersionNo;
    }

    public String getIosDownloadUrl() {
        return iosDownloadUrl;
    }

    public void setIosDownloadUrl(String iosDownloadUrl) {
        this.iosDownloadUrl = iosDownloadUrl;
    }

    public String getAndroidVersionNo() {
        return androidVersionNo;
    }

    public void setAndroidVersionNo(String androidVersionNo) {
        this.androidVersionNo = androidVersionNo;
    }

    public String getAndroidDownloadUrl() {
        return androidDownloadUrl;
    }

    public void setAndroidDownloadUrl(String androidDownloadUrl) {
        this.androidDownloadUrl = androidDownloadUrl;
    }

    public String getAndroidDownloadContent() {
        return androidDownloadContent;
    }

    public void setAndroidDownloadContent(String androidDownloadContent) {
        this.androidDownloadContent = androidDownloadContent;
    }

    public String getIosDownloadContent() {
        return iosDownloadContent;
    }

    public void setIosDownloadContent(String iosDownloadContent) {
        this.iosDownloadContent = iosDownloadContent;
    }
}

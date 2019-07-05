package com.lovecoin.ediamond.api.entity.resp;

/**
 * Created on 2017/11/28.
 */

public class SendResp {
    private boolean hasRelation;//true //true 存在关系，false 不存在关系
    private String url;//http;////xxx;//8080/test,
    private String title;//分享标题,
    private String logoUrl;//分享图标
    private int coinTotal;

    public boolean isHasRelation() {
        return hasRelation;
    }

    public void setHasRelation(boolean hasRelation) {
        this.hasRelation = hasRelation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getCoinTotal() {
        return coinTotal;
    }
}

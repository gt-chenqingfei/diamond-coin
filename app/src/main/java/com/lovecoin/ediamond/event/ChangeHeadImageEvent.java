package com.lovecoin.ediamond.event;

/**
 * 修改头像
 */
public class ChangeHeadImageEvent {

    private String url;

    public ChangeHeadImageEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

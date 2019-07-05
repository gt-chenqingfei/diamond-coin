package com.lovecoin.ediamond.event;

/**
 * Created by kassadin on 17/12/3.
 */

public class StartMainEvent {
    private String nickName;

    public StartMainEvent() {
    }

    public StartMainEvent(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

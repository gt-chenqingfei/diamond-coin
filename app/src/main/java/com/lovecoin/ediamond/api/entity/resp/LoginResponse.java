package com.lovecoin.ediamond.api.entity.resp;

/**
 * @author qingfei.chen
 * @since 2018/5/23.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class LoginResponse {
    public static final int FLAG_NO = 0;
    public static final int FLAG_YES = 1;

    public static final String PLATFORM_FB = "fb";
    public static final String PLATFORM_WEIBO = "weibo";
    public static final String PLATFORM_GOOGLE = "google";

    private String userId;
    private int profileFlag;
    private String nickname;
    private String avatar;
    private int gender;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProfileFlag() {
        return profileFlag;
    }

    public void setProfileFlag(int profileFlag) {
        this.profileFlag = profileFlag;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

}

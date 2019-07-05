package com.lovecoin.ediamond.api.entity.params;

public class LoginByFbParams {

    private String fbAccount;
    private String fbNickname;
    private String avatar;
    private int gender;

    public String getFbAccount() {
        return fbAccount;
    }

    public LoginByFbParams setFbAccount(String fbAccount) {
        this.fbAccount = fbAccount;
        return this;
    }

    public String getFbNickname() {
        return fbNickname;
    }

    public LoginByFbParams setFbNickname(String fbNickname) {
        this.fbNickname = fbNickname;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public LoginByFbParams setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public LoginByFbParams setGender(int gender) {
        this.gender = gender;
        return this;
    }
}

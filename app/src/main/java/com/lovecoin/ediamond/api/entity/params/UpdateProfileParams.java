package com.lovecoin.ediamond.api.entity.params;

public class UpdateProfileParams {

    private String nickname;
    private String avatar;
    private int gender;

    public String getNickname() {
        return nickname;
    }

    public UpdateProfileParams setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public UpdateProfileParams setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public UpdateProfileParams setGender(int gender) {
        this.gender = gender;
        return this;
    }
}

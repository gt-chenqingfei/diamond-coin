package com.lovecoin.ediamond.api.entity.params;

/**
 * {
 *
 * @param type:登录类型：0                       手机密码，1 手机验证码，2 Facebook
 * @param mobile：手机号（type:0,1传）
 * @param pwd：密码（type:0）
 * @param vCode：验证码（type:1传）
 * @param fbAccount：Facebook账户（type:2传）
 * @param fbNickName：facebook昵称（type:2传）
 * @param headImgUrl：Facebook图像（type:2传）
 * @param sex：性别（type:2传）
 * @param country：国家（type:2传）
 * @param countrySimpleCode：国家简码（type:2传）
 * @param areaCode：区域码（type:2传）
 * @param fbFriendNum：Facebook好友数量（type:2传） }
 */

public class LoginParams {

    private String type;
    private String mobile;
    private String pwd;
    private String vCode;
    private String fbAccount;
    private String fbNickName;
    private String headImgUrl;
    private String sex;
    private String country;
    private String countrySimpleCode;
    private String areaCode;
    private String fbFriendNum;

    public String getType() {
        return type;
    }

    public LoginParams setType(String type) {
        this.type = type;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public LoginParams setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public LoginParams setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public String getvCode() {
        return vCode;
    }

    public LoginParams setvCode(String vCode) {
        this.vCode = vCode;
        return this;
    }

    public String getFbAccount() {
        return fbAccount;
    }

    public LoginParams setFbAccount(String fbAccount) {
        this.fbAccount = fbAccount;
        return this;
    }

    public String getFbNickName() {
        return fbNickName;
    }

    public LoginParams setFbNickName(String fbNickName) {
        this.fbNickName = fbNickName;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public LoginParams setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public LoginParams setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public LoginParams setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountrySimpleCode() {
        return countrySimpleCode;
    }

    public LoginParams setCountrySimpleCode(String countrySimpleCode) {
        this.countrySimpleCode = countrySimpleCode;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public LoginParams setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getFbFriendNum() {
        return fbFriendNum;
    }

    public LoginParams setFbFriendNum(String fbFriendNum) {
        this.fbFriendNum = fbFriendNum;
        return this;
    }
}

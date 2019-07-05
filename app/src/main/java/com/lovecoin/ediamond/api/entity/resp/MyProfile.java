package com.lovecoin.ediamond.api.entity.resp;

/**
 * {
 * "buyNum": "5",
 * "currTime": "2017-11-22 01:05:39",
 * "deviceInfo": "",
 * "fbAccount": "470393147@qq.com",
 * "fbNickName": "杨继",
 * "id": "2",
 * "lastLoginTime": "",
 * "loveStatus": 0,
 * "receiveNum": "12",
 * "registerTimeLong": 1324546576654,
 * "registerTimeStr": "2017-10-12",
 * "sendNum": "5",
 * "sex": 1,
 * "userStatus": 0
 * }
 */

public class MyProfile {

    private String buyNum = "0";
    private String currTime;
    private String deviceInfo;
    private String fbAccount;
    private String nickname;
    private String id;
    private String lastLoginTime;
    private int loveStatus; //真爱状态0单身 1建立关系
    private String receiveNum = "0";
    private long registerTimeLong;
    private String registerTimeStr;
    private String sendNum = "0";
    private int sex;
    private int userStatus;
    private int account;//数字钱包账户
    private String headImgUrl;
    private String umobile;
    private String totalCoin;//:"12" //总币量
    private String coinValue;//120" //币对应的价格
    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(String totalCoin) {
        this.totalCoin = totalCoin;
    }

    public String getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(String coinValue) {
        this.coinValue = coinValue;
    }

    public int getAccount() {
        return account;
    }

    public MyProfile setAccount(int account) {
        this.account = account;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public MyProfile setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }

    public String getUmobile() {
        return umobile;
    }

    public MyProfile setUmobile(String umobile) {
        this.umobile = umobile;
        return this;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setCurrTime(String currTime) {
        this.currTime = currTime;
    }

    public String getCurrTime() {
        return currTime;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setFbAccount(String fbAccount) {
        this.fbAccount = fbAccount;
    }

    public String getFbAccount() {
        return fbAccount;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public String getNickName() {
        return nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLoveStatus(int loveStatus) {
        this.loveStatus = loveStatus;
    }

    public int getLoveStatus() {
        return loveStatus;
    }

    public void setReceiveNum(String receiveNum) {
        this.receiveNum = receiveNum;
    }

    public String getReceiveNum() {
        return receiveNum;
    }

    public long getRegisterTimeLong() {
        return registerTimeLong;
    }

    public MyProfile setRegisterTimeLong(long registerTimeLong) {
        this.registerTimeLong = registerTimeLong;
        return this;
    }

    public void setRegisterTimeStr(String registerTimeStr) {
        this.registerTimeStr = registerTimeStr;
    }

    public String getRegisterTimeStr() {
        return registerTimeStr;
    }

    public void setSendNum(String sendNum) {
        this.sendNum = sendNum;
    }

    public String getSendNum() {
        return sendNum;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getUserStatus() {
        return userStatus;
    }

}

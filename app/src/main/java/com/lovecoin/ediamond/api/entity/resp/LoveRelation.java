package com.lovecoin.ediamond.api.entity.resp;

import com.blankj.utilcode.util.StringUtils;

/**
 * Created on 2017/11/24.
 */

public class LoveRelation {

    private String id;//主键
    private String sponsorUid;//发起方
    private String sponsorUmobile;//发起人手机号
    private String sponsorFbAccount;//发起人Facebook账号
    private String sponsorFbNickName;//发起人Facebook昵称
    private int sponsorSex;//发起人性别
    public String sponsorHeadImg;
    private String receiverUid;//接收方
    private String receiverUmobile;//响应人手机号
    private String receiverFbAccount;//响应人Facebook账号
    private String receiverFbNickName;//响应人Facebook昵称
    public String receiverHeadImg;
    private int receiverSex;//响应人性别
    private int loveScore;//真爱值
    private String buildTime;//建立关系时间
    private int sendNum;
    private int receiveNum;

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public int getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(int receiveNum) {
        this.receiveNum = receiveNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSponsorUid() {
        return sponsorUid;
    }

    public void setSponsorUid(String sponsorUid) {
        this.sponsorUid = sponsorUid;
    }

    public String getSponsorUmobile() {
        return sponsorUmobile;
    }

    public void setSponsorUmobile(String sponsorUmobile) {
        this.sponsorUmobile = sponsorUmobile;
    }

    public String getSponsorFbAccount() {
        return sponsorFbAccount;
    }

    public void setSponsorFbAccount(String sponsorFbAccount) {
        this.sponsorFbAccount = sponsorFbAccount;
    }

    public String getSponsorFbNickName() {
        return sponsorFbNickName;
    }

    public void setSponsorFbNickName(String sponsorFbNickName) {
        this.sponsorFbNickName = sponsorFbNickName;
    }

    public int getSponsorSex() {
        return sponsorSex;
    }

    public void setSponsorSex(int sponsorSex) {
        this.sponsorSex = sponsorSex;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getReceiverUmobile() {
        return receiverUmobile;
    }

    public void setReceiverUmobile(String receiverUmobile) {
        this.receiverUmobile = receiverUmobile;
    }

    public String getReceiverFbAccount() {
        return receiverFbAccount;
    }

    public void setReceiverFbAccount(String receiverFbAccount) {
        this.receiverFbAccount = receiverFbAccount;
    }

    public String getReceiverFbNickName() {
        return receiverFbNickName;
    }

    public void setReceiverFbNickName(String receiverFbNickName) {
        this.receiverFbNickName = receiverFbNickName;
    }

    public int getReceiverSex() {
        return receiverSex;
    }

    public void setReceiverSex(int receiverSex) {
        this.receiverSex = receiverSex;
    }

    public int getLoveScore() {
        return loveScore;
    }

    public void setLoveScore(int loveScore) {
        this.loveScore = loveScore;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public boolean isSingle() {
        return StringUtils.isEmpty(receiverUid);
    }

    public String getSponsorHeadImg() {
        return sponsorHeadImg;
    }

    public void setSponsorHeadImg(String sponsorHeadImg) {
        this.sponsorHeadImg = sponsorHeadImg;
    }

    public String getReceiverHeadImg() {
        return receiverHeadImg;
    }

    public void setReceiverHeadImg(String receiverHeadImg) {
        this.receiverHeadImg = receiverHeadImg;
    }
}

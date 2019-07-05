package com.lovecoin.ediamond.api.entity.resp;

/**
 * Created on 2017/11/24.
 * <p>
 * {
 * "beyondLover": "0%",
 * "coinValue": "0.0",
 * "endRelationTime": "2017-11-26",
 * "receiveNum": "0",
 * "relationTime": "0月",
 * "sendNum": "0",
 * "startRelationTime": "2017-11-26",
 * "totalCoin": "0",
 * "totalNum": "0"
 * },
 */

public class LoveRelationProfile {

    private String loverHeadImgUrl;//对方的头像
    private String loverMobilel;//手机号
    private String loverTitle;//上方显示的对方昵称或手机号
    private String totalCoin;//总币
    private String coinValue;//总币价值
    private String receiveNum;//Ta送我
    private String sendNum;//我送Ta
    private String totalNum;//总统计
    private String startRelationTime;//开始真爱时间
    private String endRelationTime;//结束真爱时间
    private String relationTime;//建立总时间
    private String beyondLover;//超过多少真爱

    public String getLoverTitle() {
        return loverTitle;
    }

    public void setLoverTitle(String loverTitle) {
        this.loverTitle = loverTitle;
    }

    public String getLoverHeadImgUrl() {
        return loverHeadImgUrl;
    }

    public void setLoverHeadImgUrl(String loverHeadImgUrl) {
        this.loverHeadImgUrl = loverHeadImgUrl;
    }

    public String getLoverMobilel() {
        return loverMobilel;
    }

    public void setLoverMobilel(String loverMobilel) {
        this.loverMobilel = loverMobilel;
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

    public String getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(String receiveNum) {
        this.receiveNum = receiveNum;
    }

    public String getSendNum() {
        return sendNum;
    }

    public void setSendNum(String sendNum) {
        this.sendNum = sendNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getStartRelationTime() {
        return startRelationTime;
    }

    public void setStartRelationTime(String startRelationTime) {
        this.startRelationTime = startRelationTime;
    }

    public String getEndRelationTime() {
        return endRelationTime;
    }

    public void setEndRelationTime(String endRelationTime) {
        this.endRelationTime = endRelationTime;
    }

    public String getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(String relationTime) {
        this.relationTime = relationTime;
    }

    public String getBeyondLover() {
        return beyondLover;
    }

    public void setBeyondLover(String beyondLover) {
        this.beyondLover = beyondLover;
    }
}

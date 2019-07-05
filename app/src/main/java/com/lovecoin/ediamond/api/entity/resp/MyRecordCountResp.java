package com.lovecoin.ediamond.api.entity.resp;

/**
 * Created on 2017/11/24.
 */

public class MyRecordCountResp {

    private String buyNum = "0";//购买数量
    private String receiveNum = "0"; //接收数量
    private String sendNum = "0"; //发送数量

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
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
}

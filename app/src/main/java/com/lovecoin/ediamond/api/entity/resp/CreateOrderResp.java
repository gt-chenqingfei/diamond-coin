package com.lovecoin.ediamond.api.entity.resp;

import java.math.BigDecimal;

/**
 * Created on 2017/11/26.
 */

public class CreateOrderResp {

    private int itemNum;// 5,//购买数量 （payType = 0 时返回）
    private BigDecimal itemPrice;// 12.0,//币单价（payType = 0 时返回）
    private BigDecimal itemTotalPrice;// 60.0,//支付价格（payType = 0 时返回）
    private String orderDesc;// 5diamond,//支付描述
    private String orderId;// 64a2928e2678405e9e193805675d6aaa,//支付订单id
    private String payType;// 0,//支付类型(0：paypal,1;// ipa)
    private String payer;// 58e14c5b43884425802fff4ea828e0f9 //付款人id
    private String payMoney;

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(BigDecimal itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}

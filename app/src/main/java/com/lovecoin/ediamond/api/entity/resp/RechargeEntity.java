package com.lovecoin.ediamond.api.entity.resp;

import java.math.BigDecimal;

/**
 * Created on 2017/11/22 0022.
 */

public class RechargeEntity {

    private String id;
    private String rechargeName;
    private int rechargeValue;
    private BigDecimal singlePrice;
    private BigDecimal totalPrice;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setRechargeName(String rechargeName) {
        this.rechargeName = rechargeName;
    }

    public String getRechargeName() {
        return rechargeName;
    }

    public void setRechargeValue(int rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    public int getRechargeValue() {
        return rechargeValue;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public RechargeEntity setSinglePrice(BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
        return this;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public RechargeEntity setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}

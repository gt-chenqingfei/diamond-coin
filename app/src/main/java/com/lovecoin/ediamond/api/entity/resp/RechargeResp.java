package com.lovecoin.ediamond.api.entity.resp;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created on 2017/11/22 0022.
 */

public class RechargeResp {
    private List<RechargeEntity> rechargeList;
    private BigDecimal singlePrice;// :1.0,//充值单价,
    private int totalCoin; //10 //币数量(余额)
    private double balance; // :22.5 //ios余额

    public List<RechargeEntity> getRechargeList() {
        return rechargeList;
    }

    public RechargeResp setRechargeList(List<RechargeEntity> rechargeList) {
        this.rechargeList = rechargeList;
        return this;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public RechargeResp setSinglePrice(BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
        return this;
    }

    public int getTotalCoin() {
        return totalCoin;
    }

    public RechargeResp setTotalCoin(int totalCoin) {
        this.totalCoin = totalCoin;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

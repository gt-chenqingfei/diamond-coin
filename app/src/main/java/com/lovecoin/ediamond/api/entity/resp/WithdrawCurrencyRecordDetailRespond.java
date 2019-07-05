package com.lovecoin.ediamond.api.entity.resp;

import java.math.BigDecimal;

/**
 * 获取单个提币记录详情返回数据实体类
 * <p>
 * Created by ZhaiDongyang on 2018/6/12
 */
public class WithdrawCurrencyRecordDetailRespond {

    public WithdrawCurrencyRecordDetailRespond() {
    }

    private long recordId;
    private int status;
    private int coin;
    private BigDecimal amount;
    private BigDecimal poundage;
    private String walletAddress;
    private long txStartTime;
    private long txFinishTime;
    private String txId;
    private String traceUrl;
    private String note;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public long getTxStartTime() {
        return txStartTime;
    }

    public void setTxStartTime(long txStartTime) {
        this.txStartTime = txStartTime;
    }

    public long getTxFinishTime() {
        return txFinishTime;
    }

    public void setTxFinishTime(long txFinishTime) {
        this.txFinishTime = txFinishTime;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTraceUrl() {
        return traceUrl;
    }

    public void setTraceUrl(String traceUrl) {
        this.traceUrl = traceUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

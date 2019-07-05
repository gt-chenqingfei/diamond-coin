package com.lovecoin.ediamond.api.entity.resp;

import java.math.BigDecimal;

/**
 * 提币交易手续费返回数据实体类
 * 
 * Created by ZhaiDongyang on 2018/6/7
 */
public class WithdrawCurrencyTradePoundageRespond {

    private BigDecimal estimatedTxFee;

    public WithdrawCurrencyTradePoundageRespond(BigDecimal estimatedTxFee) {
        this.estimatedTxFee = estimatedTxFee;
    }

    public BigDecimal getEstimatedTxFee() {
        return estimatedTxFee;
    }
}

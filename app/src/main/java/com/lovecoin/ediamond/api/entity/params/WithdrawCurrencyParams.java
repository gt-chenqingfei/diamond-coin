package com.lovecoin.ediamond.api.entity.params;

/**
 * 提币请求参数实体类
 *
 * Created by ZhaiDongyang on 2018/6/4
 */
public class WithdrawCurrencyParams {

    private int coin;
    private String walletAddress;
    private String withdrawPass;
    private String vCode;
    private String note;

    public WithdrawCurrencyParams(int coin, String walletAddress, String withdrawPass, String vCode, String note) {
        this.coin = coin;
        this.walletAddress = walletAddress;
        this.withdrawPass = withdrawPass;
        this.vCode = vCode;
        this.note = note;
    }
}

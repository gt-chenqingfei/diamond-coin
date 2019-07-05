package com.lovecoin.ediamond.api.entity.params;

/**
 * 设置提币密码请求参数实体类
 *
 * Created by ZhaiDongyang on 2018/6/4
 */
public class WithdrawCurrencySetPwdParams {

    private String withdrawPass;
    private String vCode;

    public WithdrawCurrencySetPwdParams(String topupPass, String vCode) {
        this.withdrawPass = topupPass;
        this.vCode = vCode;
    }
}

package com.lovecoin.ediamond.api.entity.params;

/**
 * 验证提币密码请求参数实体类
 *
 * Created by ZhaiDongyang on 2018/6/12
 */
public class WithdrawCurrencyVerifyPassParams {

    private String withdrawPass;

    public WithdrawCurrencyVerifyPassParams(String withdrawPass) {
        this.withdrawPass = withdrawPass;
    }
}

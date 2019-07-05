package com.lovecoin.ediamond.api.entity.params;

/**
 * 提币时获取手机验证码请求实体类
 * 
 * Created by ZhaiDongyang on 2018/6/6
 */
public class WithdrawCurrencyIdentifyingCodeParams {

    private int type;

    public WithdrawCurrencyIdentifyingCodeParams(int type) {
        this.type = type;
    }
}

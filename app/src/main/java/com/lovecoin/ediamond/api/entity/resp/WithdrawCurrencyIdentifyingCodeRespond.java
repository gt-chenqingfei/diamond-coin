package com.lovecoin.ediamond.api.entity.resp;

/**
 * 提币时获取手机验证码返回数据实体类
 * 
 * Created by ZhaiDongyang on 2018/6/7
 */
public class WithdrawCurrencyIdentifyingCodeRespond {

    private String mobile;

    public WithdrawCurrencyIdentifyingCodeRespond(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }
}

package com.lovecoin.ediamond.api.entity.resp;

/**
 * 提币前查看是否设置提币密码返回数据实体类
 * 
 * Created by ZhaiDongyang on 2018/6/7
 */
public class WithdrawCurrencyPwdStatusRespond {

    private int passStatus;

    public WithdrawCurrencyPwdStatusRespond(int passStatus) {
        this.passStatus = passStatus;
    }

    public int getPassStatus() {
        return passStatus;
    }
}

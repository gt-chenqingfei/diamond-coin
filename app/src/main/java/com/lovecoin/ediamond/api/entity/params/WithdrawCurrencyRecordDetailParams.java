package com.lovecoin.ediamond.api.entity.params;

/**
 * 获取单个提币记录详情请求参数实体类
 *
 * Created by ZhaiDongyang on 2018/6/12
 */
public class WithdrawCurrencyRecordDetailParams {

    private long recordId;

    public WithdrawCurrencyRecordDetailParams(long recordId) {
        this.recordId = recordId;
    }
}

package com.lovecoin.ediamond.api.entity.params;

/**
 * Created on 2017/11/26.
 */

public class CreateOrderParams {

    private String payType;//0,//支付类型 （0：paypal,1:ipa，2：googlePay，3：paypal for client）
    private String buyNum;//5, //购买数量 （payType = 0时传递）
    private String payMoney;  //充值金额 (payType = 1 和 payType = 2 时传递)

    public CreateOrderParams(String payMoney) {
        this.payType = "3";
        this.payMoney = payMoney;
    }

}

package com.lovecoin.ediamond.api.entity.params;

/**
 * Created on 2017/11/26.
 */

public class PayValidParams {

    private String payType;//0,//支付类型 （0：paypal,1;//ipa）3,paypal for client
    private String nonce;//nonce,//paypal客户端支付后返回的字符串(payType = 0 时传递)
    private String orderId;//64a2928e2678405e9e193805675d6aaa//本系统支付订单id

    public PayValidParams(String nonce, String orderId) {
        this.payType = "3";
        this.nonce = nonce;
        this.orderId = orderId;
    }

    public PayValidParams(String payType, String nonce, String orderId) {
        this.payType = payType;
        this.nonce = nonce;
        this.orderId = orderId;
    }
}

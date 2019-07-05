package com.lovecoin.ediamond.api.entity;

/**
 * Created on 2017/11/15 0015.
 */

public class BaseResp<T> {

    private boolean success;
    private String code;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public BaseResp setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getCode() {
        return code;
    }

    public BaseResp setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResp setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseResp setData(T data) {
        this.data = data;
        return this;
    }
}

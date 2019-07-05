package com.lovecoin.ediamond.api.entity.params;

import com.lovecoin.ediamond.data.UserHelper;

/**
 * Created on 2017/11/19 0019.
 */

public class CommonRequestParams {
    private String param;
    private String token;

    public CommonRequestParams() {
        this.token = UserHelper.getInstance().getToken();
    }

    public CommonRequestParams(String param) {
        this();
        this.param = param;
    }
}

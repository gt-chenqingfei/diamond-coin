package com.lovecoin.ediamond.api.utils;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.entity.params.CommonRequestParams;
import com.lovecoin.ediamond.api.entity.params.GetBaseInfoRequestParams;
import com.lovecoin.ediamond.data.UserHelper;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created on 2017/11/19 0019.
 */

public class ApiInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        boolean isGetBaseInfo = url.pathSegments().contains("getBaseInfo");
        // 是否使用默认 key 加密
        String key = isGetBaseInfo
                ? Utils.getApp().getString(R.string.default_key)
                : UserHelper.getInstance().getSecretKey();

        RequestBody originBody = request.body();
        // 图片上传提前结束,但还是要解密
        Request newRequest;

        if ((originBody instanceof MultipartBody)) {
            newRequest = request;
        } else {
            String strOldBody = bodyToString(originBody);
            String encodedBody = EncryptUtils.encode(strOldBody, key);

            String newParams;
            Gson gson = new Gson();
            if (isGetBaseInfo) {
                GetBaseInfoRequestParams requestParams = new GetBaseInfoRequestParams(encodedBody);
                newParams = gson.toJson(requestParams);
            } else {
                CommonRequestParams requestParams = new CommonRequestParams(encodedBody);
                newParams = gson.toJson(requestParams);
            }

            //Logger.d("request 加密前 %s", strOldBody);
            //Logger.d("request 加密后 %s", newParams);

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, newParams);

            newRequest = request.newBuilder()
                    .post(body)
                    .build();
        }
        // 开始请求
        Response response = chain.proceed(newRequest);

        if (response.code() != 200) {
            return response;
        }

        String responseBody = response.body().string();
        //Logger.d("response 解密前 %s", responseBody);

        String newBody;

        if (responseBody.startsWith("{")) {
            newBody = responseBody;
            //Logger.d("response 不需要解密");
        } else {        // 不是 json， 需要解密
            newBody = EncryptUtils.decode(responseBody, key);
            //Logger.d("response 解密后 %s", newBody);
        }

        // response.body().string() 只能读一次，需要重新创建
        okhttp3.MediaType responseMediaType = response.body().contentType();
        response = response.newBuilder()
                .body(ResponseBody.create(responseMediaType, newBody))
                .build();
        return response;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}

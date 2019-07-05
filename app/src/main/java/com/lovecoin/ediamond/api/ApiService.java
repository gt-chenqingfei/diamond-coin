package com.lovecoin.ediamond.api;

import com.lovecoin.ediamond.api.entity.BaseResp;
import com.lovecoin.ediamond.api.entity.params.BindMobileParams;
import com.lovecoin.ediamond.api.entity.params.ConsumeGooglePayParams;
import com.lovecoin.ediamond.api.entity.params.CreateOrderParams;
import com.lovecoin.ediamond.api.entity.params.GetValidCodeParams;
import com.lovecoin.ediamond.api.entity.params.IosBuyParams;
import com.lovecoin.ediamond.api.entity.params.LoginByFbParams;
import com.lovecoin.ediamond.api.entity.params.LoginByPhoneParams;
import com.lovecoin.ediamond.api.entity.params.LoginParams;
import com.lovecoin.ediamond.api.entity.params.MyRecordCountParams;
import com.lovecoin.ediamond.api.entity.params.MyRecordsParams;
import com.lovecoin.ediamond.api.entity.params.PayValidParams;
import com.lovecoin.ediamond.api.entity.params.RegisterPushParams;
import com.lovecoin.ediamond.api.entity.params.SendParams;
import com.lovecoin.ediamond.api.entity.params.UpdateProfileParams;
import com.lovecoin.ediamond.api.entity.params.ValidGooglePayParams;
import com.lovecoin.ediamond.api.entity.params.ValidateUserMobileParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyIdentifyingCodeParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyRecordDetailParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencySetPwdParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyVerifyPassParams;
import com.lovecoin.ediamond.api.entity.resp.BaseInfo;
import com.lovecoin.ediamond.api.entity.resp.CheckVersionResp;
import com.lovecoin.ediamond.api.entity.resp.CreateOrderResp;
import com.lovecoin.ediamond.api.entity.resp.LoginResponse;
import com.lovecoin.ediamond.api.entity.resp.LoveRelation;
import com.lovecoin.ediamond.api.entity.resp.LoveRelationProfile;
import com.lovecoin.ediamond.api.entity.resp.MyProfile;
import com.lovecoin.ediamond.api.entity.resp.MyRecordCountResp;
import com.lovecoin.ediamond.api.entity.resp.RechargeResp;
import com.lovecoin.ediamond.api.entity.resp.RecordsResp;
import com.lovecoin.ediamond.api.entity.resp.SendCoinListResp;
import com.lovecoin.ediamond.api.entity.resp.SendResp;
import com.lovecoin.ediamond.api.entity.resp.SkuListResp;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyIdentifyingCodeRespond;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyPwdStatusRespond;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyRecordDetailRespond;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyRecordRespond;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyTradePoundageRespond;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/basic/getBaseInfo")
    Observable<BaseResp<BaseInfo>> getBaseInfo(
            @Body String deviceInfo
    );

    @POST("api/users/login")
    Observable<BaseResp<String>> login(@Body LoginParams loginParams);

    @POST("api/account/loginbyphone")
    Observable<BaseResp<LoginResponse>> loginByPhone(@Body LoginByPhoneParams loginParams);

    @POST("api/account/loginbyfb")
    Observable<BaseResp<LoginResponse>> loginByFb(@Body LoginByFbParams loginParams);

    @POST("api/account/bindmobile")
    Observable<BaseResp<LoginResponse>> bindMobile(@Body BindMobileParams loginParams);

    @POST("api/account/profile")
    Observable<BaseResp<Object>> updateProfile(@Body UpdateProfileParams loginParams);

    @Multipart
    @POST("api/support/uploadfile")
    Observable<BaseResp<String>> uploadFile(@Query("token") String token,
                                            @Part() MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/users/register")
    Observable<BaseResp<BaseInfo>> reg(
            @Field("mobile") String mobile,
            @Field("pwd") String pwd,
            @Field("vCode") String vCode);

    @POST("api/basic/getValidCode")
    Observable<BaseResp<Object>> getValidCode(@Body GetValidCodeParams params);

    @POST("api/users/validateUserMobile")
    Observable<BaseResp<Object>> validateUserMobile(@Body ValidateUserMobileParams params);

    @POST("api/users/getUserProfile")
    Observable<BaseResp<MyProfile>> getUserProfile();

    @POST("api/loveRelation/getLoveRelation")
    Observable<BaseResp<LoveRelation>> getLoveRelation();

    @POST("api/loveRelation/getLoveRelationProfile")
    Observable<BaseResp<LoveRelationProfile>> getLoveRelationProfile();

    @Multipart
    @POST("api/users/updateUserHeadImgUrl")
    Observable<BaseResp<String>> updateUserHeadImgUrl(@Query("token") String token,
                                                      @Part() MultipartBody.Part file);

    @POST("api/loveRelation/cancelLoveRelation")
    Observable<BaseResp<Object>> cancelLoveRelation();

    @POST("api/record/myRecords")
    Observable<BaseResp<RecordsResp>> myRecords(@Body MyRecordsParams myRecordsParams);

    @POST("api/record/myRecordCount")
    Observable<BaseResp<MyRecordCountResp>> myRecordCount(@Body MyRecordCountParams myRecordCountParams);

    @POST("api/recharge/list")
    Observable<BaseResp<RechargeResp>> getRechargeList();

    @POST("api/payOrder/createPayOrder")
    Observable<BaseResp<CreateOrderResp>> createPayOrder(@Body CreateOrderParams params);

    @POST("api/payOrder/payValid")
    Observable<BaseResp<Double>> payValid(@Body PayValidParams params);

    @POST("api/payOrder/getClientToken")
    Observable<BaseResp<String>> getPaypalClientToken();

    @POST("api/basic/sendCoinList")
    Observable<BaseResp<SendCoinListResp>> sendCoinList();

    @POST("api/sendCoin/directSendCoin")
    Observable<BaseResp<SendResp>> directSendCoin(@Body SendParams params);

    @POST("api/push/registerPush")
    Observable<BaseResp<Object>> registerPush(@Body RegisterPushParams params);

    @POST("api/basic/checkVersion")
    Observable<BaseResp<CheckVersionResp>> checkVersion();

    @POST("api/basic/topupListByGooglePay")
    Observable<BaseResp<SkuListResp>> topupListByGooglePay();

    @POST("api/payOrder/iosBuyCoin")
    Observable<BaseResp<Double>> iosBuyCoin(@Body IosBuyParams params);

    @POST("api/payOrder/validGooglePay")
    Observable<BaseResp<Boolean>> validGooglePay(@Body ValidGooglePayParams params);

    @POST("api/payOrder/consumeGooglePay")
    Observable<BaseResp<Double>> consumeGooglePay(@Body ConsumeGooglePayParams params);

    @POST("api/users/logout")
    Observable<BaseResp<Boolean>> logout();

    // 提币相关接口
    @POST("/api/withdraw/getpasssms")// 请求短信验证码
    Observable<BaseResp<WithdrawCurrencyIdentifyingCodeRespond>> getPassSms(@Body WithdrawCurrencyIdentifyingCodeParams params);
    @POST("/api/withdraw/passstatus")// 获取用户是否已设置提币密码
    Observable<BaseResp<WithdrawCurrencyPwdStatusRespond>> passStatus();
    @POST("/api/withdraw/withdrawpass")// 设置提币密码
    Observable<BaseResp<Object>> withdrawPass(@Body WithdrawCurrencySetPwdParams params);
    @POST("/api/withdraw/newtx")// 申请提币
    Observable<BaseResp<Object>> newTx(@Body WithdrawCurrencyParams params);
    @POST("/api/withdraw/history")// 查看提币记录
    Observable<BaseResp<WithdrawCurrencyRecordRespond>> history();
    @POST("/api/withdraw/txfee")// 获取交易手续费
    Observable<BaseResp<WithdrawCurrencyTradePoundageRespond>> txFee();

    @POST("/api/withdraw/verifypass")// 验证提币密码
    Observable<BaseResp<Object>> verifyPass(@Body WithdrawCurrencyVerifyPassParams params);
    @POST("/api/withdraw/tx")// 获取单个提币记录详情
    Observable<BaseResp<WithdrawCurrencyRecordDetailRespond>> tx(@Body WithdrawCurrencyRecordDetailParams params);

}

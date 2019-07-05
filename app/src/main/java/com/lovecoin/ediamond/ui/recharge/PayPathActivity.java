package com.lovecoin.ediamond.ui.recharge;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.CreateOrderParams;
import com.lovecoin.ediamond.api.entity.params.PayValidParams;
import com.lovecoin.ediamond.api.entity.resp.CreateOrderResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.app.App;
import com.lovecoin.ediamond.event.CoinChangeEvent;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.bind.BindActivityStarter;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;
@Deprecated
@MakeActivityStarter
public class PayPathActivity extends BaseActivity implements BraintreeCancelListener, PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    @Arg int num;
    @Arg BigDecimal price;
    @BindView(R.id.toolbar) CustomToolbar toolbar;
    @BindView(R.id.pay_num_tv) TextView payNumTv;
    @BindView(R.id.pay_price_tv) TextView payPriceTv;
    @BindView(R.id.pay_path_paypal_layout) RelativeLayout payPathPaypalLayout;

    @Nullable
    private BraintreeFragment mBraintreeFragment;

    private CreateOrderResp createOrderResp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_path;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this, Color.WHITE);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getString(R.string.activity_pay_path_title));
        payNumTv.setText(String.format("eDiamond X%s", num));
        payPriceTv.setText(String.format("$%s", price));
    }

    @Override
    protected void initData() {
        getPaypalToken();
    }

    private void getPaypalToken() {
        showLoading();
        String token = App.getInstance().getPalpalClientToken();
        if (!StringUtils.isEmpty(token)) {
            initPayPal(token);
            return;
        }

        Api.getInstance()
                .getPaypalClientToken()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onHandleSuccess(String s) {
                        App.getInstance().setPalpalClientToken(s);
                        initPayPal(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }
                });
    }

    private void initPayPal(String token) {
        try {
            mBraintreeFragment = BraintreeFragment
                    .newInstance(this, token);
            // mBraintreeFragment is ready to use!
            mBraintreeFragment.addListener(this);

        } catch (InvalidArgumentException e) {
            //Logger.e(e, "paypal 初始化失败");
        }
        dismissLoading();
    }

    @Override
    protected void onDestroy() {
        if (mBraintreeFragment != null) {
            mBraintreeFragment.removeListener(this);
        }
        super.onDestroy();
    }

    @OnClick(R.id.pay_path_paypal_layout)
    public void onPayPalClick(View view) {
        if (mBraintreeFragment == null) {
            //Logger.e("paypal 初始化失败");
            return;
        }

        // 已创建订单直接调用Paypal，否则创建订单
        if (createOrderResp != null) {
            callPayPal(createOrderResp.getItemTotalPrice().toPlainString());
        } else {
            createOrder(num);
        }
    }

    public void callPayPal(String totalPrice) {
        showLoading();
        PayPalRequest request = new PayPalRequest(totalPrice)
                .currencyCode("USD")
                .intent(PayPalRequest.INTENT_AUTHORIZE);
        PayPal.requestOneTimePayment(mBraintreeFragment, request);
    }

    // 支付成功
    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        // Send nonce to server
        String nonce = paymentMethodNonce.getNonce();
        //Logger.d("success, nonce %s", nonce);
        postNonceToServer(nonce);
        if (paymentMethodNonce instanceof PayPalAccountNonce) {
            PayPalAccountNonce payPalAccountNonce = (PayPalAccountNonce) paymentMethodNonce;

            // Access additional information
            String email = payPalAccountNonce.getEmail();
            String firstName = payPalAccountNonce.getFirstName();
            String lastName = payPalAccountNonce.getLastName();
            String phone = payPalAccountNonce.getPhone();

            // See PostalAddress.java for details
            PostalAddress billingAddress = payPalAccountNonce.getBillingAddress();
            PostalAddress shippingAddress = payPalAccountNonce.getShippingAddress();
        }
    }

    void postNonceToServer(String nonce) {
        if (createOrderResp == null) {
            timeoutExit();
            return;
        }

        PayValidParams params = new PayValidParams(nonce, createOrderResp.getOrderId());
        Api.getInstance()
                .payValid(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Double>() {
                    @Override
                    protected void onHandleSuccess(Double o) {
                        int num;
                        try {
                            num = o.intValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                            num = 0;
                        }
                        dismissLoading();
                        successTips(num);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            timeoutExit();
                        } else {
                            super.onError(e);
                        }
                        dismissLoading();
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoading();
                    }
                });
    }

    @Override
    public void onError(Exception error) {
        dismissLoading();

        if (error instanceof SocketTimeoutException) {
            timeoutTips();
            return;
        }
        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    //Logger.d(expirationMonthError.getMessage());
                }
            }
        }
    }

    @Override
    public void onCancel(int requestCode) {
        dismissLoading();
        //Logger.d("paypal cancel");
    }

    private void createOrder(int num) {
        showLoading();
        CreateOrderParams params = new CreateOrderParams(String.valueOf(num));
        Api.getInstance()
           .createPayOrder(params)
           .compose(RxSchedulers.compose())
           .subscribe(new BaseObserver<CreateOrderResp>() {
               @Override
               protected void onHandleSuccess(CreateOrderResp resp) {
                   createOrderResp = resp;
                   dismissLoading();
                   callPayPal(resp.getItemTotalPrice().toPlainString());
               }

               @Override
               protected void onHandleError(String code, String msg) {
                   if ("02005".equals(code)) { // 绑定手机号
                       ToastUtils.showShort(msg);
                       BindActivityStarter.start(PayPathActivity.this);
                   } else {
                       super.onHandleError(code, msg);
                   }
                   dismissLoading();
               }

               @Override
               public void onError(Throwable e) {
                   super.onError(e);
                   dismissLoading();
               }
           });
    }

    private void successTips(int num) {
        ToastUtils.showShort(R.string.tips_pay_sucess);
        EventBus.getDefault().postSticky(new CoinChangeEvent(num));
        this.finish();
    }

    private void timeoutTips() {
        ToastUtils.showShort(R.string.tips_timeout);
    }

    private void timeoutExit() {
        timeoutTips();
        this.finish();
    }
}

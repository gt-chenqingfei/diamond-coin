package com.lovecoin.ediamond.ui.account;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.ConsumeGooglePayParams;
import com.lovecoin.ediamond.api.entity.params.CreateOrderParams;
import com.lovecoin.ediamond.api.entity.params.PayValidParams;
import com.lovecoin.ediamond.api.entity.params.ValidGooglePayParams;
import com.lovecoin.ediamond.api.entity.resp.CreateOrderResp;
import com.lovecoin.ediamond.api.entity.resp.SkuListResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.app.App;
import com.lovecoin.ediamond.app.Const;
import com.lovecoin.ediamond.billing.BillingManager;
import com.lovecoin.ediamond.event.BalanceChangeEvent;
import com.lovecoin.ediamond.event.BindMobileEvent;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.bind.BindActivityStarter;
import com.lovecoin.ediamond.ui.entry.contact.ContactActivityStarter;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.List;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;

@MakeActivityStarter
public class MyAccountActivity extends BaseActivity implements BraintreeCancelListener, PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    @BindView(R.id.my_account_balance_tv)
    TextView myAccountBalanceTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recharge_money_tv)
    TextView rechargeMoneyTv;
    @BindView(R.id.btn_pay_4_google)
    Button btnPay4Google;
    @BindView(R.id.btn_pay_4_paypal)
    Button btnPay4PayPal;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tips)
    TextView tips;

    private SkuAdapter skuAdapter;
    private BillingDelegate billingDelegate;
    private boolean validPhone = false;
    private boolean isBillingReady = false;
    private boolean hasQuery = false;
    @Nullable
    private BraintreeFragment mBraintreeFragment4PayPal;
    private CreateOrderResp createOrder4PayPal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        tips.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        skuAdapter = new SkuAdapter();
        skuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SkuListResp.Sku item = skuAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                skuAdapter.setSelected(position);
            }
        });
        skuAdapter.setOnSelectedChangedListener(new SkuAdapter.OnSelectedChangedListener() {
            @Override
            public void onChnaged(SkuListResp.Sku sku) {
                rechargeMoneyTv.setText(String.format("$%s", sku.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP)));
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(skuAdapter);

        refreshLayout.setOnRefreshListener(refreshlayout -> getSkuList());

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                == ConnectionResult.SUCCESS) {
            btnPay4Google.setVisibility(View.VISIBLE);
        }
    }

    private void getSkuList() {
        Api.getInstance().topupListByGooglePay()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<SkuListResp>() {

                    @Override
                    protected void onHandleSuccess(SkuListResp resp) {
                        resetData(resp);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void resetData(SkuListResp resp) {
        validPhone = resp.isValidPhone();
        myAccountBalanceTv.setText(String.format("$%s", new BigDecimal(resp.getBalance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        skuAdapter.setNewData(resp.getTopupList());
        if (!validPhone) {
            BindActivityStarter.start(this);
            return;
        }
        queryPurchase();

    }

    @Override
    protected void initData() {
        initBilling4Google();
        refreshLayout.autoRefresh();
    }

    private void initBilling4Google() {
//        append("初始化 billing");
        billingDelegate = new BillingDelegate(this, new BillingManager.BillingUpdatesListener() {
            @Override
            public void onBillingClientSetupFinished() {
                dismissLoading();
                isBillingReady = true;
//                append(" billing 初始化 完成");
//                append("开始查询未处理订单");
                queryPurchase();
            }

            @Override
            public void onConsumeFinished(String token, @BillingClient.BillingResponse int result) {
                if (result == BillingClient.BillingResponse.OK) {
//                    append("消耗成功");
                    consume4Google(token);
                } else {
//                    append("消耗失败");
                    dismissLoading();
                }
            }

            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {
                if (purchases == null || purchases.isEmpty()) {
                    dismissLoading();
//                    append("没订单");
                    return;
                }
                showLoading();
//                append("有订单更新");
                for (Purchase purchase : purchases) {
                    handlePurchase4Google(purchase);
                }
            }

            @Override
            public void onPurchaseFlowStatus(int result) {
//                append("onPurchaseFlowStatus:" + result );
                if (result == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                    dismissLoading();
                    ToastUtils.showShort("Failure to purchase since item is already owned");
                }

                // 启动购买非成功都关闭Loading
                if (result != BillingClient.BillingResponse.OK) {
                    dismissLoading();
                }
            }

            @Override
            public void onPurchaseHistoryResponse(int code, List<Purchase> list) {

            }
        });
        getLifecycle().addObserver(billingDelegate);
    }

    private void handlePurchase4Google(Purchase purchase) {
//        append("校验订单");
        ValidGooglePayParams params = new ValidGooglePayParams(purchase.getOriginalJson(), purchase.getSignature());
        Api.getInstance()
                .validGooglePay(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Boolean>() {

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
//                   append("校验失败");
                        if (e instanceof SocketTimeoutException) {
                            ToastUtils.showShort(R.string.tips_timeout);
                        } else {
                            super.onError(e);
                        }
                    }

                    @Override
                    protected void onHandleSuccess(Boolean aBoolean) {
                        dismissLoading();
//                   append("校验成功");
                        if (aBoolean) {
                            // 校验完消耗
//                       append("开始消耗");
                            billingDelegate.consume(purchase.getPurchaseToken());
                        }
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }
                });
    }

    private void consume4Google(String token) {
        showLoading();
        ConsumeGooglePayParams params = new ConsumeGooglePayParams(token);
        Api.getInstance()
                .consumeGooglePay(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Double>() {

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        if (e instanceof SocketTimeoutException) {
                            ToastUtils.showShort(R.string.tips_timeout);
                        } else {
                            super.onError(e);
                        }
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        dismissLoading();
                    }

                    @Override
                    protected void onHandleSuccess(Double aBoolean) {
                        dismissLoading();
                        ToastUtils.showShort(R.string.tips_pay_sucess);
                        myAccountBalanceTv.setText(String.format("$%s", new BigDecimal(aBoolean).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        if (mBraintreeFragment4PayPal != null) {
            mBraintreeFragment4PayPal.removeListener(this);
        }
        super.onDestroy();
    }

    @OnClick(R.id.tips)
    public void onContactUsClick() {
        // TODO 邮件发送暂时调用系统的
        // ContactActivityStarter.start(this);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Const.EMAIL_ADDRESS));
        startActivity(Intent.createChooser(intent, "Select email application."));
    }

    @OnClick(R.id.btn_pay_4_google)
    public void onPay4GoogleClick() {
        SkuListResp.Sku sku = skuAdapter.getSelected();
        if (sku == null
                || billingDelegate == null) {
            return;
        }

        if (!validPhone) {
            BindActivityStarter.start(this);
            return;
        }

        billingDelegate.buy(sku.getIapId());
    }

    /**
     * 用户点击 PayPal 时
     *
     * @param view
     */
    @OnClick(R.id.btn_pay_4_paypal)
    public void onPayPalClick(View view) {
        getPayPalToken();
    }

    private void getPayPalToken() {
        showLoading();
        Api.getInstance()
                .getPaypalClientToken()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onHandleSuccess(String s) {
                        initPayPal(s);
                        consume4PayPal();
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
            mBraintreeFragment4PayPal = BraintreeFragment
                    .newInstance(this, token);
            // mBraintreeFragment4PayPal is ready to use!
            mBraintreeFragment4PayPal.addListener(this);

        } catch (InvalidArgumentException e) {
            //Logger.e(e, "paypal 初始化失败");
        }
    }

    private void consume4PayPal() {
        SkuListResp.Sku sku = skuAdapter.getSelected();
        if (sku == null
                || billingDelegate == null) {
            return;
        }

        if (!validPhone) {
            BindActivityStarter.start(this);
            return;
        }

        if (mBraintreeFragment4PayPal == null) {
            //Logger.e("paypal 初始化失败");
            return;
        }
        createOrder4PayPal(sku.getMoney());
    }

    private void createOrder4PayPal(BigDecimal money) {
        CreateOrderParams params = new CreateOrderParams(String.valueOf(money));
        Api.getInstance()
                .createPayOrder(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<CreateOrderResp>() {
                    @Override
                    protected void onHandleSuccess(CreateOrderResp resp) {
                        createOrder4PayPal = resp;
                        callPayPal(resp.getPayMoney());
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        dismissLoading();
                        if ("02005".equals(code)) { // 绑定手机号
                            ToastUtils.showShort(msg);
                            BindActivityStarter.start(MyAccountActivity.this);
                        } else {
                            super.onHandleError(code, msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }

    private void callPayPal(String totalPrice) {
        PayPalRequest request = new PayPalRequest(totalPrice)
                .currencyCode("USD")
                .intent(PayPalRequest.INTENT_AUTHORIZE);
        PayPal.requestOneTimePayment(mBraintreeFragment4PayPal, request);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindMobileEvent e) {
        BindMobileEvent event = EventBus.getDefault().removeStickyEvent(BindMobileEvent.class);
        if (event == null) {
            return;
        }

        validPhone = true;
        queryPurchase();
    }

    public void queryPurchase() {
        if (billingDelegate == null
                || !isBillingReady
                || !validPhone
                || hasQuery) {

            return;
        }

        hasQuery = true;
        billingDelegate.query();
    }


    // PayPal 支付取消
    @Override
    public void onCancel(int requestCode) {
        dismissLoading();
        //Logger.d("paypal cancel");
    }

    // PayPal 支付成功
    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        // Send nonce to server
        String nonce = paymentMethodNonce.getNonce();
        //Logger.d("success, nonce %s", nonce);
        postNonceToServer(nonce);
    }

    /**
     * PayPal支付失败
     */
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

    void postNonceToServer(String nonce) {
        if (createOrder4PayPal == null) {
            timeoutExit();
            return;
        }
        PayValidParams params = new PayValidParams(nonce, createOrder4PayPal.getOrderId());
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

    private void successTips(int num) {
        ToastUtils.showShort(R.string.tips_pay_sucess);
        EventBus.getDefault().postSticky(new BalanceChangeEvent(num));
        this.finish();
    }

    private void timeoutExit() {
        timeoutTips();
        this.finish();
    }

    private void timeoutTips() {
        ToastUtils.showShort(R.string.tips_timeout);
    }

}

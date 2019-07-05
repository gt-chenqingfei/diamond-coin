package com.lovecoin.ediamond.ui;

import android.view.View;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.billing.BillingConstants;
import com.lovecoin.ediamond.billing.BillingManager;
import com.lovecoin.ediamond.ui.base.BaseActivity;

import java.util.List;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;

/**
 * Created on 2018/1/1.
 */
@MakeActivityStarter
public class BillingActivity extends BaseActivity {
    private BillingManager billingManager;
    private List<SkuDetails> skuDetailsList;

    @BindView(R.id.log) TextView log;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_billing;
    }

    @Override
    protected void onBeforeSetContentLayout() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingManager != null) {
            billingManager.destroy();
        }
    }

    private void append(String s) {
        log.append(s);
        log.append("\r\n");
    }

    public void getSkus(View view) {
        if (billingManager == null) {
            append("请先连接google play");
            return;
        }
        append("获取sku");
        billingManager.querySkuDetailsAsync(BillingClient.SkuType.INAPP,
                BillingConstants.getSkuList(BillingClient.SkuType.INAPP),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        BillingActivity.this.skuDetailsList = skuDetailsList;
                        append("获取sku成功：size" + skuDetailsList.size());
                    }
                });
    }

    public void init(View view) {
        append("连接googleplay");
        billingManager = new BillingManager(this, new BillingManager.BillingUpdatesListener() {
            @Override
            public void onBillingClientSetupFinished() {
                append("连接成功");
            }

            @Override
            public void onConsumeFinished(String token, @BillingClient.BillingResponse int result) {
                if (result == BillingClient.BillingResponse.OK) {
                    append("消耗成功:" + token);
                    append("这时候告诉服务器成功，需要哪些信息？");

                } else {
                    append("消耗失败:" + token);
                }
            }

            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {
                if (purchases == null || purchases.isEmpty()) {
                    return;
                }
                append("onPurchasesUpdated");
                append("准备消耗");
                for (Purchase purchase : purchases) {
                    append("消耗" + purchase.getPurchaseToken());
                    billingManager.consumeAsync(purchase.getPurchaseToken());
                }
            }

            @Override
            public void onPurchaseFlowStatus(@BillingClient.BillingResponse int result) {
                switch (result) {
                    case BillingClient.BillingResponse.ITEM_ALREADY_OWNED:
                        append("已购买，未消耗");
                        break;
                }

            }

            @Override
            public void onPurchaseHistoryResponse(int code, List<Purchase> list) {
                if (code == BillingClient.BillingResponse.OK) {
                    if (list == null || list.isEmpty()) {
                        append("查询历史为空");
                        return;
                    }
                    append("查询历史成功");

                    for (Purchase purchase : list) {
                        append(purchase.getOrderId() + ":" + purchase.getSku() + ":" + purchase.getPurchaseToken() + ":");
                    }

                } else {
                    append("查询历史失败");
                }
            }
        });
    }

    public void payFirst(View view) {
        if (billingManager == null) {
            append("请先连接google play");
            return;
        }
        if (skuDetailsList == null || skuDetailsList.isEmpty()) {
            append("sku为空");
            return;
        }

        SkuDetails details = skuDetailsList.get(0);
        append("开始支付sku0:" + details.getSku());
        billingManager.initiatePurchaseFlow(details.getSku(), details.getType());
    }

    public void payHistory(View view) {
        if (billingManager == null) {
            append("请先连接google play");
            return;
        }
        billingManager.queryPurchaseHistory();
    }
}

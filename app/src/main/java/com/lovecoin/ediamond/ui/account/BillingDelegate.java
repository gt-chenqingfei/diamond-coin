package com.lovecoin.ediamond.ui.account;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.android.billingclient.api.BillingClient;
import com.lovecoin.ediamond.billing.BillingManager;

/**
 * Created on 2018/1/7.
 */

public class BillingDelegate implements LifecycleObserver {

    private BillingManager billingManager;

    public BillingDelegate(MyAccountActivity activity, BillingManager.BillingUpdatesListener listener) {
        billingManager = new BillingManager(activity, listener);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        if (billingManager != null) {
            billingManager.destroy();
        }
    }

    public void buy(String skuId) {
        billingManager.initiatePurchaseFlow(skuId, BillingClient.SkuType.INAPP);
    }

    public void consume(String token) {
        billingManager.consumeAsync(token);
    }

    public void query() {
        billingManager.queryPurchases();
    }
}

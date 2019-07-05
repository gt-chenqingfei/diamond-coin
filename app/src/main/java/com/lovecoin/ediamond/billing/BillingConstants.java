package com.lovecoin.ediamond.billing;

import com.android.billingclient.api.BillingClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class BillingConstants {
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    public static final String SKU_1 = "diamond_1";


    // SKU for our subscription (infinite gas)
    public static final String SKU_GOLD_MONTHLY = "gold_monthly";
    public static final String SKU_GOLD_YEARLY = "gold_yearly";

    private static final String[] IN_APP_SKUS = {SKU_1};
    private static final String[] SUBSCRIPTIONS_SKUS = {SKU_GOLD_MONTHLY, SKU_GOLD_YEARLY};

    private BillingConstants(){}

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    public static final List<String> getSkuList(@BillingClient.SkuType String billingType) {
        return (Objects.equals(billingType, BillingClient.SkuType.INAPP)) ? Arrays.asList(IN_APP_SKUS)
                : Arrays.asList(SUBSCRIPTIONS_SKUS);
    }
}
package com.lovecoin.ediamond.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author qingfei.chen
 * @since 2018/5/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class PhoneNumberUtils {
    public static boolean validPhoneNumber(String phoneNumber, int countryCode) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            String regionCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode);
            final Phonenumber.PhoneNumber number =
                    phoneNumberUtil.parse(phoneNumber, regionCode);

            return phoneNumberUtil.isValidNumber(number);

        } catch (NumberParseException e) {
            return false;
        }
    }

    /**
     * 获取国家代码
     *
     * @return
     */
    public static int getCountryCode(Context context) {
        int countryCode = 86;
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String countryIso = manager.getNetworkCountryIso();
            if (TextUtils.isEmpty(countryIso)) {
                countryIso = manager.getSimCountryIso();
            }
            if (!TextUtils.isEmpty(countryIso)) {
                countryIso = countryIso.toUpperCase();
                Map<String, Integer> countryMap = getCountyCodeMap();
                if (countryMap != null && countryMap.containsKey(countryIso)) {
                    countryCode = countryMap.get(countryIso);
                }
            }
        } catch (Exception e) {

        }

        return countryCode;
    }

    public static Map<String, Integer> getCountyCodeMap() {
        Set<String> regions = PhoneNumberUtil.getInstance().getSupportedRegions();
        Map<String, Integer> countryMap = new HashMap<String, Integer>();
        for (String region : regions) {
            countryMap.put(region.toUpperCase(), PhoneNumberUtil.getInstance().getCountryCodeForRegion(region));
        }
        return countryMap;
    }
}

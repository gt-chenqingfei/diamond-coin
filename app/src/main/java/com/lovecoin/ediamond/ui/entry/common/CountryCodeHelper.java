package com.lovecoin.ediamond.ui.entry.common;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.utils.SpinnerHeightUtils;

/**
 * Created on 2017/10/27 0027.
 */

public class CountryCodeHelper {

    public static void initCountryCodeSpinner(Context context, Spinner countryCodeSpinner) {
        String[] mItems = context.getResources().getStringArray(R.array.country_code_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.reg_item_country_code, mItems);
        adapter.setDropDownViewResource(R.layout.reg_item_country_code);
        countryCodeSpinner.setAdapter(adapter);
        SpinnerHeightUtils.setPopupHeightHeight(countryCodeSpinner, 500);
        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Logger.d(mItems[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}

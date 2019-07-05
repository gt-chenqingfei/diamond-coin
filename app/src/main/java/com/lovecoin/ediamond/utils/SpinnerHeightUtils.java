package com.lovecoin.ediamond.utils;

import android.widget.Spinner;

import java.lang.reflect.Field;

/**
 * 设置 Spinner 下拉高度
 */
public class SpinnerHeightUtils {
    public static void setPopupHeightHeight(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(height);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }
}

package com.lovecoin.ediamond.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lovecoin.ediamond.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by k on 2018/3/6.
 */
public class DateTimeUtils {
    private static String[] monthArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static List<String> years;
    private static List<String> months;

    static {
        Calendar selectedDate = Calendar.getInstance();
        int currentYear = selectedDate.get(Calendar.YEAR);
        years = new ArrayList<>();
        for (int i = 2017; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        months = Arrays.asList(monthArray);
    }

    public static void showPicker(Context context, Callback callback) {
        int color = ContextCompat.getColor(context, R.color.app_3e4f6c);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String year = years.get(options1);
                String month = String.valueOf(option2 + 1);
                callback.onSelected(year, month, getMothAndYear(year, option2));

            }
        })
                .setCancelText(context.getString(R.string.cancel))//取消按钮文字
                .setSubmitText(context.getString(R.string.complete))//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setSubmitColor(color)//确定按钮文字颜色
                .setCancelColor(color)//取消按钮文字颜色
                .setTitleBgColor(0xFFF6f6f8)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setLinkage(false)//设置是否联动，默认true
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                //                .isDialog(true)//是否显示为对话框样式
                .build();

        pvOptions.setNPicker(years, months, null);//添加数据源
        pvOptions.show();
    }

    // month 0-11
    public static String getYearAndMonth(String year, int month) {
        return year + " " + months.get(month);
    }

    public static String getMothAndYear(String year, int month) {
        return months.get(month) + " " + year;
    }

    public interface Callback {
        void onSelected(String year, String month, String monthAndYear);
    }

}

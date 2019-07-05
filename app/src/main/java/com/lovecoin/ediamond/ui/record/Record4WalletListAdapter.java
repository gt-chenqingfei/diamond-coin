package com.lovecoin.ediamond.ui.record;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lovecoin.ediamond.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 2017/11/9 0009.
 */

public class Record4WalletListAdapter extends BaseMultiItemQuickAdapter<Record, BaseViewHolder> {

    public Record4WalletListAdapter() {
        super(null);
        addItemType(Record.TYPE_DATE, R.layout.item_record_list_type_date);
        addItemType(Record.TYPE_0, R.layout.item_record_list_type_1_line);
        addItemType(Record.TYPE_EMPTY, R.layout.record_empty);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {
        int position = helper.getAdapterPosition();

        int type = helper.getItemViewType();
        switch (type) {
            case Record.TYPE_DATE:
                helper.setText(R.id.record_date_tv, item.date);
                helper.setVisible(R.id.record_calender_image, position == 0);
                helper.addOnClickListener(R.id.record_calender_image);
                break;


            case Record.TYPE_0:
                helper.setText(R.id.record_time_tv, item.time);
                helper.setText(R.id.record_value_tv, "+" + item.coin);
                break;

        }
    }

    private boolean isSameYearMonthToPre(Calendar current, Calendar pre) {
        return pre != null
                && current.get(Calendar.YEAR) == pre.get(Calendar.YEAR)
                && current.get(Calendar.MONTH) == pre.get(Calendar.MONTH);
    }

    private String format(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}

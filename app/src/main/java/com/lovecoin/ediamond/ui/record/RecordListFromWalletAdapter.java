package com.lovecoin.ediamond.ui.record;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyRecordRespond;
import com.lovecoin.ediamond.app.Const;
import com.lovecoin.ediamond.utils.TimeFormatUtils;

import java.util.List;

public class RecordListFromWalletAdapter extends BaseMultiItemQuickAdapter<WithdrawCurrencyRecordRespond, BaseViewHolder> {

    public RecordListFromWalletAdapter(List<WithdrawCurrencyRecordRespond> list) {
        super(list);
        addItemType(WithdrawCurrencyRecordRespond.TYPE_ITEM_DATE, R.layout.item_record_list_type_date);
        addItemType(WithdrawCurrencyRecordRespond.TYPE_ITEM_LAYOUT, R.layout.item_record_from_wallet_layout_item);
        addItemType(WithdrawCurrencyRecordRespond.TYPE_EMPTY, R.layout.record_empty);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithdrawCurrencyRecordRespond withdrawCurrencyRecordRespond) {
        int itemType = withdrawCurrencyRecordRespond.getItemType();
        switch (itemType) {
            case WithdrawCurrencyRecordRespond.TYPE_ITEM_DATE:
                helper.setText(R.id.record_date_tv, withdrawCurrencyRecordRespond.getTradeYearMonth().getYearMonth());
                break;
            case WithdrawCurrencyRecordRespond.TYPE_ITEM_LAYOUT:
                helper.setText(R.id.tv_record_coin, withdrawCurrencyRecordRespond.getTrade().getCoin() + Const.TRADE_UNIT);
                helper.setText(R.id.tv_record_address, "To：" + withdrawCurrencyRecordRespond.getTrade().getWalletAddress());

                if (withdrawCurrencyRecordRespond.getTrade().getStatus() == Const.TRADE_STATUS_PENDING) {// 0 - pending（提现中）
                    helper.setImageResource(R.id.iv_item_record, R.drawable.record_from_wallet_status_pending);
                    helper.setText(R.id.tv_record_status, Const.TRADE_STATUS_PENDING_CONTENT);
                } else if (withdrawCurrencyRecordRespond.getTrade().getStatus() == Const.TRADE_STATUS_FINISHED) {//  1 - finished（提现完成）
                    helper.setImageResource(R.id.iv_item_record, R.drawable.record_from_wallet_status_finished);
                    String finishTime = TimeFormatUtils.millis2String(withdrawCurrencyRecordRespond.getTrade().getTxFinishTime());
                    helper.setText(R.id.tv_record_status, finishTime);
                } else if (withdrawCurrencyRecordRespond.getTrade().getStatus() == Const.TRADE_STATUS_FAILED) {// -1 - failed（提现失败）
                    helper.setImageResource(R.id.iv_item_record, R.drawable.record_from_wallet_status_failed);
                    helper.setText(R.id.tv_record_status, Const.TRADE_STATUS_FAILED_CONTENT);
                }
                break;
        }
    }
}

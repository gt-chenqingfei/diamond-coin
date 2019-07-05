package com.lovecoin.ediamond.ui.recharge;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.entity.resp.RechargeEntity;

/**
 * Created on 2017/11/22 0022.
 */

public class RechargeAdapter extends BaseQuickAdapter<RechargeEntity, BaseViewHolder> {
    public RechargeAdapter() {
        super(R.layout.item_recharge);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeEntity item) {
        helper.setText(R.id.recharge_num_tv, String.valueOf(item.getRechargeValue()));
        helper.setText(R.id.recharge_total_price_tv, String.format("$%s", item.getTotalPrice()));
    }
}

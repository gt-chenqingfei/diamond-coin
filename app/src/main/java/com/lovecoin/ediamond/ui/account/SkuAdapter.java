package com.lovecoin.ediamond.ui.account;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.entity.resp.SkuListResp;

import io.reactivex.annotations.NonNull;

/**
 * Created on 2018/1/6.
 */

public class SkuAdapter extends BaseQuickAdapter<SkuListResp.Sku, BaseViewHolder> {

    private int selected = -1;
    private boolean isFirst = true;
    private OnSelectedChangedListener onSelectedChangedListener;

    public void setOnSelectedChangedListener(OnSelectedChangedListener onSelectedChangedListener) {
        this.onSelectedChangedListener = onSelectedChangedListener;
    }

    public SkuAdapter() {
        super(R.layout.item_my_account_sku);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuListResp.Sku item) {
        CheckBox skuCb = helper.getView(R.id.sku_title_tv);
        skuCb.setOnCheckedChangeListener(null);
        skuCb.setText(item.getTitle());

        if (isFirst && helper.getAdapterPosition() == 0) {
            isFirst = false;
            selected = 0;
            notifySelectedChange();
        }

        skuCb.setChecked(helper.getAdapterPosition() == selected);
    }

    public void setSelected(int position) {
        this.selected = position;
        notifyDataSetChanged();
        notifySelectedChange();
    }

    private void notifySelectedChange() {
        if (onSelectedChangedListener != null) {
            onSelectedChangedListener.onChnaged(getSelected());
        }
    }

    @NonNull
    public SkuListResp.Sku getSelected() {
        if (selected == -1) {
            return null;
        }
        return getItem(selected);
    }

    public interface OnSelectedChangedListener {
        void onChnaged(SkuListResp.Sku sku);
    }

}

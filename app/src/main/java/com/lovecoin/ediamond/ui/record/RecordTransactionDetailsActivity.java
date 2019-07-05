package com.lovecoin.ediamond.ui.record;


import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyRecordDetailParams;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyRecordDetailRespond;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.app.Const;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.utils.TimeFormatUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * 提币记录中每个条目的交易细节
 * <p>
 * Created by ZhaiDongyang on 2018/6/6
 */
public class RecordTransactionDetailsActivity extends BaseActivity {

    private long recordId;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_record_transation_details_status_pic)
    ImageView tradeStatusImageView;
    @BindView(R.id.tv_record_transation_details_ediamond_count)
    TextView tradeEdiamondCountTextView;

    @BindView(R.id.tv_record_transation_details_amount)
    TextView tradeAmountTextView;
    @BindView(R.id.tv_record_transation_details_estimated)
    TextView tradeEstimatedTextView;
    @BindView(R.id.tv_record_transation_details_recipient)
    TextView tradeRecipientTextView;
    @BindView(R.id.tv_record_transation_details_timestamp)
    TextView tradeTimestampTextView;
    @BindView(R.id.tv_record_transation_details_start_timestamp)
    TextView tradeStartTimestampTextView;
    @BindView(R.id.tv_record_transation_details_state)
    TextView tradeStatusTextView;
    @BindView(R.id.tv_record_transation_details_transaction)
    TextView tradeTransactionTextView;
    @BindView(R.id.tv_record_transation_details_note)
    TextView tradeNoteTextView;

    @BindView(R.id.ll_record_transation_details_link)
    LinearLayout tradeLinkLinearLayout;
    @BindView(R.id.tv_record_transation_details_link)
    TextView tradeLinkTextView;
    @BindView(R.id.sv_detail)
    ScrollView detailSv;

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activty_record_transaction_details;
    }

    @Override
    protected void initView() {
        recordId = getIntent().getExtras().getLong("recordId");
        refreshLayout.setOnRefreshListener(refreshlayout -> getRecordDetail(recordId));
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initData() {
    }

    private void getRecordDetail(long recordId) {
        Api.getInstance()
                .tx(new WithdrawCurrencyRecordDetailParams(recordId))
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<WithdrawCurrencyRecordDetailRespond>() {
                    @Override
                    protected void onHandleSuccess(WithdrawCurrencyRecordDetailRespond respond) {
                        refreshLayout.finishRefresh();
                        // TODO 处理数据
                        setRespondData(respond);
                        detailSv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void setRespondData(WithdrawCurrencyRecordDetailRespond respond) {
        tradeEdiamondCountTextView.setText(respond.getCoin() + Const.TRADE_UNIT);
        tradeAmountTextView.setText(respond.getAmount().toString() + Const.TRADE_UNIT);
        tradeEstimatedTextView.setText(respond.getPoundage() + Const.TRADE_UNIT);
        tradeRecipientTextView.setText(respond.getWalletAddress());
        tradeStartTimestampTextView.setText(TimeFormatUtils.millis2StringYear(respond.getTxStartTime()));
        tradeTimestampTextView.setText(TimeFormatUtils.millis2StringYear(respond.getTxFinishTime()));
        tradeTransactionTextView.setText(respond.getTxId());
        tradeNoteTextView.setText(respond.getNote());

        if (respond.getStatus() == Const.TRADE_STATUS_PENDING) {
            tradeStatusImageView.setImageResource(R.drawable.record_from_wallet_status_pending);
            tradeStatusTextView.setText(Const.TRADE_STATUS_PENDING_CONTENT);
        }
        if (respond.getStatus() == Const.TRADE_STATUS_FINISHED) {
            tradeStatusImageView.setImageResource(R.drawable.record_from_wallet_status_finished);
            tradeStatusTextView.setText(Const.TRADE_STATUS_FINISHED_CONTENT);
            tradeLinkLinearLayout.setVisibility(View.VISIBLE);
            tradeLinkTextView.setAutoLinkMask(Linkify.ALL);
            tradeLinkTextView.setText(respond.getTraceUrl());

            tradeTimestampTextView.setText(TimeFormatUtils.millis2StringYear(respond.getTxStartTime()));
        }
        if (respond.getStatus() == Const.TRADE_STATUS_FAILED) {
            tradeStatusImageView.setImageResource(R.drawable.record_from_wallet_status_failed);
            tradeStatusTextView.setText(Const.TRADE_STATUS_FAILED_CONTENT);
        }
    }
}

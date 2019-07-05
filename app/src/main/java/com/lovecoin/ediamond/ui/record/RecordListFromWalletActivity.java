package com.lovecoin.ediamond.ui.record;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyRecordRespond;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;

/**
 * 提币记录页面
 * <p>
 * Created by ZhaiDongyang on 2018/6/5
 */
@MakeActivityStarter
public class RecordListFromWalletActivity extends BaseActivity {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecordListFromWalletAdapter recordListFromWalletAdapter;
    private final String DATE_FORMAT_YEAR_MONTH = "yyyyMM MMMM";
    private WithdrawCurrencyRecordRespond.TradeYearMonth tradeYearMonth;
    private WithdrawCurrencyRecordRespond.Trade trade;
    private WithdrawCurrencyRecordRespond recordRespond;
    private List<WithdrawCurrencyRecordRespond> withdrawCurrencyRecordRespondList;
    private Set<String> treeSetYearMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_list_from_wallet;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        smartRefreshLayout.setOnRefreshListener(smart_refresh_layout -> getWalletRecord());
        withdrawCurrencyRecordRespondList = new ArrayList<>();
        intRecyclerView();
    }

    @Override
    protected void initData() {
        smartRefreshLayout.autoRefresh();
    }

    private void intRecyclerView() {
        recordListFromWalletAdapter = new RecordListFromWalletAdapter(withdrawCurrencyRecordRespondList);
        recordListFromWalletAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.record_bottom_layout) {
                    Intent intent = new Intent(RecordListFromWalletActivity.this, RecordTransactionDetailsActivity.class);
                    intent.putExtra("recordId", withdrawCurrencyRecordRespondList.get(position).getTrade().getRecordId());
                    startActivity(intent);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(RecordListFromWalletActivity.this));
        recyclerView.setAdapter(recordListFromWalletAdapter);
    }

    private void getWalletRecord() {
        Api.getInstance().history()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<WithdrawCurrencyRecordRespond>() {
                    @Override
                    protected void onHandleSuccess(WithdrawCurrencyRecordRespond withdrawCurrencyRecordRespond) {
                        disposeRespondData(withdrawCurrencyRecordRespond);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }

    /**
     * 处理返回的数据，并显示两个不同的 item 条目
     *
     * @param respondData
     */
    private void disposeRespondData(WithdrawCurrencyRecordRespond respondData) {
        releaseRecycler();
        withdrawCurrencyRecordRespondList = new ArrayList<>();

        DateFormat format = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH, Locale.ENGLISH);// 201806
        treeSetYearMonth = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        List<WithdrawCurrencyRecordRespond.Trade> tradeList = respondData.getList();

        if (tradeList.size() == 0) {
            recordRespond = new WithdrawCurrencyRecordRespond(WithdrawCurrencyRecordRespond.TYPE_EMPTY);
            withdrawCurrencyRecordRespondList.add(recordRespond);
        }

        for (int i = 0; i < tradeList.size(); i++) {
            String yearMonth = TimeUtils.millis2String(tradeList.get(i).getTxFinishTime(), format);
            treeSetYearMonth.add(yearMonth);
        }

        for (String ym : treeSetYearMonth) {
            // MM yyyy
            tradeYearMonth = new WithdrawCurrencyRecordRespond.TradeYearMonth(ym.substring(6, ym.length()) + " " + ym.substring(0, 4));// TODO
            recordRespond = new WithdrawCurrencyRecordRespond(WithdrawCurrencyRecordRespond.TYPE_ITEM_DATE);
            recordRespond.setTradeYearMonth(tradeYearMonth);
            withdrawCurrencyRecordRespondList.add(recordRespond);

            for (int i = 0; i < tradeList.size(); i++) {
                String yearMonth = TimeUtils.millis2String(tradeList.get(i).getTxFinishTime(), format);
                if (ym.equals(yearMonth)) {
                    trade = tradeList.get(i);
                    recordRespond = null;
                    recordRespond = new WithdrawCurrencyRecordRespond(WithdrawCurrencyRecordRespond.TYPE_ITEM_LAYOUT);
                    recordRespond.setTrade(trade);
                    withdrawCurrencyRecordRespondList.add(recordRespond);
                }
            }
        }
        recordListFromWalletAdapter.setNewData(withdrawCurrencyRecordRespondList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecycler();
    }

    private void releaseRecycler() {
        tradeYearMonth = null;
        trade = null;
        recordRespond = null;
        if (withdrawCurrencyRecordRespondList != null) {
            withdrawCurrencyRecordRespondList.clear();
            withdrawCurrencyRecordRespondList = null;
        }
        if (treeSetYearMonth != null) {
            treeSetYearMonth.clear();
            treeSetYearMonth = null;
        }
    }
}

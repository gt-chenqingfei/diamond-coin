package com.lovecoin.ediamond.ui.record;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.utils.DateTimeUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;

/**
 * 我的记录
 */
@MakeActivityStarter
public class RecordList4WalletActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Record4WalletListAdapter adapter;
    private int page = 1;

    @Arg
    int type;

    private String year = "";
    private String month = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_list_4_wallet;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        initRefreshLayout();
        initRecyclerView();
    }

    @Override
    protected void initData() {
        getWalletRecord(1);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_3e4f6c));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {

        adapter = new Record4WalletListAdapter();
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.record_calender_image) {
                    DateTimeUtils.showPicker(RecordList4WalletActivity.this, new DateTimeUtils.Callback() {
                        @Override
                        public void onSelected(String year, String month, String yearAndMonth) {
                            RecordList4WalletActivity.this.year = year;
                            RecordList4WalletActivity.this.month = month;
                            page = 1;
                            onRefresh();
                        }
                    });
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(RecordList4WalletActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void onRequestFinish() {
        swipeRefreshLayout.setRefreshing(false);
        adapter.loadMoreComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getWalletRecord(page + 1);
    }

    @Override
    public void onRefresh() {
        getWalletRecord(1);
    }

    private void getWalletRecord(int page) {

    }
}

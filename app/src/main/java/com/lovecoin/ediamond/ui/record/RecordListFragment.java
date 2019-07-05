package com.lovecoin.ediamond.ui.record;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.MyRecordsParams;
import com.lovecoin.ediamond.api.entity.resp.RecordsResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.ui.base.BaseFragment;
import com.lovecoin.ediamond.utils.DateTimeUtils;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import activitystarter.Arg;
import butterknife.BindView;

/**
 * Created on 2017/11/9 0009.
 */

public class RecordListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    private RecordListAdapter adapter;
    private int page = 1;

    @Arg int type;

    private String year = "";
    private String month = "";

    @Override
    protected void onBeforeSetContentLayout() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_record_list;
    }

    @Override
    protected void initView() {
        initRefreshLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {

        adapter = new RecordListAdapter();
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.record_calender_image) {
                    DateTimeUtils.showPicker(getActivity(), new DateTimeUtils.Callback() {
                        @Override
                        public void onSelected(String year, String month, String yearAndMonth) {
                            RecordListFragment.this.year = year;
                            RecordListFragment.this.month = month;
                            page = 1;
                            onRefresh();
                        }
                    });
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.app_3e4f6c));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        getMyRecords(1);
    }

    @Override
    public void onRefresh() {
        getMyRecords(1);
    }

    private void getMyRecords(int page) {
        MyRecordsParams params = new MyRecordsParams(String.valueOf(type), String.valueOf(page), year, month);
        Api.getInstance()
           .myRecords(params)
           .compose(RxSchedulers.compose())
           .subscribe(new BaseObserver<RecordsResp>() {
               @Override
               protected void onHandleSuccess(RecordsResp resp) {
                   RecordListFragment.this.page = page;
                   List<Record> records = resp.transform();
                   if (records.isEmpty()) {

                       String date;

                       if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month)) {
                           Calendar cal = Calendar.getInstance();
                           int y = cal.get(Calendar.YEAR);
                           int m = cal.get(Calendar.MONTH) + 1;
                           date = String.format("%s-%02d",y,m);
                       } else {
                           date = String.format("%s-%02d",year, Integer.valueOf(month));
                       }

                       Record record = new Record();
                       record.date = date;
                       record.type = Record.TYPE_DATE;
                       records.add(record);
                   }

                   if (resp.isFirstpage()) {
                       if (records.isEmpty() || records.size() == 1) {
                           Record record = new Record();
                           record.type = Record.TYPE_EMPTY;
                           records.add(record);
                       }
                       adapter.setNewData(records);
                   } else {

                       // 过滤重复月份数据
                       List<Record> data = adapter.getData();
                       Iterator<Record> iterator = records.iterator();
                       while (iterator.hasNext()) {
                           Record next = iterator.next();
                           if (next.type == Record.TYPE_DATE) {
                               if (hasSameMonth(data, next)) {
                                   iterator.remove();
                               }
                           }
                       }
                       adapter.addData(records);
                   }

                   adapter.setEnableLoadMore(resp.hasNextPage());

                   onRequestFinish();
               }

               @Override
               protected void onHandleError(String code, String msg) {
                   super.onHandleError(code, msg);
                   onRequestFinish();
               }

               @Override
               public void onError(Throwable e) {
                   super.onError(e);
                   onRequestFinish();
               }
           });
    }

    private boolean hasSameMonth(List<Record> data, Record month) {

        if (data == null || data.isEmpty()) {
            return false;
        }

        for (Record item : data) {
            if (item.type == Record.TYPE_DATE
                    && item.date.equals(month.date)) {
                return true;
            }
        }

        return false;
    }

    private void onRequestFinish() {
        swipeRefreshLayout.setRefreshing(false);
        adapter.loadMoreComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getMyRecords(page + 1);
    }
}

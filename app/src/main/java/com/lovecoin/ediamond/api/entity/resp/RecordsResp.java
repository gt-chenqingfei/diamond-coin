package com.lovecoin.ediamond.api.entity.resp;

import com.lovecoin.ediamond.ui.record.Record;
import com.lovecoin.ediamond.utils.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/11/27.
 */

public class RecordsResp implements Mapper<List<Record>> {

    private int pageNo;
    private int pageSize;
    private int count;

    private List<RecordDetailItem> transactionRecordItemVos;

    public boolean isFirstpage() {
        return pageNo == 1;
    }

    public boolean hasNextPage() {
        return pageNo * pageSize < count;
    }

    @Override
    public List<Record> transform() {

        List<Record> records = new ArrayList<>();

        if (transactionRecordItemVos != null && !transactionRecordItemVos.isEmpty()) {
            for (RecordDetailItem vo : transactionRecordItemVos) {
                Record record = new Record();
                record.date = vo.showTime;
                record.type = Record.TYPE_DATE;
                records.add(record);
                records.addAll(vo.transform());
            }
        }

        return records;
    }
}

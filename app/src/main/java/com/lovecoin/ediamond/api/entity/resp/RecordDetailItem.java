package com.lovecoin.ediamond.api.entity.resp;

import com.lovecoin.ediamond.ui.record.Record;
import com.lovecoin.ediamond.utils.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/11/27.
 */

public class RecordDetailItem implements Mapper<List<Record>> {
    public String showTime;//:11-27 00:53
    public List<RecordDetailVo> transactionRecordDetailVos;

    @Override
    public List<Record> transform() {
        Record record;
        List<Record> records = new ArrayList<>();
        if (transactionRecordDetailVos != null && !transactionRecordDetailVos.isEmpty()) {
            for (RecordDetailVo vo : transactionRecordDetailVos) {
                record = vo.transform();
                record.date = showTime;
                records.add(record);
            }
        }
        return records;
    }

    public static class RecordDetailVo implements Mapper<Record> {
        public String showTime;//:11-27 00:53
        public String id;//:主键值
        public String uid;//:用户主键
        public String tranCoin;//:交易币总数
        public String payAmount;//:交易金额
        public String tranType;//交易类型0送出 1收到 2购买

        @Override
        public Record transform() {
            Record record = new Record();
            record.time = showTime;
            record.coin = tranCoin;
            record.value = payAmount;
            try {
                record.type = Integer.valueOf(tranType);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                record.type = Record.TYPE_DATE;
            }
            return record;
        }

    }
}

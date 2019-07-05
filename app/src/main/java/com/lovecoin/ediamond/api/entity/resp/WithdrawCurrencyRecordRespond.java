package com.lovecoin.ediamond.api.entity.resp;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币记录返回数据实体类
 * <p>
 * Created by ZhaiDongyang on 2018/6/4
 */
public class WithdrawCurrencyRecordRespond implements MultiItemEntity {

    private List<Trade> list;

    private Trade trade;
    private TradeYearMonth tradeYearMonth;

    public static final int TYPE_ITEM_DATE = 0;
    public static final int TYPE_ITEM_LAYOUT = 1;
    public static final int TYPE_EMPTY = 2;
    private int itemType;

    @Override
    public int getItemType() {
        return this.itemType;
    }

    public List<Trade> getList() {
        return list;
    }

    public void setList(List<Trade> list) {
        this.list = list;
    }

    public WithdrawCurrencyRecordRespond(List<Trade> trade) {
        this.list = trade;
    }

    public WithdrawCurrencyRecordRespond(int itemType) {
        this.itemType = itemType;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public TradeYearMonth getTradeYearMonth() {
        return tradeYearMonth;
    }

    public void setTradeYearMonth(TradeYearMonth tradeYearMonth) {
        this.tradeYearMonth = tradeYearMonth;
    }


    public static class TradeYearMonth {
        private String yearMonth;

        public String getYearMonth() {
            return yearMonth;
        }

        public void setYearMonth(String yearMonth) {
            this.yearMonth = yearMonth;
        }

        public TradeYearMonth(String yearMonth) {
            this.yearMonth = yearMonth;
        }
    }

    public static class Trade implements Parcelable {
        private long recordId;
        private int coin;
        private String walletAddress;
        private int status;
        private long txFinishTime;

        public Trade(long recordId, int coin, String walletAddress, int status, long txFinishTime) {
            this.recordId = recordId;
            this.coin = coin;
            this.walletAddress = walletAddress;
            this.status = status;
            this.txFinishTime = txFinishTime;
        }

        public long getRecordId() {
            return recordId;
        }

        public void setRecordId(long recordId) {
            this.recordId = recordId;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getWalletAddress() {
            return walletAddress;
        }

        public void setWalletAddress(String walletAddress) {
            this.walletAddress = walletAddress;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getTxFinishTime() {
            return txFinishTime;
        }

        public void setTxFinishTime(long txFinishTime) {
            this.txFinishTime = txFinishTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(recordId);
            dest.writeInt(coin);
            dest.writeString(walletAddress);
            dest.writeInt(status);
            dest.writeLong(txFinishTime);
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            @Override
            public Trade createFromParcel(Parcel in) {
                return new Trade(in);
            }

            @Override
            public Trade[] newArray(int size) {
                return new Trade[size];
            }
        };

        public Trade(Parcel in) {
            recordId = in.readLong();
            coin = in.readInt();
            walletAddress = in.readString();
            status = in.readInt();
            txFinishTime = in.readLong();
        }
    }
}

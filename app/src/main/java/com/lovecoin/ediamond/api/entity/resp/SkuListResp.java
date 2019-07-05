package com.lovecoin.ediamond.api.entity.resp;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created on 2018/1/6.
 * <p>
 * //    "balance":15,//账户余额
 * //            "topupList":[{
 * //        "iapId":"123",（google的支付标识）
 * //        "id":"1",
 * //                "money":100.0,
 * //                "status":0,
 * //                "title":"100美元"
 * //    }
 */

public class SkuListResp {

    private String balance;
    private boolean validPhone;
    private List<Sku> topupList;

    public boolean isValidPhone() {
        return validPhone;
    }

    public void setValidPhone(boolean validPhone) {
        this.validPhone = validPhone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<Sku> getTopupList() {
        return topupList;
    }

    public void setTopupList(List<Sku> topupList) {
        this.topupList = topupList;
    }

    public static class Sku {
        private String iapId;
        private String id;
        private BigDecimal money;
        private int status;
        private String title;

        public String getIapId() {
            return iapId;
        }

        public void setIapId(String iapId) {
            this.iapId = iapId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

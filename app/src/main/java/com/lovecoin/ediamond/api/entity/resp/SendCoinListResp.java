package com.lovecoin.ediamond.api.entity.resp;

import java.util.List;

/**
 * Created on 2017/11/29.
 * <p>
 * "sendCoinLists":
 * [{
 * "id":"1",
 * "num":100,//送币数量
 * "status":0,
 * "title":"100币"//显示文字
 * }],
 * "coinNum":1 //币余额
 */

public class SendCoinListResp {

    public int coinNum;
    public List<SendCoin> sendCoinLists;

    public static class SendCoin {

        public String id;
        public int num;
        public int status;
        public String title;
    }
}

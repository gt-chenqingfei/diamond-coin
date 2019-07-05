package com.lovecoin.ediamond.event;


public class BalanceChangeEvent {
    private int num;

    public BalanceChangeEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}

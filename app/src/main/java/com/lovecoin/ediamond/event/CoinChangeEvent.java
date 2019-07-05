package com.lovecoin.ediamond.event;


public class CoinChangeEvent {
    private int num;

    public CoinChangeEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}

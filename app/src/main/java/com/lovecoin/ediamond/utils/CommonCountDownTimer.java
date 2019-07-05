package com.lovecoin.ediamond.utils;

import android.os.CountDownTimer;

public class CommonCountDownTimer extends CountDownTimer {

    public static final int DEFAULT_COUNT_DOWN_INTERVAL = 1000; // 1s

    private CommonCountDownCallback callback;

    public CommonCountDownTimer(long millisInFuture, CommonCountDownCallback callback) {
        this(millisInFuture, DEFAULT_COUNT_DOWN_INTERVAL, callback);
    }

    public CommonCountDownTimer(long millisInFuture, long countDownInterval, CommonCountDownCallback callback) {
        super(millisInFuture, countDownInterval);
        this.callback = callback;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (callback != null) {
            callback.onTimerTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        //Logger.i("count down finish");
        if (callback != null) {
            callback.onTimerFinish();
        }
    }

    public CommonCountDownTimer setCallback(CommonCountDownCallback callback) {
        this.callback = callback;
        return this;
    }

    public interface CommonCountDownCallback {

        void onTimerTick(long millisUntilFinished);

        void onTimerFinish();
    }
}
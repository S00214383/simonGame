package com.education.simongame;

import android.os.CountDownTimer;
public abstract class CountUpTimer extends CountDownTimer {
    private static final long INTERVAL_MS = 100;
    private final long duration;
    public double CurrentSeconds;
    protected CountUpTimer(long durationMs) {
        super(durationMs, INTERVAL_MS);
        this.duration = durationMs;
        CurrentSeconds =0;
    }
    public abstract void onTick(double second);

    @Override
    public void onTick(long msUntilFinished) {
        double second = ((duration - msUntilFinished )/ 100 );
        CurrentSeconds += (second - CurrentSeconds);
        this.onTick(CurrentSeconds);
    }
    @Override
    public void onFinish() {
        onTick(duration/100);
    }
}
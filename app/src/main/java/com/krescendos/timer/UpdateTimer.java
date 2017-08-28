package com.krescendos.timer;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateTimer {

    private static int REPEAT_TIME_MS = 200;

    private Timer timer;
    private long time;
    private OnTimerUpdateListener onTimerUpdateListener;

    public UpdateTimer(final OnTimerUpdateListener onTimerUpdateListener){
        this.onTimerUpdateListener = onTimerUpdateListener;
        time = 0;
        timer = new Timer();
    }

    public void start(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time += REPEAT_TIME_MS;
                onTimerUpdateListener.onUpdate(time);
            }
        }, 0, REPEAT_TIME_MS);
    }

    public void pause(){
        timer.cancel();
    }

    public void setTime(long time){
        this.time = time;
    }
}

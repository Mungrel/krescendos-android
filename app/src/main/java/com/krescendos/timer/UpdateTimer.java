package com.krescendos.timer;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateTimer {

    public static final int REPEAT_TIME_MS = 200;

    private static Timer timer;
    private long time;

    public UpdateTimer() {
        time = 0;
        timer = null;
    }

    public void start() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, REPEAT_TIME_MS);
    }

    public void pause() {
        if (timer == null) {
            return;
        }
        timer.cancel();
        timer.purge();
        timer = null;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
            time += REPEAT_TIME_MS;
        }
    }
}

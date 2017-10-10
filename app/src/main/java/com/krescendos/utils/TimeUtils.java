package com.krescendos.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String msTommss(long ms) {
        if (ms == 0) {
            return "0:00";
        } else {
            return String.format(Locale.ENGLISH, "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(ms),
                    TimeUnit.MILLISECONDS.toSeconds(ms) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
            );
        }
    }
}

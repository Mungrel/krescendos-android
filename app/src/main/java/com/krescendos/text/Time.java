package com.krescendos.text;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Time {

    public static String msTommss(long ms){
        return String.format(Locale.ENGLISH, "%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(ms),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
        );
    }
}
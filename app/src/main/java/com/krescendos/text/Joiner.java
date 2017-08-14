package com.krescendos.text;

import com.krescendos.domain.Artist;

import java.util.List;

public class Joiner {

    public static String join(List<Artist> artists) {
        String out = "";
        for (int i = 0; i < artists.size(); i++) {
            out += artists.get(i);
            if (i < artists.size() - 1) {
                out += ", ";
            }
        }
        return out;
    }
}

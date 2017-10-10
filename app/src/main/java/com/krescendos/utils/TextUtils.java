package com.krescendos.utils;

import com.krescendos.model.Artist;

import java.util.List;

public class TextUtils {

    public static String join(List<Artist> artists) {
        String out = "";
        for (int i = 0; i < artists.size(); i++) {
            out += artists.get(i).getName();
            if (i < artists.size() - 1) {
                out += ", ";
            }
        }
        return out;
    }

    public static String space(String s) {
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            out += s.charAt(i) + " ";
        }
        return out.trim();
    }
}

package com.krescendos.firebase;

public class FirebasePaths {
    public static String BASE_URL = "https://krescendos-174122.firebaseio.com";

    public static String buildPath(String... params) {
        StringBuilder path = new StringBuilder();
        path.append(BASE_URL);

        for (String param : params) {
            path.append("/");
            path.append(param);
        }

        path.append(".json");
        return path.toString();
    }
}

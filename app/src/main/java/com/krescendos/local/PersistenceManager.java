package com.krescendos.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PersistenceManager {

    private static final String ID_PREFS = "host_id_prefs";
    private static PersistenceManager instance;

    private SharedPreferences sharedPreferences;

    private PersistenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PersistenceManager.ID_PREFS, 0);
    }

    public static PersistenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PersistenceManager(context);
        }

        return instance;
    }

    public void persist(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);

        editor.apply();
    }

    public String retrieve(String key) {
        return sharedPreferences.getString(key, "");
    }
}

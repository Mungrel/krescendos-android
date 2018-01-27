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
}

package com.krescendos.web.async;

import com.google.gson.reflect.TypeToken;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.model.Profile;
import com.krescendos.model.Track;

import java.lang.reflect.Type;
import java.util.List;

public class Types {

    public static final Type LIST_TRACK = new TypeToken<List<Track>>() {}.getType();
    public static final Type PARTY = new TypeToken<Party>() {}.getType();
    public static final Type PROFILE = new TypeToken<Profile>() {}.getType();
    public static final Type LIST_STRING = new TypeToken<List<String>>() {}.getType();
    public static final Type LIST_PLAYLIST = new TypeToken<List<Playlist>>() {}.getType();

}

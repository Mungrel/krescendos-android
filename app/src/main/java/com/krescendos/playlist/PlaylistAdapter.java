package com.krescendos.playlist;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.krescendos.R;
import com.krescendos.model.Playlist;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    public PlaylistAdapter(Context context) {
        super(context, R.layout.player_list_layout);
    }
}

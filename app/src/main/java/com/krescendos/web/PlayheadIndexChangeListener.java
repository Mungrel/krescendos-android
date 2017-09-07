package com.krescendos.web;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.R;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.text.TextUtils;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private TrackListAdapter adapter;
    private ImageLoader imageLoader;

    private TextView trackTitle;
    private TextView artistAlbum;
    private NetworkImageView albumArt;
    private SeekBar seekBar;

    public PlayheadIndexChangeListener(Context context, LinearLayout trackDetailsLayout, TrackListAdapter adapter, SeekBar seekBar) {
        this.adapter = adapter;
        this.imageLoader = new Requester(context).getImageLoader();
        this.seekBar = seekBar;

        trackTitle = trackDetailsLayout.findViewById(R.id.current_track_title);
        artistAlbum = trackDetailsLayout.findViewById(R.id.current_track_artist_album);
        albumArt = trackDetailsLayout.findViewById(R.id.current_track_album_art);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("ABC", "HERE");
        int newIndex = dataSnapshot.getValue(Integer.class);
        Log.d("SIZE", "" + adapter.getTracks().size());
        if (adapter.getTracks().isEmpty()) {
            return;
        }
        adapter.setCurrentPosition(newIndex);
        Track newCurrentTrack = adapter.getTracks().get(newIndex);

        seekBar.setMax((int) newCurrentTrack.getDuration_ms());
        Log.d("MAX", "MaxSet");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

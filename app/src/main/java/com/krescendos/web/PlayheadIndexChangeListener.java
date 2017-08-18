package com.krescendos.web;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.R;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.player.TrackPlayer;
import com.krescendos.text.TextUtils;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private TrackListAdapter adapter;
    private ImageLoader imageLoader;

    private TextView trackTitle;
    private TextView artistAlbum;
    private NetworkImageView albumArt;

    public PlayheadIndexChangeListener(Context context, LinearLayout trackDetailsLayout, TrackListAdapter adapter){
        this.adapter = adapter;
        this.imageLoader = Requester.getInstance(context).getImageLoader();

        trackTitle = trackDetailsLayout.findViewById(R.id.current_track_title);
        artistAlbum = trackDetailsLayout.findViewById(R.id.current_track_artist_album);
        albumArt = trackDetailsLayout.findViewById(R.id.current_track_album_art);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int newIndex = dataSnapshot.getValue(Integer.class);
        Track newCurrentTrack = adapter.getTracks().get(newIndex);

        trackTitle.setText(newCurrentTrack.getName());
        artistAlbum.setText(TextUtils.join(newCurrentTrack.getArtists())+" - "+newCurrentTrack.getAlbum());
        albumArt.setImageUrl(newCurrentTrack.getAlbum().getImages().get(0).getUrl(), imageLoader);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

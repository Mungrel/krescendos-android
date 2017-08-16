package com.krescendos.player;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.domain.Track;
import com.krescendos.text.TextUtils;
import com.krescendos.web.Requester;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter extends ArrayAdapter<Track> {
    private List<Track> tracks;
    private LayoutInflater mInflater;
    private String currentPlayingId;
    private Requester requester;

    public TrackListAdapter(Context context) {
        super(context, R.layout.player_list_layout);
        this.tracks = new ArrayList<Track>();
        this.mInflater = LayoutInflater.from(context);
        this.requester = new Requester(context);
        currentPlayingId = null;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Track getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPosition(Track item) {
        return tracks.indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.player_list_layout, parent, false);
            holder.trackName = convertView.findViewById(R.id.up_next_track_name);
            holder.artistAlbum = convertView.findViewById(R.id.up_next_artist_album);
            holder.albumArt = convertView.findViewById(R.id.up_next_album_art);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Track track = tracks.get(position);
        holder.trackName.setText(track.getName());
        holder.artistAlbum.setText(TextUtils.join(track.getArtists()));
        holder.albumArt.setImageUrl(track.getAlbum().getImages().get(0).getUrl(), requester.getImageLoader());
        holder.pos = position;

        if (tracks.get(position).getId().equals(currentPlayingId)) {
            holder.artistAlbum.setTextColor(Color.BLUE);
            holder.trackName.setTextColor(Color.BLUE);
        } else {
            holder.artistAlbum.setTextColor(Color.WHITE);
            holder.trackName.setTextColor(Color.WHITE);
        }

        return convertView;
    }

    public void addTrack(Track track) {
        tracks.add(track);
        notifyDataSetChanged();
    }

    public void updateTracks(List<Track> tracks) {
        this.tracks = tracks;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {

        TextView trackName;
        TextView artistAlbum;
        NetworkImageView albumArt;
        int pos;
    }

    public void setCurrentPlayingId(String currentPlayingId) {
        this.currentPlayingId = currentPlayingId;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
package com.krescendos.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.model.Track;
import com.krescendos.text.TextUtils;
import com.krescendos.utils.ListResizer;
import com.krescendos.web.Requester;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter extends ArrayAdapter<Track> {
    private List<Track> tracks;
    private int currentPosition;
    private LayoutInflater mInflater;
    private Requester requester;
    private boolean itemsSelectable;
    private ListView upNextListView;
    private LinearLayout currentTrackLayout;

    public TrackListAdapter(Context context, ListView upNextListView, LinearLayout currentTrackLayout) {
        super(context, R.layout.player_list_layout);
        this.tracks = new ArrayList<Track>();
        this.mInflater = LayoutInflater.from(context);
        this.requester = Requester.getInstance(context);
        this.itemsSelectable = false;
        this.upNextListView = upNextListView;
        this.currentTrackLayout = currentTrackLayout;
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

        holder.pos = position;

        Log.d("HOLDER_POS", "" + holder.pos);
        Log.d("CURRENT_POS", "" + currentPosition);
        if (holder.pos <= currentPosition) {
            convertView.setVisibility(View.GONE);
            holder.trackName.setVisibility(View.GONE);
            holder.artistAlbum.setVisibility(View.GONE);
            holder.albumArt.setVisibility(View.GONE);
        } else {
            Track track = tracks.get(position);
            holder.trackName.setText(track.getName());
            holder.artistAlbum.setText(TextUtils.join(track.getArtists()));
            holder.albumArt.setImageUrl(track.getAlbum().getSmallestImage().getUrl(), requester.getImageLoader());
        }

        updateCurrentTrackLayout();
        return convertView;
    }

    public void addTrack(Track track) {
        tracks.add(track);
        notifyDataSetChanged();
    }

    public void updateTracks(List<Track> tracks) {
        Log.d("UPDATE", "newListSize: " + tracks.size());
        this.tracks = tracks;
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
        notifyDataSetChanged();
    }

    private void updateCurrentTrackLayout() {
        Track currentTrack = tracks.get(currentPosition);
        TextView trackTitle = currentTrackLayout.findViewById(R.id.current_track_title);
        TextView artistAlbum = currentTrackLayout.findViewById(R.id.current_track_artist_album);
        NetworkImageView albumArt = currentTrackLayout.findViewById(R.id.current_track_album_art);

        trackTitle.setText(currentTrack.getName());
        artistAlbum.setText(TextUtils.join(currentTrack.getArtists()) + " - " + currentTrack.getAlbum().getName());
        albumArt.setImageUrl(currentTrack.getAlbum().getLargestImage().getUrl(), Requester.getInstance(getContext()).getImageLoader());
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        ListResizer.setListViewHeightBasedOnChildren(upNextListView);
    }

    @Override
    public boolean isEnabled(int pos) {
        return itemsSelectable;
    }

    private static class ViewHolder {

        TextView trackName;
        TextView artistAlbum;
        NetworkImageView albumArt;
        int pos;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setItemsSelectable(boolean itemsSelectable) {
        this.itemsSelectable = itemsSelectable;
    }
}
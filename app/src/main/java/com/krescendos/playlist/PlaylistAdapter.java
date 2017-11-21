package com.krescendos.playlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.firebase.FirebaseManager;
import com.krescendos.model.Playlist;
import com.krescendos.model.Track;
import com.krescendos.search.SearchTrackListAdapter;
import com.krescendos.utils.TextUtils;
import com.krescendos.web.Requester;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    private List<Playlist> playlists;
    private LayoutInflater mInflater;
    private Requester requester;

    public PlaylistAdapter(Context context) {
        super(context, R.layout.player_list_layout);
        this.mInflater = LayoutInflater.from(context);
        this.requester = Requester.getInstance(context);
        this.playlists = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Playlist getItem(int position) {
        return playlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPosition(Playlist item) {
        return playlists.indexOf(item);
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
        final PlaylistAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new PlaylistAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.search_list_layout, parent, false);
            holder.playlistName = convertView.findViewById(R.id.search_list_track_name);
            holder.playlistSize = convertView.findViewById(R.id.search_list_artist_album);
            holder.playlistArt = convertView.findViewById(R.id.search_list_album_art);
            holder.expandButton = convertView.findViewById(R.id.search_list_add_button);

            convertView.setTag(holder);

        } else {
            holder = (PlaylistAdapter.ViewHolder) convertView.getTag();
        }
        final Playlist playlist = playlists.get(position);
        holder.playlistName.setText(playlist.getName());
        holder.playlistSize.setText(String.format("%d %s", playlist.size(), "tracks"));
        holder.playlistArt.setImageUrl(playlist.getSmallestImage().getUrl(), requester.getImageLoader());

        holder.expandButton.setImageResource(R.drawable.add);
        holder.pos = position;

        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandButton.setImageResource(R.drawable.check);
            }
        });

        return convertView;
    }

    public void updateResults(List<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView playlistName;
        TextView playlistSize;
        NetworkImageView playlistArt;
        ImageButton expandButton;
        int pos;
    }
}

package com.krescendos.playlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.activities.PlaylistTracksActivity;
import com.krescendos.model.Playlist;
import com.krescendos.web.ImageHandler;
import com.krescendos.web.Requester;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    private List<Playlist> playlists;
    private LayoutInflater mInflater;
    private Requester requester;
    private ImageLoader imageLoader;
    private Context context;
    private String partyID;

    public PlaylistAdapter(Context context, String partyID) {
        super(context, R.layout.player_list_layout);
        this.mInflater = LayoutInflater.from(context);
        this.requester = Requester.getInstance();
        this.imageLoader = ImageHandler.getInstance(context).getImageLoader();
        this.playlists = new ArrayList<>();
        this.context = context;
        this.partyID = partyID;
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
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final PlaylistAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new PlaylistAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.playlist_list_layout, parent, false);
            holder.playlistName = convertView.findViewById(R.id.playlist_name);
            holder.playlistSize = convertView.findViewById(R.id.playlist_size);
            holder.playlistArt = convertView.findViewById(R.id.playlist_art);
            holder.expandButton = convertView.findViewById(R.id.playlist_expand_button);

            convertView.setTag(holder);

        } else {
            holder = (PlaylistAdapter.ViewHolder) convertView.getTag();
        }
        final Playlist playlist = playlists.get(position);
        holder.playlistName.setText(playlist.getName());
        holder.playlistSize.setText(String.format("%d %s", playlist.size(), "tracks"));
        holder.playlistArt.setImageUrl(playlist.getSmallestImage().getUrl(), imageLoader);

        holder.pos = position;

        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaylistTracksActivity.class);
                intent.putExtra("playlist", new Gson().toJson(playlists.get(position)));
                intent.putExtra("partyID", partyID);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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

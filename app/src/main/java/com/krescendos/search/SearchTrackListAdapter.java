package com.krescendos.search;

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
import com.krescendos.domain.Track;
import com.krescendos.text.TextUtils;
import com.krescendos.web.Requester;

import java.util.ArrayList;
import java.util.List;

public class SearchTrackListAdapter extends ArrayAdapter<Track> {

    private List<Track> playlist;
    private List<Track> searchResults;
    private LayoutInflater mInflater;
    private Requester requester;
    private String partyCode;

    public SearchTrackListAdapter(Context context, List<Track> playlist, String partyCode) {
        super(context, R.layout.search_list_layout);
        this.playlist = playlist;
        this.searchResults = new ArrayList<Track>();
        this.mInflater = LayoutInflater.from(context);
        this.requester = new Requester(context);
        this.partyCode = partyCode;
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Track getItem(int position) {
        return searchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPosition(Track item) {
        return searchResults.indexOf(item);
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
        final SearchTrackListAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new SearchTrackListAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.search_list_layout, parent, false);
            holder.trackName = convertView.findViewById(R.id.search_list_track_name);
            holder.artistAlbum = convertView.findViewById(R.id.search_list_artist_album);
            holder.albumArt = convertView.findViewById(R.id.search_list_album_art);
            holder.addCheck = convertView.findViewById(R.id.search_list_add_button);

            convertView.setTag(holder);

        } else {
            holder = (SearchTrackListAdapter.ViewHolder) convertView.getTag();
        }
        final Track track = searchResults.get(position);
        holder.trackName.setText(track.getName());
        holder.artistAlbum.setText(TextUtils.join(track.getArtists()));
        holder.albumArt.setImageUrl(track.getAlbum().getSmallestImage().getUrl(), requester.getImageLoader());
        if (playlist.contains(track)) {
            holder.addCheck.setImageResource(R.drawable.check);
        } else {
            holder.addCheck.setImageResource(R.drawable.add);
        }
        holder.pos = position;

        holder.addCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addCheck.setImageResource(R.drawable.check);
                requester.append(partyCode, track);
            }
        });

        return convertView;
    }

    public void updateResults(List<Track> tracks) {
        this.searchResults = tracks;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView trackName;
        TextView artistAlbum;
        NetworkImageView albumArt;
        ImageButton addCheck;
        int pos;
    }
}

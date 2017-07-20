package com.krescendos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krescendos.domain.Artist;
import com.krescendos.domain.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter extends ArrayAdapter<Track> {
    private Context context;
    private List<Track> tracks;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public TrackListAdapter(Context context, List<Track> tracks) {
        super(context, R.layout.player_list);
        this.context = context;
        this.tracks = new ArrayList<Track>(tracks);
        this.mInflater = LayoutInflater.from(context);
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = mInflater.inflate(R.layout.player_list, parent, false);
                    holder.trackName = (TextView) convertView.findViewById(R.id.listTrackName);
                    holder.artistName = (TextView) convertView.findViewById(R.id.listArtistName);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.trackName.setText(tracks.get(position).getName());
        String artistString = "";
        for (Artist artist : tracks.get(position).getArtists()){
            artistString += artist.getName()+" ";
        }
        artistString = artistString.trim();
        holder.artistName.setText(artistString);
        holder.pos = position;
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    static class ViewHolder {

        TextView trackName;
        TextView artistName;
        int pos;
    }
}
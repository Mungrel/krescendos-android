package com.krescendos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 4/07/2017.
 */

public class Track {

    private String id;
    private String name;
    private String artist;

    public Track(String id, String name, String artist){
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public static List<Track> getTrackList(){
        List<Track> list = new ArrayList<Track>();
        list.add(new Track("id1", "name1", "artist1"));
        list.add(new Track("id2", "name2", "artist2"));
        list.add(new Track("id3", "name3", "artist3"));

        return list;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }
}

package com.krescendos.model;

import java.util.List;

public class Track {
    private String id;
    private String name;
    private Album album;
    private long duration_ms;
    private List<Artist> artists;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Album getAlbum() {
        return album;
    }

    public long getDuration_ms() {
        return duration_ms;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setDuration_ms(long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getTrackURI() {
        return "spotify:track:" + id;
    }
}

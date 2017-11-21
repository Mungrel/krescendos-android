package com.krescendos.model;

import java.util.List;

public class Playlist {

    private String id;
    private String name;
    private String owner;
    private List<Track> tracks;
    private List<AlbumArt> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<AlbumArt> getImages() {
        return images;
    }

    public void setImages(List<AlbumArt> images) {
        this.images = images;
    }

    public AlbumArt getSmallestImage() {
        return images.get(images.size() - 1);
    }

    public AlbumArt getLargestImage() {
        return images.get(0);
    }

    public int size() {
        return tracks.size();
    }
}

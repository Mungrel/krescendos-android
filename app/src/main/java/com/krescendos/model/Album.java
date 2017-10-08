package com.krescendos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable{
    private String id;
    private String name;
    private String type;
    private List<Artist> artists;
    private List<AlbumArt> images;

    public Album() {
        this.artists = new ArrayList<Artist>();
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
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
}

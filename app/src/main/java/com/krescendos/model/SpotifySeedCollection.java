package com.krescendos.model;

import java.util.ArrayList;
import java.util.List;

public class SpotifySeedCollection {

    private List<String> artistSeeds;
    private List<String> genreSeeds;
    private List<String> trackSeeds;

    public SpotifySeedCollection() {
        this.artistSeeds = new ArrayList<>();
        this.genreSeeds = new ArrayList<>();
        this.trackSeeds = new ArrayList<>();
    }

    public int getTotalSize() {
        return artistSeeds.size() + genreSeeds.size() + trackSeeds.size();
    }

    public List<String> getArtistSeeds() {
        return artistSeeds;
    }

    public void setArtistSeeds(List<String> artistSeeds) {
        this.artistSeeds = artistSeeds;
    }

    public List<String> getGenreSeeds() {
        return genreSeeds;
    }

    public void setGenreSeeds(List<String> genreSeeds) {
        this.genreSeeds = genreSeeds;
    }

    public List<String> getTrackSeeds() {
        return trackSeeds;
    }

    public void setTrackSeeds(List<String> trackSeeds) {
        this.trackSeeds = trackSeeds;
    }
}

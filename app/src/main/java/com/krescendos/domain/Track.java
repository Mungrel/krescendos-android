package com.krescendos.domain;

import java.util.ArrayList;
import java.util.List;

public class Track {
	private String id;
	private String name;
	private Album album;
	private List<Artist> artists;

	public Track() {
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

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
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
}

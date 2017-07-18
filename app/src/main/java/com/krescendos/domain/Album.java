package com.krescendos.domain;

import java.util.ArrayList;
import java.util.List;

public class Album {
	private String id;
	private String name;
	private String type;
	private List<Artist> artists;

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
}
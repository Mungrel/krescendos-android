package com.krescendos.model;

import java.io.Serializable;

public class AlbumArt implements Serializable{
    private String url;
    private int height;
    private int width;

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

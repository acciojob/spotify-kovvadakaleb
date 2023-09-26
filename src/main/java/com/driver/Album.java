package com.driver;

import java.util.Date;
import java.util.List;

public class Album {
    private String title;
    private Date releaseDate;

    private String artist;

    public Album(){

    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Album(String title, String artist){
        this.title = title;
        this.artist = artist;
        this.releaseDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}

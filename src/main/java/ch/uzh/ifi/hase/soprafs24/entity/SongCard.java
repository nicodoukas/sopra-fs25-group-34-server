package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;

public class SongCard implements Serializable {

    private String title; //"trackName"
    private String artist; //"artistName"
    private int year; //"releaseDate"
    private String songURL; //"previewURL"

    public SongCard() {this.title = "defaultSongCard";}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setArtist(String artist) {this.artist = artist;}
    public String getArtist() {return artist;}

    public void setYear(int year) {this.year = year;}
    public int getYear() {return year;}

    public void setSongURL(String songURL) {this.songURL = songURL;}
    public String getSongURL() {return songURL;}

}

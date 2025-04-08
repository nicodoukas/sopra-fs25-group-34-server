package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class SongCardGetDTO {

    private String title;
    private String artist;
    private int year;
    private String songURL;

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setArtist(String artist) {this.artist = artist;}
    public String getArtist() {return artist;}

    public void setYear(int year) {this.year = year;}
    public int getYear() {return year;}

    public void setSongURL(String songURL) {this.songURL = songURL;}
    public String getSongURL() {return songURL;}
}
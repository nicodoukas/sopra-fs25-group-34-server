package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;

public class PlayerPutDTO {

    private boolean addCoin; // True if one adds a coin.
    private int position; // integer where to place the SongCard into timeline
    private SongCard songCard; // SongCard to place inside timeline.

    public void setAddCoin(boolean addCoin) {this.addCoin = addCoin;}
    public boolean getAddCoin() {return this.addCoin;}

    public void setPosition(int position) {this.position = position;}
    public int getPosition() {return this.position;}

    public void setSongCard(SongCard songCard) {this.songCard = songCard;}
    public SongCard getSongCard() {return this.songCard;}

}
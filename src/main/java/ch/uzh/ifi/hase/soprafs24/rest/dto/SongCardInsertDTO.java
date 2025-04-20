package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;

public class SongCardInsertDTO {

    private SongCard songCard;
    private int position;

    public SongCard getSongCard() {
        return songCard;
    }

    public void setSongCard(SongCard songCard) {
        this.songCard = songCard;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Player;

public class GuessPostDTO {

    private String guessedTitle;
    private String guessedArtist;
    private Player player;

    public void setGuessedTitle(String guessedTitle) {
        if (guessedTitle != null) {
            this.guessedTitle = guessedTitle.trim();
        }
        else {
            this.guessedTitle = null;
        }
    }

    public String getGuessedTitle() {
        return this.guessedTitle;
    }

    public void setGuessedArtist(String guessedArtist) {
        if (guessedArtist != null) {
            this.guessedArtist = guessedArtist.trim();
        }
        else {
            this.guessedArtist = null;
        }
    }
    public String getGuessedArtist() {return this.guessedArtist;}

    public void setPlayer(Player player) {this.player = player;}
    public Player getPlayer() {return this.player;}
}

package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Comparator;

public class Player implements Serializable {

    private Long userId;
    private Long gameId;
    private int coinBalance;
    private String username;
    private List<SongCard> timeline;
    private ProfilePicture profilePicture;

    public Player() {
        this.coinBalance = 2;
        this.timeline = new ArrayList<SongCard>();
    }

    public void setUserId(Long userId) {this.userId = userId;}
    public Long getUserId() {return this.userId;}

    public void setGameId(Long gameId) {this.gameId = gameId;}
    public Long getGameId() {return this.gameId;}

    public void setCoinBalance(int coinBalance) {this.coinBalance = coinBalance;}
    public int getCoinBalance() {return this.coinBalance;}

    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return this.username;}
    
    public void setTimeline(List<SongCard> timeline) {this.timeline = timeline;}
    public List<SongCard> getTimeline() {return this.timeline;}

    public void setProfilePicture(ProfilePicture profilePicture) {this.profilePicture = profilePicture;}
    public ProfilePicture getProfilePicture() {return this.profilePicture;}

    public void addCoin() {
        if (this.coinBalance < 5) {this.coinBalance += 1;}
    }
    
    public void updateTimeline(int placement, SongCard songCard) {
        if (placement<0) {
            throw new IllegalArgumentException("placement must be greater than zero");
        }
        this.timeline.add(placement, songCard); //IMPORTANT counting starts at 0 [0,1,2,3,...]
    }

    public void buySongCard() {
        if (this.coinBalance < 3) {
            throw new IllegalStateException("Not enough coins to buy a song card");
        }
        this.coinBalance -= 3;

        // add a default song card
        SongCard defaultSongCard = new SongCard();
        defaultSongCard.setTitle("default");
        defaultSongCard.setArtist("default");
        defaultSongCard.setSongURL("default");

        // with a random year between 1950 and 2025
        int randomYear = new Random().nextInt(2025 - 1950 + 1) + 1950;
        defaultSongCard.setYear(randomYear);

        this.timeline.add(defaultSongCard);
        this.timeline.sort(Comparator.comparingInt(SongCard::getYear)); // sort the timeline so default card is inserted at correct position
    }
}

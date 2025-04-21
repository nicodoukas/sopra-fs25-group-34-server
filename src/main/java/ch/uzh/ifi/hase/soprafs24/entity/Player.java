package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private Long userId;
    private Long gameId;
    private int coinBalance;
    private String username;
    private List<SongCard> timeline;

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

    public void addCoin() {
        if (this.coinBalance < 5) {this.coinBalance += 1;}
    }
    
    public void updateTimeline(int placement, SongCard songCard) {
        if (placement<0) {
            throw new IllegalArgumentException("placement must be greater than zero");
        }
        this.timeline.add(placement, songCard); //IMPORTANT counting starts at 0 [0,1,2,3,...]
    }
}

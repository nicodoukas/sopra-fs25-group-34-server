package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class PlayerGetDTO {

    private Long userId;
    private Long gameId;
    private int coinBalance;
    private String username;
    private List<SongCardGetDTO> timeline;

    public void setUserId(Long userId) {this.userId = userId;}
    public Long getUserId() {return userId;}

    public void setGameId(Long gameId) {this.gameId = gameId;}
    public Long getGameId() {return gameId;}

    public void setCoinBalance(int coinBalance) {this.coinBalance = coinBalance;}
    public int getCoinBalance() {return coinBalance;}

    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return username;}

    public void setTimeline(List<SongCardGetDTO> timeline) {this.timeline = timeline;}
    public List<SongCardGetDTO> getTimeline() {return timeline;}
}

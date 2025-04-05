package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;

public class Player implements Serializable {

    private Long userId;
    private Long gameId;
    private int coinBalance;
    private String username;

    public Player() {
        this.coinBalance = 2;
    }

    public void setUserId(Long userId) {this.userId = userId;}
    public Long getUserId() {return this.userId;}

    public void setGameId(Long gameId) {this.gameId = gameId;}
    public Long getGameId() {return this.gameId;}

    public void setCoinBalance(int coinBalance) {this.coinBalance = coinBalance;}
    public int getCoinBalance() {return this.coinBalance;}

    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return this.username;}

    public void addCoin() {this.coinBalance += 1;}
}

package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Round;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class GameGetDTO {

    private List<Player> players;
    private Long gameId;
    private Long turnCount;
    private Queue<Player> turnOrder;
    private Player host;
    private Round currentRound;
    private String gameName;

    public void setPlayers(List<Player> players) {this.players = players;}
    public List<Player> getPlayers() {return this.players;}

    public Long getGameId() {return this.gameId;}
    public void setGameId(Long gameId) {this.gameId = gameId;}

    public Long getTurnCount() {return this.turnCount;}
    public void setTurnCount(Long turnCount) {this.turnCount = turnCount;}

    public Queue<Player> getTurnOrder() {return this.turnOrder;}
    public void setTurnOrder(LinkedList<Player> turnOrder) {this.turnOrder = turnOrder;}

    public Player getHost() {return this.host;}
    public void setHost(Player host) {this.host = host;}

    public Round getCurrentRound() {return this.currentRound;}
    public void setCurrentRound(Round currentRound) {this.currentRound = currentRound;}

    public String getGameName() {return this.gameName;}
    public void setGameName(String gameName) {this.gameName = gameName;}
}

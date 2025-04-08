package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game implements Serializable {

    private List<Player> players;
    private Long gameId; // lobbyId
    private String gameName; //lobbyName
    private Long turnCount;
    private Queue<Player> turnOrder;
    private Player host;

    public Game() {
        this.players = new ArrayList<Player>();
        this.turnOrder = new LinkedList<Player>();
    }

    public void setPlayers(List<Player> players) {this.players = players;}
    public List<Player> getPlayers() {return this.players;}

    public void setGameId(Long gameId) {this.gameId = gameId;}
    public Long getGameId() {return this.gameId;}

    public void setGameName(String gameName) {this.gameName = gameName;}
    public String getGameName() {return this.gameName;}

    public void setTurnCount(Long turnCount) {this.turnCount = turnCount;}
    public Long getTurnCount() {return this.turnCount;}

    public void setHost(Player host) {this.host = host;}
    public Player getHost() {return this.host;}

    public void setTurnOrder(Queue<Player> turnOrder) {this.turnOrder = turnOrder;}
    public Queue<Player> getTurnOrder() {return this.turnOrder;}

    public List<Player> createPlayers(List<User> users) {
        for (User user : users) {
            Player player = new Player();
            player.setUserId(user.getId());
            player.setUsername(user.getUsername());
            player.setGameId(this.gameId);
            player.updateTimeline(0, new SongCard());

            this.players.add(player);
        }
        return this.players;
    }

    public Queue<Player> createTurnOrder(List<Player> players) {
        this.turnOrder.addAll(players);
        return this.turnOrder;
    }
    
}

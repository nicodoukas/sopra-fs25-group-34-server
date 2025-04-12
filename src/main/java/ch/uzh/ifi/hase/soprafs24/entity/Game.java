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
    private int turnCount;
    private Queue<Player> turnOrder;
    private Player host;
    private Round currentRound;

    public Game() {
        this.players = new ArrayList<Player>();
        this.turnOrder = new LinkedList<Player>();
        this.turnCount = 0;
    }

    public void setPlayers(List<Player> players) {this.players = players;}
    public List<Player> getPlayers() {return this.players;}

    public void setGameId(Long gameId) {this.gameId = gameId;}
    public Long getGameId() {return this.gameId;}

    public void setGameName(String gameName) {this.gameName = gameName;}
    public String getGameName() {return this.gameName;}

    public void setTurnCount(int turnCount) {this.turnCount = turnCount;}
    public int getTurnCount() {return this.turnCount;}

    public void setHost(Player host) {this.host = host;}
    public Player getHost() {return this.host;}

    public void setTurnOrder(Queue<Player> turnOrder) {this.turnOrder = turnOrder;}
    public Queue<Player> getTurnOrder() {return this.turnOrder;}

    public void setCurrentRound(Round currentRound) {this.currentRound = currentRound;}
    public Round getCurrentRound() {return this.currentRound;}

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

    public void startNewRound() {
        this.currentRound = new Round();
        final Player activePlayer = this.turnOrder.poll(); //get first player in the queue
        this.turnOrder.add(activePlayer); //add the player at the back of the queue
        this.currentRound.setActivePlayer(activePlayer);
        this.currentRound.setSongCard(new SongCard()); //TODO figure out how this will work with Music API
        this.turnCount += 1; //new Round => turnCount + 1
        this.currentRound.setRoundNr(this.turnCount);
    }
    
}

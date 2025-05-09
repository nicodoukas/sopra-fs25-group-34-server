package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.service.APIHandler;

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

    public List<Player> createPlayers(List<User> users, APIHandler apiHandler) {
        for (User user : users) {
            Player player = new Player();
            player.setUserId(user.getId());
            player.setUsername(user.getUsername());
            player.setGameId(this.gameId);
            player.setProfilePicture(user.getProfilePicture());
            SongCard songCard1 = apiHandler.getNewSongCard();
            player.updateTimeline(0, songCard1);
            this.players.add(player);
        }
        return this.players;
    }

    public Queue<Player> createTurnOrder(List<Player> players) {
        this.turnOrder.addAll(players);
        return this.turnOrder;
    }

    public void startNewRound(SongCard newSongCard) {
        increaseTurnCount();
        Player newActivePlayer = updateTurnOrder();
        updateRound(newSongCard, newActivePlayer);
    }

    private void increaseTurnCount() {
        turnCount +=1;
    }

    private Player updateTurnOrder() {
        Player newActivePlayer = turnOrder.poll(); // removes the newActivePlayer from the queue
        turnOrder.add(newActivePlayer); // then adds that player again at the end
        return newActivePlayer;
    }

    private void updateRound(SongCard songCard, Player newActivePlayer) {
        Round nextRound = new Round();
        nextRound.setActivePlayer(newActivePlayer);
        nextRound.setSongCard(songCard);
        nextRound.setRoundNr(turnCount);
        currentRound = nextRound;
    }

    public void leaveGame(User user) {
        players.removeIf(player -> player.getUserId().equals(user.getId()));

        Queue<Player> tempQueue = new LinkedList<>();
        while (!turnOrder.isEmpty()) {
            Player nextPlayer = turnOrder.poll();
            if (!nextPlayer.getUserId().equals(user.getId())) {
                tempQueue.add(nextPlayer);
            }
        }
        setTurnOrder(tempQueue);
    }
    
}

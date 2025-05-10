package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testSetGameId() {
        Game game = new Game();
        game.setGameId(1L);
        assertEquals(1L, game.getGameId());
    }

    @Test
    public void testSetGameName() {
        Game game = new Game();
        game.setGameName("testGame");
        assertEquals("testGame", game.getGameName());
    }

    @Test
    public void testSetTurnCount() {
        Game game = new Game();
        game.setTurnCount(5);
        assertEquals(5, game.getTurnCount());
    }

    @Test
    public void testSetGameHost() {
        Game game = new Game();
        Player host = new Player(); // in game, host is a player, as opposed to user in lobby
        host.setUserId(1L);
        host.setUsername("HostPlayer");

        game.setHost(host);

        assertNotNull(game.getHost());
        assertEquals("HostPlayer", game.getHost().getUsername());
        assertEquals(1L, game.getHost().getUserId());
    }

    @Test
    public void testSetPlayers() {
        Game game = new Game();
        Player player1 = new Player();
        player1.setUserId(1L);
        player1.setUsername("Player1");

        Player player2 = new Player();
        player2.setUserId(2L);
        player2.setUsername("Player2");

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        game.setPlayers(players);

        assertEquals(2, game.getPlayers().size());
        assertEquals("Player1", game.getPlayers().get(0).getUsername());
        assertEquals("Player2", game.getPlayers().get(1).getUsername());
    }

    @Test
    public void testSetTurnOrder() {
        Game game = new Game();
        Player player1 = new Player();
        player1.setUserId(1L);
        player1.setUsername("Player1");

        Player player2 = new Player();
        player2.setUserId(2L);
        player2.setUsername("Player2");

        Queue<Player> turnOrder = new LinkedList<>();
        turnOrder.add(player1);
        turnOrder.add(player2);

        game.setTurnOrder(turnOrder);

        assertEquals(2, game.getTurnOrder().size());
        assertEquals("Player1", game.getTurnOrder().peek().getUsername()); // get head of TurnOrder
    }

    @Test
    public void testSetCurrentRound() {
        Game game = new Game();
        Round round = new Round();
        round.setRoundNr(1);

        game.setCurrentRound(round);

        assertNotNull(game.getCurrentRound());
        assertEquals(1, game.getCurrentRound().getRoundNr());
    }
}

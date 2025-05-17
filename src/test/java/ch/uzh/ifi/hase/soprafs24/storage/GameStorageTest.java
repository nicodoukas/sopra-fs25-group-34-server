package ch.uzh.ifi.hase.soprafs24.storage;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class GameStorageTest {

    private GameStorage gameStorage;

    @BeforeEach
    public void setup() {
        gameStorage = new GameStorage();
    }

    @Test
    public void addGame_success() {
        Game game = new Game();
        game.setGameId(1L);
        game.setGameName("Test Game");

        Game addedGame = gameStorage.addGame(game);

        assertNotNull(addedGame);
        assertEquals(1L, addedGame.getGameId());
        assertEquals("Test Game", addedGame.getGameName());
    }

    @Test
    public void addGame_nullGameId_throwsException() {
        Game game = new Game();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gameStorage.addGame(game);
        });
        assertEquals("Game or gameId cannot be null", exception.getMessage());
    }

    @Test
    public void getGameById_success() {
        Game game = new Game();
        game.setGameId(1L);
        game.setGameName("Test Game");

        gameStorage.addGame(game);

        Game foundGame = gameStorage.getGameById(1L);

        assertNotNull(foundGame);
        assertEquals(1L, foundGame.getGameId());
        assertEquals("Test Game", foundGame.getGameName());
    }

    @Test
    public void getGameById_notFound_throwsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameStorage.getGameById(123L);
        });
        assertEquals("404 NOT_FOUND \"Game with ID 123 not found\"", exception.getMessage());
    }

    @Test
    public void deleteGame_success() {
        Game game = new Game();
        game.setGameId(1L);
        game.setGameName("Test Game");

        gameStorage.addGame(game);

        gameStorage.deleteGame(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameStorage.getGameById(1L);
        });
        assertEquals("404 NOT_FOUND \"Game with ID 1 not found\"", exception.getMessage());
    }

    @Test
    public void deleteGame_notFound_throwsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameStorage.deleteGame(123L);
        });
        assertEquals("404 NOT_FOUND \"Game with ID 123 not found\"", exception.getMessage());
    }

    @Test
    public void getGameById_withPlayers_success() {
        Game game = new Game();
        game.setGameId(1L);
        game.setGameName("Test Game");

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
        gameStorage.addGame(game);

        Game foundGame = gameStorage.getGameById(1L);

        assertNotNull(foundGame);
        assertEquals(2, foundGame.getPlayers().size());
        assertEquals("Player1", foundGame.getPlayers().get(0).getUsername());
        assertEquals("Player2", foundGame.getPlayers().get(1).getUsername());
    }

    @Test
    public void getGameById_withRound_success() {
        Game game = new Game();
        game.setGameId(1L);
        game.setGameName("Test Game");

        game.setTurnCount(1);
        gameStorage.addGame(game);

        Game foundGame = gameStorage.getGameById(1L);

        assertNotNull(foundGame);
        assertEquals(1, foundGame.getTurnCount());
    }
}

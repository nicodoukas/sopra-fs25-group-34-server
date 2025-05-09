package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.GameStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    @Mock
    private GameStorage gameStorage;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LobbyService lobbyService;
    @Mock
    private APIHandler apiHandler;
    @Mock
    private UserService userService;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

        Mockito.when(gameStorage.addGame(Mockito.any())).thenReturn(testGame);
        Mockito.when(gameStorage.getGameById(testGame.getGameId())).thenReturn(testGame);
    }

    @Test
    public void getGameById_success() {
        Game getGame = gameService.getGameById(testGame.getGameId());
        assertEquals(testGame, getGame);
        Mockito.verify(gameStorage, Mockito.times(1)).getGameById(testGame.getGameId());
    }
    @Test
    public void getGameById_fail() {
        gameStorage = new GameStorage();
        gameService = new GameService(gameStorage, userRepository, lobbyService, apiHandler, userService);
        assertThrows(ResponseStatusException.class, () -> gameService.getGameById(2L));
    }

    @Test
    public void getPlayerInGame_success() {
        Player player = new Player();
        player.setGameId(1L);
        player.setUserId(1L);
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        testGame.setPlayers(players);
        Player getPlayer = gameService.getPlayerInGame(testGame.getGameId(), player.getUserId());
        assertEquals(player, getPlayer);
        Mockito.verify(gameStorage, Mockito.times(1)).getGameById(testGame.getGameId());
    }

    @Test
    public void getPlayerInGame_fail() {
        Player player = new Player();
        player.setGameId(1L);
        player.setUserId(1L);
        assertThrows(ResponseStatusException.class, () -> gameService.getPlayerInGame(testGame.getGameId(), player.getUserId()));
        Mockito.verify(gameStorage, Mockito.times(1)).getGameById(testGame.getGameId());
    }

    @Test
    public void getSongCard_success() {
        SongCard songCard = new SongCard();
        songCard.setTitle("title");
        Round round = new Round();
        round.setSongCard(songCard);
        testGame.setCurrentRound(round);
        SongCard getSongCard = gameService.getSongCard(testGame.getGameId());
        assertEquals(songCard, getSongCard);
    }

    @Test
    public void createGame_success() {
        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setLobbyName("testGame");
        Mockito.when(lobbyService.getLobbyById(lobby.getLobbyId())).thenReturn(lobby);
        Game createdGame = gameService.createGame(lobby.getLobbyId());
        assertEquals(createdGame.getGameId(), lobby.getLobbyId());
        assertEquals(createdGame.getGameName(), lobby.getLobbyName());
        Mockito.verify(gameStorage, Mockito.times(1)).addGame(Mockito.any());
        Mockito.verify(lobbyService, Mockito.times(1)).getLobbyById(lobby.getLobbyId());
    }

    @Test
    public void startNewRound_success() {
        SongCard songCard = new SongCard();
        songCard.setTitle("title");
        Mockito.when(apiHandler.getNewSongCard()).thenReturn(songCard);
        Game game = gameService.startNewRound(testGame);
        assertEquals(game.getGameId(), testGame.getGameId());
        assertEquals(songCard, game.getCurrentRound().getSongCard());
        Mockito.verify(apiHandler, Mockito.times(1)).getNewSongCard();
    }

    @Test
    public void addCoinToPlayer_success() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(2);

        testGame.setPlayers(List.of(player));

        Player result = gameService.addCoinToPlayer(testGame.getGameId(), player.getUserId());

        assertEquals(3, result.getCoinBalance());
    }

    @Test
    public void addCoinToPlayer_coinBalance5_doesNotAddCoin() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(5); // 5 is the maximum amount of coins a player can have

        testGame.setPlayers(List.of(player));

        Player result = gameService.addCoinToPlayer(testGame.getGameId(), player.getUserId());

        assertEquals(5, result.getCoinBalance());
    }

    @Test
    public void insertSongCardIntoTimeline_success() {
        Player player = new Player();
        player.setUserId(1L);
        player.setTimeline(new ArrayList<>());

        SongCard songCard = new SongCard();
        songCard.setTitle("Disorder");
        songCard.setArtist("Joy Division");
        songCard.setYear(1979);
        songCard.setSongURL("https://blablabla.com");

        testGame.setPlayers(List.of(player));

        int position = 0;

        Player result = gameService.insertSongCardIntoTimeline(testGame.getGameId(), player.getUserId(), songCard, position);

        assertEquals(1, result.getTimeline().size());
        assertEquals("Disorder", result.getTimeline().get(position).getTitle());
        assertEquals("Joy Division", result.getTimeline().get(position).getArtist());
        assertEquals(1979, result.getTimeline().get(position).getYear());
        assertEquals("https://blablabla.com", result.getTimeline().get(position).getSongURL());
    }

    @Test
    public void checkGuess_correctGuess_addsCoin() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(2);

        SongCard songCard = new SongCard();
        songCard.setTitle("Disorder");
        songCard.setArtist("Joy Division");
        songCard.setYear(1979);
        songCard.setSongURL("https://blablabla.com");

        Round round = new Round();
        round.setSongCard(songCard);

        testGame.setCurrentRound(round);
        testGame.setPlayers(List.of(player));

        // Note that the guess is correct
        Guess guess = new Guess();
        guess.setGuessedTitle("Disorder");
        guess.setGuessedArtist("Joy Division");

        // Therefore checkGuess should return True and a coin should be added
        boolean result = gameService.checkGuess(testGame, guess, player.getUserId());

        assertTrue(result);
        assertEquals(3, player.getCoinBalance());
    }

    @Test
    public void checkGuess_wrongGuess_doesNotAddCoin() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(2);

        SongCard songCard = new SongCard();
        songCard.setTitle("Disorder");
        songCard.setArtist("Joy Division");
        songCard.setYear(1979);
        songCard.setSongURL("https://blablabla.com");

        Round round = new Round();
        round.setSongCard(songCard);

        testGame.setCurrentRound(round);
        testGame.setPlayers(List.of(player));

        // Here the player guessed the correct band, but the wrong song
        Guess guess = new Guess();
        guess.setGuessedTitle("Transmission");
        guess.setGuessedArtist("Joy Division");

        // Therefore, the entire guess is false, so checkGuess should return false and no coin is added
        boolean result = gameService.checkGuess(testGame, guess, player.getUserId());

        assertFalse(result);
        assertEquals(2, player.getCoinBalance());
    }

    @Test
    public void buySongCard_enoughCoins_success() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(3); // 3 coins, so player is able to buy a card
        player.setTimeline(new ArrayList<>());

        testGame.setPlayers(List.of(player));

        Player result = gameService.buySongCard(testGame.getGameId(), player.getUserId());

        // Assert: Coin balance decreased and one default song added
        assertEquals(0, result.getCoinBalance(), "Expected player to have spent 3 coins");

        assertEquals(1, result.getTimeline().size(), "Expected timeline to have 1 song");
        SongCard card = result.getTimeline().get(0);
        assertEquals("default", card.getTitle(), "Expected default song title");
        assertTrue(card.getYear() >= 1950 && card.getYear() <= 2025, "Expected year between 1950 and 2025");
    }

    @Test
    public void buySongCard_notEnoughCoins_throwsException() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(2); // not enough coins to buy a songCard
        player.setTimeline(new ArrayList<>());

        testGame.setPlayers(List.of(player));

        // Assert that an exception is thrown when trying to buy a song card (from Player.java, line 50)
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            gameService.buySongCard(testGame.getGameId(), player.getUserId());
        });

        assertEquals("Not enough coins to buy a song card", exception.getMessage());
        assertEquals(2, player.getCoinBalance(), "Coin balance should remain unchanged");
        assertEquals(0, player.getTimeline().size(), "Timeline should remain empty");
    }


}

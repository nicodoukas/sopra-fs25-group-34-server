package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.GameStorage;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
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
        player.setCoinBalance(5);

        testGame.setPlayers(List.of(player));

        Player result = gameService.addCoinToPlayer(testGame.getGameId(), player.getUserId());

        assertEquals(5, result.getCoinBalance());
    }

    @Test
    public void insertSongCardIntoTimeline_whenTimelineEmpty_success() {
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
    public void insertSongCardIntoTimeline_whenTimelinePopulated_success() {
        Player player = new Player();
        player.setUserId(1L);

        SongCard firstSongCard = new SongCard();
        firstSongCard.setTitle("Song 1");
        firstSongCard.setArtist("Artist 1");
        firstSongCard.setYear(1970);
        firstSongCard.setSongURL("https://song1.com");

        SongCard secondSongCard = new SongCard();
        secondSongCard.setTitle("Song 2");
        secondSongCard.setArtist("Blur");
        secondSongCard.setYear(1994);
        secondSongCard.setSongURL("https://song2.com");

        player.setTimeline(new ArrayList<>(List.of(firstSongCard, secondSongCard)));
        testGame.setPlayers(List.of(player));

        SongCard newSongCard = new SongCard();
        newSongCard.setTitle("SongInsert");
        newSongCard.setArtist("ArtistInsert");
        newSongCard.setYear(1979);
        newSongCard.setSongURL("https://songinsert.com");

        int position = 1;

        Player result = gameService.insertSongCardIntoTimeline(testGame.getGameId(), player.getUserId(), newSongCard, position);

        assertEquals(3, result.getTimeline().size());
        assertEquals("Song 1", result.getTimeline().get(0).getTitle());
        assertEquals("SongInsert", result.getTimeline().get(1).getTitle());
        assertEquals("Song 2", result.getTimeline().get(2).getTitle());

        assertEquals(1970, result.getTimeline().get(0).getYear());
        assertEquals(1979, result.getTimeline().get(1).getYear());
        assertEquals(1994, result.getTimeline().get(2).getYear());
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

        Guess guess = new Guess();
        guess.setGuessedTitle("Disorder");
        guess.setGuessedArtist("Joy Division");

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

        Guess guess = new Guess();
        guess.setGuessedTitle("Transmission");
        guess.setGuessedArtist("Joy Division");

        boolean result = gameService.checkGuess(testGame, guess, player.getUserId());

        assertFalse(result);
        assertEquals(2, player.getCoinBalance());
    }

    @Test
    public void buySongCard_enoughCoins_success() {
        Player player = new Player();
        player.setUserId(1L);
        player.setCoinBalance(3);
        player.setTimeline(new ArrayList<>());

        testGame.setPlayers(List.of(player));

        Player result = gameService.buySongCard(testGame.getGameId(), player.getUserId());

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
        player.setCoinBalance(2);
        player.setTimeline(new ArrayList<>());

        testGame.setPlayers(List.of(player));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            gameService.buySongCard(testGame.getGameId(), player.getUserId());
        });

        assertEquals("Not enough coins to buy a song card", exception.getMessage());
        assertEquals(2, player.getCoinBalance(), "Coin balance should remain unchanged");
        assertEquals(0, player.getTimeline().size(), "Timeline should remain empty");
    }

    @Test
    public void leaveOrDeleteGame_hostDeletesGame_success() {
        User hostUser = new User();
        hostUser.setId(1L);
        hostUser.setStatus(UserStatus.PLAYING);

        Player hostPlayer = new Player();
        hostPlayer.setUserId(1L);

        Player otherPlayer = new Player();
        otherPlayer.setUserId(2L);

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setStatus(UserStatus.PLAYING);

        List<Player> players = List.of(hostPlayer, otherPlayer);
        testGame.setPlayers(players);
        testGame.setHost(hostPlayer);

        Mockito.when(userService.getUserById(1L)).thenReturn(hostUser);
        Mockito.when(userService.getUserById(2L)).thenReturn(otherUser);

        gameService.leaveOrDeleteGame(testGame.getGameId(), hostUser.getId());

        Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any(User.class));
        Mockito.verify(gameStorage, Mockito.times(1)).deleteGame(testGame.getGameId());
        assertEquals(UserStatus.ONLINE, hostUser.getStatus());
        assertEquals(UserStatus.ONLINE, otherUser.getStatus());
    }

    @Test
    public void leaveOrDeleteGame_playerLeavesGame_success() {
        User hostUser = new User();
        hostUser.setId(1L);
        hostUser.setStatus(UserStatus.PLAYING);

        Player hostPlayer = new Player();
        hostPlayer.setUserId(1L);

        Player leavingPlayer = new Player();
        leavingPlayer.setUserId(2L);

        User leavingUser = new User();
        leavingUser.setId(2L);
        leavingUser.setStatus(UserStatus.PLAYING);

        List<Player> players = new ArrayList<>(List.of(hostPlayer, leavingPlayer));
        testGame.setPlayers(players);
        testGame.setHost(hostPlayer);

        Round round = new Round();
        round.setActivePlayer(hostPlayer);
        testGame.setCurrentRound(round);

        Mockito.when(userService.getUserById(1L)).thenReturn(hostUser);
        Mockito.when(userService.getUserById(2L)).thenReturn(leavingUser);

        gameService.leaveOrDeleteGame(testGame.getGameId(), leavingUser.getId());

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(gameStorage, Mockito.never()).deleteGame(Mockito.anyLong());
        assertEquals(UserStatus.ONLINE, leavingUser.getStatus());
        assertNull(leavingUser.getLobbyId());
        assertFalse(testGame.getPlayers().contains(leavingPlayer));
    }


    @Test
    public void updateActivePlayerPlacement_success() {
        Round round = new Round();
        round.setActivePlayerPlacement(0);
        testGame.setCurrentRound(round);

        Game updatedGame = gameService.updateActivePlayerPlacement(testGame, 2);
        assertEquals(2, updatedGame.getCurrentRound().getActivePlayerPlacement());
    }

    @Test
    public void updateChallengerPlacement_success() {
        Round round = new Round();
        round.setChallengerPlacement(0);
        testGame.setCurrentRound(round);

        Game updatedGame = gameService.updateChallengerPlacement(testGame, 1);
        assertEquals(1, updatedGame.getCurrentRound().getChallengerPlacement());
    }

    @Test
    public void declinesChallenge_allPlayersDeclined_returnsTrue() {
        Player activePlayer = new Player();
        activePlayer.setUserId(1L);

        Player challenger1 = new Player();
        challenger1.setUserId(2L);

        Player challenger2 = new Player();
        challenger2.setUserId(3L);

        List<Player> players = List.of(activePlayer, challenger1, challenger2);
        testGame.setPlayers(players);

        Round round = new Round();
        round.setActivePlayer(activePlayer);
        round.userDeclinesChallenge(2L);

        testGame.setCurrentRound(round);

        boolean result = gameService.declinesChallenge(testGame.getGameId(), 3L);
        assertTrue(result);
    }

    @Test
    public void declinesChallenge_notAllPlayersDeclined_returnsFalse() {
        Player activePlayer = new Player();
        activePlayer.setUserId(1L);

        Player challenger1 = new Player();
        challenger1.setUserId(2L);

        Player challenger2 = new Player();
        challenger2.setUserId(3L);

        List<Player> players = List.of(activePlayer, challenger1, challenger2);
        testGame.setPlayers(players);

        Round round = new Round();
        round.setActivePlayer(activePlayer);

        testGame.setCurrentRound(round);

        boolean result = gameService.declinesChallenge(testGame.getGameId(), 3L);
        assertFalse(result);
    }

}

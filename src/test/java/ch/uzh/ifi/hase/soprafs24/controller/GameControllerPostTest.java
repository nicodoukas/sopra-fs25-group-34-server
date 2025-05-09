package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GuessPostDTO;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.Guess;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerPostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger webSocketMessenger;

    private ObjectMapper objectMapper;

    private Player mockPlayer;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        mockPlayer = new Player();
        mockPlayer.setUserId(1L);
        mockPlayer.setGameId(10L);
        mockPlayer.setUsername("testUsername");
        mockPlayer.setCoinBalance(2);
    }

    @Test
    public void createGame_success() throws Exception {
        Long lobbyId = 1L;

        User hostUser = new User();
        hostUser.setId(1L);
        hostUser.setUsername("HostUser");

        Lobby testLobby = new Lobby();
        testLobby.setLobbyId(lobbyId);
        testLobby.setLobbyName("Test Lobby");
        testLobby.setHost(hostUser);
        testLobby.setMembers(List.of(hostUser));

        Game testGame = new Game();
        testGame.setGameId(lobbyId);
        testGame.setGameName("Test Lobby");
        testGame.setTurnCount(0);

        Mockito.when(gameService.createGame(Mockito.anyLong())).thenReturn(testGame);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId", is(testGame.getGameId().intValue())))
                .andExpect(jsonPath("$.gameName", is(testGame.getGameName())))
                .andExpect(jsonPath("$.turnCount", is(testGame.getTurnCount())));
    }

    @Test
    public void checkGuess_correctGuess_returnsTrue() throws Exception {
        Long gameId = 10L;
        Long userId = 1L;

        SongCard songCard = new SongCard();
        songCard.setTitle("Disorder");
        songCard.setArtist("Joy Division");
        songCard.setYear(1979);
        songCard.setSongURL("https://blablabla.com");

        Round currentRound = new Round();
        currentRound.setSongCard(songCard);

        Game game = new Game();
        game.setGameId(gameId);
        game.setCurrentRound(currentRound);

        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGuessedTitle("Disorder");
        guessPostDTO.setGuessedArtist("Joy Division");
        guessPostDTO.setPlayer(mockPlayer);

        Mockito.when(gameService.getGameById(gameId)).thenReturn(game);
        Mockito.when(gameService.checkGuess(eq(game), any(Guess.class), eq(userId))).thenReturn(true);

        mockMvc.perform(post("/games/{gameId}/{userId}/guess", gameId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guessPostDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true")); // Boolean true as raw string
    }

    @Test
    public void checkGuess_wrongGuess_returnsFalse() throws Exception {
        Long gameId = 10L;
        Long userId = 1L;

        SongCard songCard = new SongCard();
        songCard.setTitle("Disorder");
        songCard.setArtist("Joy Division");
        songCard.setYear(1979);
        songCard.setSongURL("https://blablabla.com");

        Round currentRound = new Round();
        currentRound.setSongCard(songCard);

        Game game = new Game();
        game.setGameId(gameId);
        game.setCurrentRound(currentRound);

        // Wrong guess
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGuessedTitle("Ceremony");
        guessPostDTO.setGuessedArtist("New Order");
        guessPostDTO.setPlayer(mockPlayer);

        Mockito.when(gameService.getGameById(gameId)).thenReturn(game);
        Mockito.when(gameService.checkGuess(eq(game), any(Guess.class), eq(userId))).thenReturn(false);

        mockMvc.perform(post("/games/{gameId}/{userId}/guess", gameId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guessPostDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("false")); // Boolean false as raw string
    }


}

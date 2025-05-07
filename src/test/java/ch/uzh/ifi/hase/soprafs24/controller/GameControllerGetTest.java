package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerGetTest {

    private static final Logger log = LoggerFactory.getLogger(GameControllerGetTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private GameService gameService;

    @MockBean
    private ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger webSocketMessenger;

    @MockBean
    private UserService userService;

    private Game game;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        game = new Game();
        game.setGameId(1L);
        game.setGameName("testGame");
        game.setTurnCount(10);
        game.setCurrentRound(new Round());
        game.setHost(new Player());
        Queue<Player> turnOrder = new LinkedList<>();
        turnOrder.add(new Player());
        turnOrder.add(new Player());
        turnOrder.add(new Player());
        game.setTurnOrder(turnOrder);
        game.setPlayers(List.of(new Player(), new Player()));

    }

    // GET /games/{gameId} success
    @Test
    public void getGame_validId_success() throws Exception {

        given(gameService.getGameById(1L)).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameName", is("testGame")))
                .andExpect(jsonPath("$.turnCount", is(10)))
                .andExpect(jsonPath("$.currentRound").exists())
                .andExpect(jsonPath("$.host").exists())
                .andExpect(jsonPath("$.turnOrder", hasSize(3)))
                .andExpect(jsonPath("$.players", hasSize(2)));
    }

    // GET /games/{gameId} error
    @Test
    public void getGame_invalidId_throwsNotFound() throws Exception {
        given(gameService.getGameById(123L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        MockHttpServletRequestBuilder getRequest = get("/games/123")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    // GET /games/{gameId}/{userId} success
    @Test
    public void getPlayerInGame_validUserId_success() throws Exception {
        Player player = new Player();
        player.setUserId(1L);

        given(gameService.getPlayerInGame(game.getGameId(), 1L)).willReturn(player);

        MockHttpServletRequestBuilder getRequest = get("/games/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)));
    }

    // GET /games/{gameId}/{userId} error
    @Test
    public void getPlayerInGame_invalidUserId_throwsNotFound() throws Exception {
        given(gameService.getPlayerInGame(1L, 123L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with userId 123 not found in gameId 1"));

        MockHttpServletRequestBuilder getRequest = get("/games/1/123")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    // GET /games/{gameId}/song
    @Test
    public void getSongCard_success() throws Exception {

        SongCard songCard = new SongCard();
        songCard.setTitle("title");

        given(gameService.getSongCard(1L)).willReturn(songCard);

        MockHttpServletRequestBuilder getRequest = get("/games/1/song")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("title")));

    }

}

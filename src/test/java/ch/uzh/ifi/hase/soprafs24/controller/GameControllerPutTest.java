package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerPutTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger webSocketMessenger;

    private ObjectMapper objectMapper;

    private Player mockPlayer;
    private SongCard mockSongCard;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        mockSongCard = new SongCard();
        mockSongCard.setTitle("Disorder");
        mockSongCard.setArtist("Joy Division");
        mockSongCard.setYear(1979);
        mockSongCard.setSongURL("https://blablabla.com");

        mockPlayer = new Player();
        mockPlayer.setUserId(1L);
        mockPlayer.setGameId(10L);
        mockPlayer.setUsername("testUsername");
        mockPlayer.setCoinBalance(2);
        mockPlayer.setTimeline(List.of(mockSongCard)); // We assume it was inserted
    }


    @Test
    public void updatePlayer_InsertSongCardIntoTimeline_success() throws Exception {
        Long gameId = 10L;
        Long userId = 1L;
        int position = 0;

        PlayerPutDTO playerPutDTO = new PlayerPutDTO();
        playerPutDTO.setSongCard(mockSongCard);
        playerPutDTO.setPosition(position);
        playerPutDTO.setAddCoin(false);

        Mockito.when(gameService.insertSongCardIntoTimeline(eq(gameId), eq(userId), any(SongCard.class), eq(position)))
                .thenReturn(mockPlayer);

        mockMvc.perform(put("/games/{gameId}/{userId}", gameId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerPutDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.gameId").value(gameId))
                .andExpect(jsonPath("$.username").value("testUsername"))
                .andExpect(jsonPath("$.coinBalance").value(2))
                .andExpect(jsonPath("$.timeline[0].title").value("Disorder"))
                .andExpect(jsonPath("$.timeline[0].artist").value("Joy Division"))
                .andExpect(jsonPath("$.timeline[0].year").value(1979))
                .andExpect(jsonPath("$.timeline[0].songURL").value("https://blablabla.com"));
    }

    @Test
    public void updatePlayer_AddCoin_success() throws Exception {
        Long gameId = 10L;
        Long userId = 1L;

        // The player starts with 2 coins, with addCoin = True he should have 3 after calling endpoint
        Player updatedPlayer = new Player();
        updatedPlayer.setUserId(userId);
        updatedPlayer.setGameId(gameId);
        updatedPlayer.setUsername("testUsername");
        updatedPlayer.setCoinBalance(3);  // Now set to 3 to simulate GameService adding one
        updatedPlayer.setTimeline(List.of(mockSongCard));

        PlayerPutDTO playerPutDTO = new PlayerPutDTO();
        playerPutDTO.setAddCoin(true);

        Mockito.when(gameService.addCoinToPlayer(gameId, userId)).thenReturn(updatedPlayer);

        mockMvc.perform(put("/games/{gameId}/{userId}", gameId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerPutDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.gameId").value(gameId))
                .andExpect(jsonPath("$.username").value("testUsername"))
                .andExpect(jsonPath("$.coinBalance").value(3))
                .andExpect(jsonPath("$.timeline[0].title").value("Disorder"));
    }

    @Test
    public void buySongCard_success() throws Exception {
        Long gameId = 10L;
        Long userId = 1L;

        SongCard boughtCard = new SongCard();
        boughtCard.setTitle("default");
        boughtCard.setArtist("default");
        boughtCard.setSongURL("default");
        boughtCard.setYear(2000);  // Fixed the year for predictability, other than using Random()

        Player updatedPlayer = new Player();
        updatedPlayer.setUserId(userId);
        updatedPlayer.setGameId(gameId);
        updatedPlayer.setUsername("testUsername");
        updatedPlayer.setCoinBalance(0); // let's assume the player had 3 coins at the start
        updatedPlayer.setTimeline(List.of(mockSongCard, boughtCard));

        Mockito.when(gameService.buySongCard(gameId, userId)).thenReturn(updatedPlayer);

        mockMvc.perform(put("/games/{gameId}/buy", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId)))
                .andExpect(status( ).isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.coinBalance").value(0))
                .andExpect(jsonPath("$.timeline.length()").value(2))
                .andExpect(jsonPath("$.timeline[1].title").value("default"))
                .andExpect(jsonPath("$.timeline[1].artist").value("default"))
                .andExpect(jsonPath("$.timeline[1].songURL").value("default"))
                .andExpect(jsonPath("$.timeline[1].year").value(2000));
    }

}

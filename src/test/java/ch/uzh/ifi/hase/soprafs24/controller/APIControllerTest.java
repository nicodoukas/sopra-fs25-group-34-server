package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.service.APIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIController.class)
@ActiveProfiles("test")
public class APIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIService apiService;

    @MockBean
    private PictureRepository pictureRepository;

    @Test
    public void getSongById_success() throws Exception {
        SongCard songCard = new SongCard();
        songCard.setTitle("Dramamine");
        songCard.setArtist("Modest Mouse");
        songCard.setYear(1996);
        songCard.setSongURL("https://test.com");

        given(apiService.getSongById("1")).willReturn(songCard);

        mockMvc.perform(get("/api/songs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Dramamine")))
                .andExpect(jsonPath("$.artist", is("Modest Mouse")))
                .andExpect(jsonPath("$.year", is(1996)))
                .andExpect(jsonPath("$.songURL", is("https://test.com")));
    }

    @Test
    public void getSongById_notFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found"))
                .when(apiService).getSongById("123");

        mockMvc.perform(get("/api/songs/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSongsOfPlaylist_success() throws Exception {
        List<String> songIds = Arrays.asList("1", "2", "3");
        given(apiService.getSongsOfPlaylist("testPlaylist")).willReturn(songIds);

        mockMvc.perform(get("/api/playlists/testPlaylist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("1")))
                .andExpect(jsonPath("$[1]", is("2")))
                .andExpect(jsonPath("$[2]", is("3")));
    }

    @Test
    public void getSongsOfPlaylist_notFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found"))
                .when(apiService).getSongsOfPlaylist("invalidPlaylist");

        mockMvc.perform(get("/api/playlists/invalidPlaylist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SongCardGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.APIService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class APIController {

    private final APIService apiService;

    public APIController(APIService apiService) {this.apiService = apiService;}

    @GetMapping("/api/songs/{songId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SongCardGetDTO getSong(@PathVariable String songId) {

        SongCard song = apiService.getSongById(songId);
        return DTOMapper.INSTANCE.convertEntityToSongCardGetDTO(song);
    }

    @GetMapping("/api/playlists/{playlistId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getSongsOfPlaylist(@PathVariable String playlistId) {

        List<String> songs = apiService.getSongsOfPlaylist(playlistId);
        return songs;
    }
}

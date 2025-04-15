package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class APIHandler {

    private final APIService apiService;

    // TODO: find the actual IDs of playlists we want to include
    private static final List<String> PLAYLIST_IDS = List.of("playlist1ID", "playlist2ID", "playlist3ID");

    @Autowired
    public APIHandler(APIService apiService) {
        this.apiService = apiService;
    }

    public SongCard getNewSongCard() {
        String playlistId = getRandomElement(PLAYLIST_IDS);
        List<String> songIds = apiService.getSongsOfPlaylist(playlistId);
        String songId = getRandomElement(songIds);
        return apiService.getSongById(songId);
    }

    private static <T> T getRandomElement(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

}

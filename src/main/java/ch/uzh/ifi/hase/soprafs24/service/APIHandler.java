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

    private static final List<String> PLAYLIST_IDS = List.of("pl.4c4185db922342f1bc36e0817eec213a", "pl.405e6f67264a4a44ba1b0c3a787c78b8", "pl.1745c21b5f084936ad637b4cd5cbd99a", "pl.af4d982795c6472ea48579eb147cd726", "pl.0d70b7c9be8e4e0b95ebbf5578aaf7a2", "pl.e50ccee7318043eaaf8e8e28a2a55114", "pl.6b1b5dfda067443481265436811002f1", "pl.adc231109c184fc691d8b78bf28217de" );

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

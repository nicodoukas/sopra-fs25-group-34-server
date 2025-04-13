package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.GameStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameStorage gameStorage;
    private final UserRepository userRepository;

    @Autowired
    public GameService(GameStorage gameStorage, UserRepository userRepository) {
        this.gameStorage = gameStorage;
        this.userRepository = userRepository;
    }

    public Game getGameById(Long gameId){
        return gameStorage.getGameById(gameId);
    }

    public Player getPlayerInGame(Long gameId, Long userId) {
        Game game = gameStorage.getGameById(gameId);

        return game.getPlayers()
                .stream()
                .filter(player -> player.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with userId xyz not found in this game"));
    }

    public SongCard getSongCard(Long gameId) {
        Game game = getGameById(gameId);
        return game.getCurrentRound().getSongCard(); //get SongCard from currentRound
    }
}

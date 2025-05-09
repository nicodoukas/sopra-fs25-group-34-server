package ch.uzh.ifi.hase.soprafs24.storage;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class GameStorage {

    private final Map<Long, Game> games = new ConcurrentHashMap<>();

    public Game addGame(Game game) {
        if (game == null || game.getGameId() == null) {
            throw new IllegalArgumentException("Game or gameId cannot be null");
        }
        games.put(game.getGameId(), game);
        return game;
    }

    public Game getGameById(Long gameId) {
        if (!games.containsKey(gameId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found");
        }
        return games.get(gameId);
    }

    public void deleteGame(Long gameId) {
        if (!games.containsKey(gameId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found");
        }
        games.remove(gameId);
    }
}


package ch.uzh.ifi.hase.soprafs24.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class PlayerStorage {

    private final Map<Long, Player> players = new ConcurrentHashMap<>();

    public Player addPlayer(Player player) {
        if (player == null || player.getUserId() == null) {
            throw new IllegalArgumentException("Player or UserId cannot be null");
        }
        players.put(player.getUserId(), player);
        return player;
    }

    public Player getPlayerById(Long userId) {
        if (!players.containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with ID " + userId + " not found");
        }
        return players.get(userId);
    }

}

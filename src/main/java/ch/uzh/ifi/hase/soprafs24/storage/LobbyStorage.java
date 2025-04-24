package ch.uzh.ifi.hase.soprafs24.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class LobbyStorage {

    private final Map<Long, Lobby> lobbies = new ConcurrentHashMap<>();

    public Lobby addLobby(Lobby lobby) {
        if (lobby == null || lobby.getLobbyId() == null) {
            throw new IllegalArgumentException("Lobby or lobbyId cannot be null");
        }
        lobbies.put(lobby.getLobbyId(), lobby);
        return lobby;
    }

    public void removeLobby(Long lobbyId) {
        if (!lobbies.containsKey(lobbyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot remove: Lobby with ID " + lobbyId + " not found");
        }
        lobbies.remove(lobbyId);
    }


    public Lobby getLobbyById(Long lobbyId) {
        if (!lobbies.containsKey(lobbyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with ID " + lobbyId + " not found");
        }
        return lobbies.get(lobbyId);
    }

}

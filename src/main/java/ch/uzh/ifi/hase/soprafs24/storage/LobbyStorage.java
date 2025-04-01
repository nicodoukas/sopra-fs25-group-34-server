package ch.uzh.ifi.hase.soprafs24.storage;

import org.springframework.stereotype.Component;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class LobbyStorage {

    private final Map<Long, Lobby> lobbies = new ConcurrentHashMap<>();

    public Lobby addLobby(Lobby lobby) {
        lobbies.put(lobby.getLobbyId(), lobby);
        return lobby;
    }

    public Lobby getLobbyBId(Long lobbyId) {
        return lobbies.get(lobbyId);
    }

}

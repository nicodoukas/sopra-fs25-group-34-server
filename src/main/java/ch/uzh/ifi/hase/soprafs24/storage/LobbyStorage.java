package ch.uzh.ifi.hase.soprafs24.storage;

import org.springframework.stereotype.Component;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;

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

    public Lobby getLobbyBId(Long lobbyId) {
        if (!lobbies.containsKey(lobbyId)) {
            System.out.println("Lobby with ID " + lobbyId + " does not exist in storage.");
            // TODO @monolino maybe throw an error
        }
        return lobbies.get(lobbyId);
    }

}

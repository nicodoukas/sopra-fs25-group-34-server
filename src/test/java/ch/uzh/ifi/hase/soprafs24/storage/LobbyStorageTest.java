package ch.uzh.ifi.hase.soprafs24.storage;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyStorageTest {

    private LobbyStorage lobbyStorage;

    @BeforeEach
    public void setup() {
        lobbyStorage = new LobbyStorage();
    }

    @Test
    public void addLobby_success() {
        Lobby lobby = new Lobby();
        lobby.createLobbyId();
        lobby.setLobbyName("Test Lobby");

        Lobby addedLobby = lobbyStorage.addLobby(lobby);

        assertNotNull(addedLobby);
        assertEquals("Test Lobby", addedLobby.getLobbyName());
        assertEquals(lobby.getLobbyId(), addedLobby.getLobbyId());
    }

    @Test
    public void addLobby_nullLobbyId_throwsException() {
        Lobby lobby = new Lobby();

        // No lobbyId
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lobbyStorage.addLobby(lobby);
        });
        assertEquals("Lobby or lobbyId cannot be null", exception.getMessage());
    }

    // Not sure if this test makes sense even, since we never should have a lobby without host, but anyway
    @Test
    public void getLobbyById_success() {
        Lobby lobby = new Lobby();
        lobby.createLobbyId();
        lobby.setLobbyName("Test Lobby");

        lobbyStorage.addLobby(lobby);
        Lobby foundLobby = lobbyStorage.getLobbyById(lobby.getLobbyId());

        assertNotNull(foundLobby);
        assertEquals("Test Lobby", foundLobby.getLobbyName());
    }

    @Test
    public void getLobbyById_withHost_success() {

        User host = new User();
        host.setId(1L);
        host.setUsername("HostUser");

        Lobby lobby = new Lobby();
        lobby.createLobbyId();
        lobby.setLobbyName("Test Lobby");
        lobby.setHost(host);

        lobbyStorage.addLobby(lobby);

        Lobby foundLobby = lobbyStorage.getLobbyById(lobby.getLobbyId());

        assertNotNull(foundLobby);
        assertEquals("Test Lobby", foundLobby.getLobbyName());

        assertNotNull(foundLobby.getHost());
        assertEquals("HostUser", foundLobby.getHost().getUsername());

        assertEquals(1, foundLobby.getMembers().size());
        assertEquals("HostUser", foundLobby.getMembers().get(0).getUsername());
    }


    @Test
    public void getLobbyById_notFound_throwsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyStorage.getLobbyById(123L);
        });
        assertEquals("404 NOT_FOUND \"Lobby with ID 123 not found\"", exception.getMessage());
    }

    @Test
    public void deleteLobby_success() {
        Lobby lobby = new Lobby();
        lobby.createLobbyId();
        lobby.setLobbyName("Test Lobby");

        lobbyStorage.addLobby(lobby);
        lobbyStorage.deleteLobby(lobby.getLobbyId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyStorage.getLobbyById(lobby.getLobbyId());
        });
        assertEquals("404 NOT_FOUND \"Lobby with ID " + lobby.getLobbyId() + " not found\"", exception.getMessage());
    }

    @Test
    public void deleteLobby_notFound_throwsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyStorage.deleteLobby(123L);
        });
        assertEquals("404 NOT_FOUND \"Lobby with ID 123 not found\"", exception.getMessage());
    }
}

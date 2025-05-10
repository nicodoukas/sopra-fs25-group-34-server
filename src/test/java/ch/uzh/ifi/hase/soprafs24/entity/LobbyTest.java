package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {

    @Test
    public void testSetLobbyId() {
        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);
        assertEquals(1L, lobby.getLobbyId());
    }

    @Test
    public void testCreateLobbyId() {
        Lobby lobby1 = new Lobby();
        lobby1.createLobbyId();
        assertNotNull(lobby1.getLobbyId());

        // quick additional check that created IDs of 2 different lobbies aren't equal
        Lobby lobby2 = new Lobby();
        lobby2.createLobbyId();
        assertNotEquals(lobby1.getLobbyId(), lobby2.getLobbyId());
    }

    @Test
    public void testSetLobbyName() {
        Lobby lobby = new Lobby();
        lobby.setLobbyName("testLobby");
        assertEquals("testLobby", lobby.getLobbyName());
    }

    @Test
    public void testSetMembers() {
        Lobby lobby = new Lobby();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("User1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("User2");

        List<User> members = new ArrayList<>();
        members.add(user1);
        members.add(user2);

        lobby.setMembers(members);

        assertEquals(2, lobby.getMembers().size());
        assertEquals("User1", lobby.getMembers().get(0).getUsername());
        assertEquals("User2", lobby.getMembers().get(1).getUsername());
    }

    @Test
    public void testSetLobbyHost() {
        Lobby lobby = new Lobby();
        User host = new User();
        host.setId(1L);
        host.setUsername("HostUser");

        lobby.setHost(host);

        assertNotNull(lobby.getHost());
        assertEquals("HostUser", lobby.getHost().getUsername());
        assertEquals(1, lobby.getMembers().size());
        assertEquals(host, lobby.getMembers().get(0));
    }
}

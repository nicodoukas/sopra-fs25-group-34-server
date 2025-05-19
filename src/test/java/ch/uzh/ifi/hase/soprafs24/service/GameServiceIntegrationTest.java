package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class GameServiceIntegrationTest {

    private Game testGame;
    private APIHandler apiHandler;

    @BeforeEach
    public void setUp() {
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("Test Game");
        apiHandler = Mockito.mock(APIHandler.class);
    }

    @Test
    public void createPlayers_success() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("User1");
        ProfilePicture pfp1 = new ProfilePicture();
        user1.setProfilePicture(pfp1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("User2");
        ProfilePicture pfp2 = new ProfilePicture();
        user2.setProfilePicture(pfp2);

        List<User> users = List.of(user1, user2);

        SongCard songCard1 = new SongCard();
        songCard1.setTitle("Song 1");
        songCard1.setYear(1970);

        SongCard songCard2 = new SongCard();
        songCard2.setTitle("Song 2");
        songCard2.setYear(1980);

        when(apiHandler.getNewSongCard()).thenReturn(songCard1, songCard2);

        List<Player> players = testGame.createPlayers(users, apiHandler);

        assertEquals(2, players.size());

        Player player1 = players.get(0);
        assertEquals(1L, player1.getUserId());
        assertEquals("User1", player1.getUsername());
        assertEquals(pfp1, player1.getProfilePicture());
        assertEquals(songCard1, player1.getTimeline().get(0));

        Player player2 = players.get(1);
        assertEquals(2L, player2.getUserId());
        assertEquals("User2", player2.getUsername());
        assertEquals(pfp2, player2.getProfilePicture());
        assertEquals(songCard2, player2.getTimeline().get(0));

        verify(apiHandler, times(2)).getNewSongCard();
    }

    @Test
    public void leaveGame_playerLeaves_success() {
        Player player1 = new Player();
        player1.setUserId(1L);

        Player player2 = new Player();
        player2.setUserId(2L);

        Player player3 = new Player();
        player3.setUserId(3L);

        testGame.setPlayers(new ArrayList<>(List.of(player1, player2, player3)));

        Queue<Player> turnOrder = new LinkedList<>(List.of(player1, player2, player3));
        testGame.setTurnOrder(turnOrder);

        User userToLeave = new User();
        userToLeave.setId(2L);

        testGame.leaveGame(userToLeave);

        assertEquals(2, testGame.getPlayers().size());
        assertFalse(testGame.getPlayers().stream().anyMatch(p -> p.getUserId().equals(2L)));

        Queue<Player> updatedTurnOrder = testGame.getTurnOrder();
        assertEquals(2, updatedTurnOrder.size());
        assertFalse(updatedTurnOrder.stream().anyMatch(p -> p.getUserId().equals(2L)));
        assertEquals(1L, updatedTurnOrder.poll().getUserId());
        assertEquals(3L, updatedTurnOrder.poll().getUserId());
    }

}

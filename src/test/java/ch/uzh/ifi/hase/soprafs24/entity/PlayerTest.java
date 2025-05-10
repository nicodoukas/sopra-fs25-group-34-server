package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testSetUserId() {
        Player player = new Player();
        player.setUserId(1L);
        assertEquals(1L, player.getUserId());
    }

    @Test
    public void testSetGameId() {
        Player player = new Player();
        player.setGameId(1L);
        assertEquals(1L, player.getGameId());
    }

    @Test
    public void testSetCoinBalance() {
        Player player = new Player();
        player.setCoinBalance(2);
        assertEquals(2, player.getCoinBalance());
    }

    @Test
    public void testSetUsername() {
        Player player = new Player();
        player.setUsername("testPlayer");
        assertEquals("testPlayer", player.getUsername());
    }

    @Test
    public void testSetTimeline() {
        Player player = new Player();
        SongCard song1 = new SongCard();
        song1.setTitle("Song1");

        SongCard song2 = new SongCard();
        song2.setTitle("Song2");

        List<SongCard> timeline = new ArrayList<>();
        timeline.add(song1);
        timeline.add(song2);

        player.setTimeline(timeline);

        assertEquals(2, player.getTimeline().size());
        assertEquals("Song1", player.getTimeline().get(0).getTitle());
        assertEquals("Song2", player.getTimeline().get(1).getTitle());
    }

    @Test
    public void testSetProfilePicture() {
        Player player = new Player();
        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setUrl("https://exampleimage.com");

        player.setProfilePicture(profilePicture);

        assertNotNull(player.getProfilePicture());
        assertEquals("https://exampleimage.com", player.getProfilePicture().getUrl());
    }
}

package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuessTest {

    @Test
    public void testSetGuessedTitle() {
        Guess guess = new Guess();
        guess.setGuessedTitle(" Atmosphere "); // adding extra spaces to check trim method
        assertEquals("Atmosphere", guess.getGuessedTitle());
    }

    @Test
    public void testSetGuessedTitleNull() {
        Guess guess = new Guess();
        guess.setGuessedTitle(null);
        assertNull(guess.getGuessedTitle());
    }

    @Test
    public void testSetGuessedArtist() {
        Guess guess = new Guess();
        guess.setGuessedArtist(" Joy Division ");
        assertEquals("Joy Division", guess.getGuessedArtist());
    }

    @Test
    public void testSetGuessedArtistNull() {
        Guess guess = new Guess();
        guess.setGuessedArtist(null);
        assertNull(guess.getGuessedArtist());
    }

    @Test
    public void testSetPlayer() {
        Guess guess = new Guess();
        Player player = new Player();
        player.setUserId(1L);
        player.setUsername("Player1");

        guess.setPlayer(player);
        assertNotNull(guess.getPlayer());
        assertEquals(1L, guess.getPlayer().getUserId());
        assertEquals("Player1", guess.getPlayer().getUsername());
    }
}

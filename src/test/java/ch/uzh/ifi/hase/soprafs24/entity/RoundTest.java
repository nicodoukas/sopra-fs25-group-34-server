package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class RoundTest {

    @Test
    public void testSetActivePlayer() {
        Round round = new Round();
        Player player = new Player();
        player.setUsername("Player1");

        round.setActivePlayer(player);
        assertEquals(player, round.getActivePlayer());
    }

    @Test
    public void testSetSongCard() {
        Round round = new Round();
        SongCard songCard = new SongCard();
        songCard.setTitle("Nutshell");
        songCard.setArtist("Alice in Chains");
        songCard.setYear(1994);
        songCard.setSongURL("https://test.com");

        round.setSongCard(songCard);
        assertEquals(songCard, round.getSongCard());
        assertEquals("Alice in Chains", round.getSongCard().getArtist());
    }

    @Test
    public void testSetRoundNr() {
        Round round = new Round();
        round.setRoundNr(5);
        assertEquals(5, round.getRoundNr());
    }

    @Test
    public void testSetActivePlayerPlacement() {
        Round round = new Round();
        round.setActivePlayerPlacement(1);
        assertEquals(1, round.getActivePlayerPlacement());
    }

    @Test
    public void testSetChallengerPlacement() {
        Round round = new Round();
        round.setChallengerPlacement(2);
        assertEquals(2, round.getChallengerPlacement());
    }

    @Test
    public void testSetChallenger() {
        Round round = new Round();
        Player challenger = new Player();
        challenger.setUsername("Challenger");

        round.setChallenger(challenger);
        assertEquals(challenger, round.getChallenger());
    }

    @Test
    public void testSetPreviewURL() {
        Round round = new Round();
        round.setPreviewURL("https://testtest.com");
        assertEquals("https://testtest.com", round.getPreviewURL());
    }
}

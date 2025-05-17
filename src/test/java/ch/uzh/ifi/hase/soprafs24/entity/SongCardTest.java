package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class SongCardTest {

    @Test
    public void testSetTitle() {
        SongCard songCard = new SongCard();
        songCard.setTitle("Decades");
        assertEquals("Decades", songCard.getTitle());
    }

    @Test
    public void testSetArtist() {
        SongCard songCard = new SongCard();
        songCard.setArtist("Joy Division");
        assertEquals("Joy Division", songCard.getArtist());
    }

    @Test
    public void testSetYear() {
        SongCard songCard = new SongCard();
        songCard.setYear(1980);
        assertEquals(1980, songCard.getYear());
    }

    @Test
    public void testSetSongURL() {
        SongCard songCard = new SongCard();
        songCard.setSongURL("https://xaxaxa.com");
        assertEquals("https://xaxaxa.com", songCard.getSongURL());
    }

    @Test
    public void testDefaultTitle() {
        SongCard songCard = new SongCard();
        assertEquals("defaultSongCard", songCard.getTitle());
    }
}

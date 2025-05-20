package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfilePictureTest {

    @Test
    public void testSetId() {
        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setId(1L);
        assertEquals(1L, profilePicture.getId());
    }

    @Test
    public void testSetUrl() {
        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setUrl("https://testImage.com");
        assertEquals("https://testImage.com", profilePicture.getUrl());
    }

    @Test
    public void testConstructor() {
        ProfilePicture profilePicture = new ProfilePicture("https://testImage2.com");
        assertEquals("https://testImage2.com", profilePicture.getUrl());
    }
}

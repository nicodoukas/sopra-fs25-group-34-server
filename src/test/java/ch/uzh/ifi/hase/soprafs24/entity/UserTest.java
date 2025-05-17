package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class UserTest {

    @Test
    public void testSetId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    public void testSetUsername() {
        User user = new User();
        user.setUsername("testUser");
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword("testPassword");
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testSetToken() {
        User user = new User();
        user.setToken("testToken");
        assertEquals("testToken", user.getToken());
    }

    @Test
    public void testSetStatus() {
        User user = new User();
        user.setStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, user.getStatus());
    }

    @Test
    public void testSetCreationDate() {
        User user = new User();
        Date creationDate = new Date();
        user.setCreation_date(creationDate);
        assertEquals(creationDate, user.getCreation_date());
    }

    @Test
    public void testSetBirthday() {
        User user = new User();
        Date birthday = new Date();
        user.setBirthday(birthday);
        assertEquals(birthday, user.getBirthday());
    }

    @Test
    public void testSetFriends() {
        User user = new User();
        List<Long> friends = new ArrayList<>();
        friends.add(1L);
        friends.add(2L);
        user.setFriends(friends);
        assertEquals(friends, user.getFriends());
    }

    @Test
    public void testSetFriendRequests() {
        User user = new User();
        List<Long> friendRequests = new ArrayList<>();
        friendRequests.add(1L);
        friendRequests.add(2L);
        user.setFriendrequests(friendRequests);
        assertEquals(friendRequests, user.getFriendrequests());
    }

    @Test
    public void testSetLobbyId() {
        User user = new User();
        user.setLobbyId(1L);
        assertEquals(1L, user.getLobbyId());
    }

    @Test
    public void testSetOpenLobbyInvitations() {
        User user = new User();
        List<Long> invitations = new ArrayList<>();
        invitations.add(1L);
        invitations.add(2L);
        user.setOpenLobbyInvitations(invitations);
        assertEquals(invitations, user.getOpenLobbyInvitations());
    }

    @Test
    public void testSetProfilePicture() {
        User user = new User();
        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setId(1L);
        user.setProfilePicture(profilePicture);
        assertEquals(profilePicture, user.getProfilePicture());
    }

    @Test
    public void testSetDescription() {
        User user = new User();
        user.setDescription("Hello, I am test description.");
        assertEquals("Hello, I am test description.", user.getDescription());
    }
}

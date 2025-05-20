package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.LobbyStorage;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LobbyServiceIntegrationTest {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private LobbyStorage lobbyStorage;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User testUser;
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        // Create and save test user
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setCreation_date(new Date());

        testUser = userRepository.saveAndFlush(testUser);

        // Create and store test lobby
        testLobby = new Lobby();
        testLobby.setLobbyName("testLobby");
        testLobby.createLobbyId();
        lobbyStorage.addLobby(testLobby);

        // Add lobby invite to user
        testUser.addLobbyInvitation(testLobby.getLobbyId());
        userRepository.saveAndFlush(testUser);
    }


    @Test
    public void userJoinsLobby_correctlyUpdatesUserAndLobby() {
        // Just a quick setup check
        assertTrue(testUser.getOpenLobbyInvitations().contains(testLobby.getLobbyId()));
        assertNull(testUser.getLobbyId());

        // user accepts invite and joins lobby
        Lobby updatedLobby = lobbyService.manageLobbyRequest(testLobby, testUser.getId(), true);

        // get user from DB
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();

        // Check if user was updated; i.e. has correct lobbyId and invitation removed
        assertEquals(testLobby.getLobbyId(), updatedUser.getLobbyId());
        assertFalse(updatedUser.getOpenLobbyInvitations().contains(testLobby.getLobbyId()));

        // Check if lobby contains user as a member
        assertTrue(updatedLobby.getMembers().stream()
                .anyMatch(member -> member.getId().equals(updatedUser.getId())));
    }

    @AfterEach
    public void teardown() {
        userRepository.deleteById(testUser.getId());
        lobbyStorage.removeLobby(testLobby.getLobbyId());
    }
}

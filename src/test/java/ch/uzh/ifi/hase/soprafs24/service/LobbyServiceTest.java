package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.LobbyStorage;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@ActiveProfiles("test")
public class LobbyServiceTest {
    @Mock
    private LobbyStorage lobbyStorage;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @MockBean
    private PictureRepository pictureRepository;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setId(1L);

        testLobby = new Lobby();
        testLobby.setLobbyName("testLobby");
        testLobby.setLobbyId(1L);
        testLobby.setHost(testUser);

        Mockito.when(lobbyStorage.addLobby(Mockito.any())).thenReturn(testLobby);
        Mockito.when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);
        doNothing().when(userRepository).flush();

    }

    @Test
    public void createLobby_success() {
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        assertEquals(createdLobby.getLobbyId(), testLobby.getLobbyId());
        assertEquals(createdLobby.getLobbyName(), testLobby.getLobbyName());
    }

    @Test
    public void inviteUserToLobby_success() {
        User user = new User();
        user.setId(1L);
        User addedUser = lobbyService.inviteUserToLobby(user, testLobby.getLobbyId());
        assertEquals(addedUser.getId(), user.getId());
        assertTrue(addedUser.getOpenLobbyInvitations().contains(testLobby.getLobbyId()));
    }

    @Test
    public void manageLobbyRequest_accepted_success() {
        User user = new User();
        user.setId(1L);
        user.addLobbyInvitation(testLobby.getLobbyId());
        Mockito.when(userService.getUserById(user.getId())).thenReturn(user);
        Lobby lobby = lobbyService.manageLobbyRequest(testLobby,user.getId(),true);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        assertEquals(testLobby.getLobbyId(), lobby.getLobbyId());
        assertTrue(lobby.getMembers().contains(user));
        assertTrue(user.getOpenLobbyInvitations().isEmpty());
    }

    @Test
    public void manageLobbyRequest_declined_success() {
        User user = new User();
        user.setId(1L);
        user.addLobbyInvitation(testLobby.getLobbyId());
        Mockito.when(userService.getUserById(user.getId())).thenReturn(user);
        Lobby lobby = lobbyService.manageLobbyRequest(testLobby,user.getId(),false);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
        assertEquals(testLobby.getLobbyId(), lobby.getLobbyId());
        assertTrue(lobby.getMembers().contains(testUser));
        assertTrue(user.getOpenLobbyInvitations().isEmpty());
    }

    @Test
    public void leaveOrDeleteLobby_hostLeaves_deletesLobbyAndUpdatesMembers() {
        User hostUser = new User();
        hostUser.setId(1L);
        hostUser.setLobbyId(1L);

        User member1 = new User();
        member1.setId(2L);
        member1.setLobbyId(1L);

        User member2 = new User();
        member2.setId(3L);
        member2.setLobbyId(1L);

        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setHost(hostUser);
        lobby.setMembers(List.of(hostUser, member1, member2));

        Mockito.when(lobbyStorage.getLobbyById(1L)).thenReturn(lobby);
        Mockito.when(userService.getUserById(1L)).thenReturn(hostUser);
        Mockito.when(userService.getUserById(2L)).thenReturn(member1);
        Mockito.when(userService.getUserById(3L)).thenReturn(member2);

        lobbyService.leaveOrDeleteLobby(1L, 1L);

        Mockito.verify(userRepository, Mockito.times(1)).save(hostUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(member1);
        Mockito.verify(userRepository, Mockito.times(1)).save(member2);
        Mockito.verify(lobbyStorage, Mockito.times(1)).deleteLobby(1L);
        Mockito.verify(userRepository, Mockito.times(1)).flush();

        assertNull(hostUser.getLobbyId());
        assertNull(member1.getLobbyId());
        assertNull(member2.getLobbyId());
    }

    @Test
    public void leaveOrDeleteLobby_memberLeaves_updatesMemberAndLobby() {
        User hostUser = new User();
        hostUser.setId(1L);
        hostUser.setLobbyId(1L);

        User member1 = new User();
        member1.setId(2L);
        member1.setLobbyId(1L);

        User member2 = new User();
        member2.setId(3L);
        member2.setLobbyId(1L);

        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setHost(hostUser);
        lobby.setMembers(new ArrayList<>(List.of(hostUser, member1, member2)));

        Mockito.when(lobbyStorage.getLobbyById(1L)).thenReturn(lobby);
        Mockito.when(userService.getUserById(2L)).thenReturn(member1);

        lobbyService.leaveOrDeleteLobby(1L, 2L);

        Mockito.verify(userRepository, Mockito.times(1)).save(member1);
        Mockito.verify(lobbyStorage, Mockito.never()).deleteLobby(1L);
        Mockito.verify(userRepository, Mockito.times(1)).flush();

        assertNull(member1.getLobbyId());
        assertFalse(lobby.getMembers().contains(member1));
    }

}

package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LobbyController.class)
public class LobbyControllerPostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    private Lobby lobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        lobby = new Lobby();
        lobby.setLobbyId(2L);
        lobby.setLobbyName("Our awesome Lobby");
    }

    //POST /lobbies
    @Test
    public void createLobby_success() throws Exception {

        UserGetDTO hostPostDTO = new UserGetDTO();

        User host = new User();

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyId(1L);
        lobbyPostDTO.setLobbyName("Our awesome lobby");
        lobbyPostDTO.setHost(hostPostDTO);

        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setLobbyName("Our awesome lobby");
        lobby.setHost(host);

        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ControllerTestUtils.asJsonString(lobbyPostDTO));

        mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.lobbyName", is(lobby.getLobbyName())));
    }

    // POST/lobbies/invite/{userId}
    @Test
    public void inviteUser_success() throws Exception {

        User user = new User();
        user.setId(1L);

        given(lobbyService.getLobbyById(2L)).willReturn(lobby);
        given(userService.getUserById(1L)).willReturn(user);
        given(lobbyService.inviteUserToLobby(user, 2L)).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/invite/1")
                .content("2") //lobbyId
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())));
    }

}

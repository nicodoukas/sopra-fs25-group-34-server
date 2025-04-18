package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LobbyController.class)
public class LobbyControllerGetTest {

    private static final Logger log = LoggerFactory.getLogger(LobbyControllerGetTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    private User member;

    private Lobby lobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        member = new User();
        member.setId(1L);
        member.setUsername("firstname@lastname");
        member.setPassword("1234");
        member.setStatus(UserStatus.OFFLINE);
        member.setCreation_date(new Date());
        member.setToken("1");

        lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setLobbyName("Our awesome Lobby");
        lobby.joinLobby(member);
    }


    // GET /lobbies/{lobbyId}
    @Test
    public void getLobbyById_success() throws Exception {

        given(lobbyService.getLobbyById(1L)).willReturn(lobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1")
            .contentType(MediaType.APPLICATION_JSON);
        getRequest.header("lobbyId", "1");

        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lobbyName", is(lobby.getLobbyName())));

    }

    // GET/lobbies/{lobbyId}/users
    @Test
    public void getLobbyUsers_success() throws Exception {

        List<User> members = Arrays.asList(member);

        given(lobbyService.getLobbyById(1L)).willReturn(lobby);
        given(lobbyService.getMembers(lobby)).willReturn(members);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/users")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$", hasSize(1)))
                .andExpect(jsonPath( "$[0].id", is(member.getId().intValue())))
                .andExpect(jsonPath( "$[0].username", is(member.getUsername())));
    }

}

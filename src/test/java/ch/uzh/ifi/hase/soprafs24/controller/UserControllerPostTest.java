package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerPostTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PictureRepository pictureRepository;

  @MockBean
  private UserService userService;

  // POST /users
  @Test
  public void createUser_validInput_userCreated() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date());
    user.setBirthday(new Date());
    user.setPassword("1234");

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("1234");

    given(userService.createUser(Mockito.any())).willReturn(user);

    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ControllerTestUtils.asJsonString(userPostDTO));

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(user.getId().intValue())))
            .andExpect(jsonPath("$.username", is(user.getUsername())))
            .andExpect(jsonPath("$.password", is(user.getPassword())))
            .andExpect(jsonPath("$.creation_date", is(formatter.format(user.getCreation_date()).replace("Z", "+00:00"))))
            .andExpect(jsonPath("$.birthday", is(formatter.format(user.getBirthday()).replace("Z", "+00:00"))))
            .andExpect(jsonPath("$.status", is(user.getStatus().toString())));

  }

  // POST /users ERROR
  @Test
  public void createUser_InvalidUsername_throwsError() throws Exception {
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Username not unique"));

    MockHttpServletRequestBuilder postRequest = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ControllerTestUtils.asJsonString(userPostDTO));

    mockMvc.perform(postRequest).andExpect(status().is(409));
  }

  // for POST/users/{userIdSender}/friendrequests
  @Test
  public void sendFriendRequest_success() throws Exception {
    User sender = new User();
    sender.setId(1L);
    sender.setUsername("Sender");

    User receiver = new User();
    receiver.setId(2L);
    receiver.setUsername("Receiver");

    given(userService.getUserById(1L)).willReturn(sender);
    given(userService.getUserById(2L)).willReturn(receiver);

    MockHttpServletRequestBuilder postRequest = post("/users/1/friendrequests")
      .contentType(MediaType.APPLICATION_JSON)
      .content("2");

    mockMvc.perform(postRequest)
      .andExpect(status().isCreated());
  }

  // POST /users/{userId}/friends accepting friendrequest
  @Test
  public void manageFriendRequest_acceptRequest_success() throws Exception {
    // test setup, create users
    User user = new User();
    user.setId(1L);
    user.setUsername("Username1");

    User friend = new User();
    friend.setId(2L);
    friend.setUsername("Username2");

    given(userService.getUserById(1L)).willReturn(user);
    given(userService.manageFriendRequest(user, 2L, true)).willReturn(user);

    String requestBody = """
    {
    "userId2": 2,
    "accepted": true
    }
    """;

    MockHttpServletRequestBuilder postRequest = post("/users/1/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);
    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(user.getId().intValue())))
            .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  // POST /users/{userId}/friends declining friendrequest
  @Test
  public void manageFriendRequest_declineRequest_success() throws Exception {

    User user = new User();
    user.setId(1L);
    user.setUsername("Username1");

    given(userService.getUserById(1L)).willReturn(user);
    given(userService.manageFriendRequest(user, 2L, false)).willReturn(user);

    String requestBody = """
    {
      "userId2": 2,
      "accepted": false
    }
    """;

    MockHttpServletRequestBuilder postRequest = post("/users/1/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);
    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(user.getId().intValue())))
            .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  // POST /users/{userId}/friends invalid userId
  @Test
  public void manageFriendRequest_nonexistentUser_throwsNotFound() throws Exception {

    given(userService.getUserById(1L)).willReturn(new User());
    given(userService.manageFriendRequest(Mockito.any(), Mockito.eq(123L), Mockito.anyBoolean()))
            .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    String requestBody = """
    {
    "userId2": 123,
    "accepted": true
     }
    """;
    MockHttpServletRequestBuilder postRequest = post("/users/1/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);
    mockMvc.perform(postRequest)
            .andExpect(status().isNotFound());
  }
}

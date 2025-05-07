package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerGetTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PictureRepository pictureRepository;

  @MockBean
  private UserService userService;

  private User user;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId(1L);
    user.setUsername("firstname@lastname");
    user.setPassword("1234");
    user.setStatus(UserStatus.OFFLINE);
    user.setCreation_date(new Date());
    user.setToken("1");
  }

  // GET /users/{userId}
  @Test
  public void getUserById_validInput_getcorrectUser() throws Exception {
    //accessing an existing user
    given(userService.getUserById(1L)).willReturn(user);

    MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);
    getRequest.header("token", "1");
    getRequest.header("id", "1");

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); //make sure date has correct format
    formatter.setTimeZone(TimeZone.getTimeZone("UTC")); //both are using UTC +00:00 timezone

    mockMvc.perform(getRequest).andExpect(status().is(200))
            .andExpect(jsonPath("$.id", is(user.getId().intValue())))
            .andExpect(jsonPath("$.username", is(user.getUsername())))
            .andExpect(jsonPath("$.password", is(user.getPassword())))
            .andExpect(jsonPath("$.creation_date", is(formatter.format(user.getCreation_date()).replace("Z", "+00:00")))) //replace Z because after adjusting Timezone instead of +00:00 it is Z
            .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  // GET /users/{userId} ERROR
  @Test
  public void getUserById_InvalidID_throwsError() throws Exception {

    given(userService.getUserById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

    //when
    MockHttpServletRequestBuilder getRequest = get("/users/2")
      .contentType(MediaType.APPLICATION_JSON);
    getRequest.header("token", "1");
    getRequest.header("id", "1");

    //then
    mockMvc.perform(getRequest).andExpect(status().is(404));
  }

  // GET /users
  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    List<User> allUsers = Collections.singletonList(user);
    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); //make sure date has correct format
    formatter.setTimeZone(TimeZone.getTimeZone("UTC")); //both are using UTC +00:00 timezone

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(user.getId().intValue())))
            .andExpect(jsonPath("$[0].username", is(user.getUsername())))
            .andExpect(jsonPath("$[0].password", is(user.getPassword())))
            .andExpect(jsonPath("$[0].creation_date", is(formatter.format(user.getCreation_date()).replace("Z", "+00:00")))) //replace Z because after adjusting Timezone instead of +00:00 it is Z
            .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  // for GET/users/{userId}/friendrequests
  @Test
  public void getOpenFriendRequests_noRequests() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setUsername("Username");

    given(userService.getUserById(1L)).willReturn(user);

    MockHttpServletRequestBuilder getRequest = get("/users/1/friendrequests")
      .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(0)));
  }

    // for GET/users/{userId}/friendrequests
  @Test
  public void getOpenFriendRequests_oneRequests() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setUsername("Username");
    List<Long> requests = new ArrayList<Long>();
    requests.add(2L);
    user.setFriendrequests(requests);    

    given(userService.getUserById(1L)).willReturn(user);
    given(userService.getOpenFriendRequests(user)).willReturn(requests);

    MockHttpServletRequestBuilder getRequest = get("/users/1/friendrequests")
      .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0]", is(2)));
  }

  // GET /usersByUsername/{username} success
  @Test
  public void getUserByUsername_validInput_returnsUser() throws Exception {

    given(userService.getUserByUsername("firstname@lastname")).willReturn(user);
    MockHttpServletRequestBuilder getRequest = get("/usersByUsername/firstname@lastname")
            .contentType(MediaType.APPLICATION_JSON);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(user.getId().intValue())))
            .andExpect(jsonPath("$.username", is(user.getUsername())))
            .andExpect(jsonPath("$.password", is(user.getPassword())))
            .andExpect(jsonPath("$.creation_date", is(formatter.format(user.getCreation_date()).replace("Z", "+00:00")))) // danke Julia
            .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  // GET /usersByUsername/{username} error
  @Test
  public void getUserByUsername_invalidInput_throwsNotFound() throws Exception {

    given(userService.getUserByUsername("nonexistent")).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    MockHttpServletRequestBuilder getRequest = get("/usersByUsername/nonexistent")
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isNotFound());
  }
}

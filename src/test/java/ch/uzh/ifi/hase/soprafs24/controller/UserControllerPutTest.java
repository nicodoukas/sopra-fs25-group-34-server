package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
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

// import java.util.Collections;
// import java.util.List;
import java.util.Date;

// import static org.hamcrest.Matchers.hasSize;
// import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerPutTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PictureRepository pictureRepository;

  @MockBean UserService userService;

  // PUT /users/{userId}
  @Test
  public void updateUser_validInput_userUpdated() throws Exception {
    User user = new User();
    user.setCreation_date(new Date());
    user.setUsername("OGUsername");
    user.setStatus(UserStatus.ONLINE);
    user.setId(1L);
    user.setToken("1");
    user.setPassword("1234");


    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setBirthday(new Date());
    userPutDTO.setUsername("testUsername");

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder putRequest = put("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ControllerTestUtils.asJsonString(userPutDTO))
            .header("token", "1")
            .header("id", "1");

    // then
    mockMvc.perform(putRequest).andExpect(status().is(204));
  }

  // PUT /users/{userId} ERROR
  @Test
  public void updateUser_InvalidId() throws Exception {
      UserPutDTO userPutDTO = new UserPutDTO();
      userPutDTO.setId(1L);
      userPutDTO.setUsername("NewUsername");
      userPutDTO.setBirthday(new Date());

      doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User id was not found")).when(userService).updateUser(any());

      MockHttpServletRequestBuilder putRequest = put("/users/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(ControllerTestUtils.asJsonString(userPutDTO));

      // then
      mockMvc.perform(putRequest).andExpect(status().is(404));
  }

  // Sorry, I am adding the user DELETE test here because they are probably the only two DELETE tests necessary
  // and I don't want to make a new file just for these two

  // DELETE /users/{userId}/friends/{userId2} success
  @Test
  public void deleteFriend_existingUser_success() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setUsername("Username1");
    User friend = new User();
    friend.setId(2L);
    friend.setUsername("Username2");

    given(userService.getUserById(1L)).willReturn(user);
    MockHttpServletRequestBuilder deleteRequest =
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .delete("/users/1/friends/2");

    mockMvc.perform(deleteRequest)
            .andExpect(status().isNoContent());
  }

  // DELETE /users/{userId}/friends/{userId2} error, friend does not exist
  @Test
  public void deleteFriend_nonExistingFriend_throwsNotFound() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setUsername("Username1");

    given(userService.getUserById(1L)).willReturn(user);
    Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend not found"))
            .when(userService).deleteFriend(Mockito.eq(user), Mockito.eq(2L));

    MockHttpServletRequestBuilder deleteRequest =
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .delete("/users/1/friends/2");
    mockMvc.perform(deleteRequest)
            .andExpect(status().isNotFound());
  }
}

package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerPutTest {

  @Autowired
  private MockMvc mockMvc;

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
            .content(asJsonString(userPutDTO))
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

      MockHttpServletRequestBuilder putRequest = post("/edit/{userid}", 1L, userPutDTO)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPutDTO));

      // then
      mockMvc.perform(putRequest).andExpect(status().is(404));
  }

  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    }
    catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
      String.format("The request body could not be created.%s", e.toString()));
    }
  }

}

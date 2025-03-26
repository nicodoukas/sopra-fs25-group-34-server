package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/usersByUsername/{username}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable String username){
    User user = userService.getUserByUsername(username);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }
  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @GetMapping("/users/{userId}") // retrieve user profile with userid
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable Long userId) {

    User user = userService.getUserById(userId);

    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public UserGetDTO login(@RequestBody UserPostDTO userPostDTO){
  // convert API user to internal representation
  User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

  //get user by username
  User foundUser = userService.getUserByUsername(userInput.getUsername());

  //check if the password is correct
  if (!userInput.getPassword().equals(foundUser.getPassword())){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
  }
  //userStatus = ONLINE
  userService.login(foundUser.getId());

  return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
  }

  @PutMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void logout(@RequestBody Long id){

    //userStatus = OFFLINE
    userService.logout(id);

  }

  @PutMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateUser(@PathVariable Long userId, @RequestBody UserPutDTO userPutDTO) {

    User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
    userInput.setId(userId);

      userService.updateUser(userInput);
  }

}

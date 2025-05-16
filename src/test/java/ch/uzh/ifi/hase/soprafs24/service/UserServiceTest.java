package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PictureRepository pictureRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUsername");

    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
  }

  @Test
  public void createUser_validInputs_success() {
    User newUser = new User();
    newUser.setId(2L);
    newUser.setUsername("newUsername");
    newUser.setPassword("newPassword");
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(newUser);
    User createdUser = userService.createUser(newUser);

    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(2, createdUser.getId()); // second user to be created
    assertEquals(newUser.getUsername(), createdUser.getUsername());
    assertEquals(newUser.getPassword(), createdUser.getPassword());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }


  @Test
  public void createUser_duplicateInputs_throwsException() {
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void getUserByUsername_validInputs_success() {
    User getUser = userService.getUserByUsername(testUser.getUsername());
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(testUser.getUsername());
    assertEquals(testUser.getId(), getUser.getId());
    assertEquals(testUser.getUsername(), getUser.getUsername());
  }

  @Test
  public void getUserByUsername_notFound_throwsException() {
    assertThrows(ResponseStatusException.class, () -> userService.getUserByUsername("newUsername"));
  }

  @Test
  public void getUserById_validInputs_success() {
    User getUser = userService.getUserById(testUser.getId());
    Mockito.verify(userRepository, Mockito.times(1)).findById(testUser.getId());
    assertEquals(testUser.getId(), getUser.getId());
    assertEquals(testUser.getUsername(), getUser.getUsername());
  }

  @Test
  public void getUserById_notFound_throwsException() {
    assertThrows(ResponseStatusException.class, () -> userService.getUserById(3L));
  }

  @Test
  public void login_success() {
    userService.login(testUser.getId());
    assertEquals(UserStatus.ONLINE, testUser.getStatus());
    Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).flush();
  }

  @Test
  public void logout_success() {
    testUser.setStatus(UserStatus.ONLINE);
    userService.logout(testUser.getId());
    assertEquals(UserStatus.OFFLINE, testUser.getStatus());
    Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).flush();
  }

  @Test
  public void updateUser_success() {
    User updateUser = new User();
    updateUser.setId(1L);
    updateUser.setUsername("updatedUsername");
    updateUser.setBirthday(new Date());
    User updatedUser = userService.updateUser(updateUser);
    Mockito.verify(userRepository, Mockito.times(1)).findById(updateUser.getId());
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(updateUser.getUsername());
    assertEquals(updateUser.getId(), updatedUser.getId());
    assertEquals(updateUser.getUsername(), updatedUser.getUsername());
    assertEquals(updateUser.getBirthday(), updatedUser.getBirthday());
  }

  @Test
  public void updateUser_UsernameNotUnique_throwsException() {
    User updateUser = new User();
    updateUser.setId(2L);
    updateUser.setUsername("testUsername");
    Mockito.when(userRepository.findById(updateUser.getId())).thenReturn(Optional.of(updateUser));
    assertThrows(ResponseStatusException.class, () -> userService.updateUser(updateUser));
    Mockito.verify(userRepository, Mockito.times(1)).findById(updateUser.getId());
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(updateUser.getUsername());
  }

  @Test
  public void deleteFriend_success() {
    User friend = new User();
    friend.setId(2L);
    Mockito.when(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend));
    testUser.addFriend(friend.getId());
    userService.deleteFriend(testUser, friend.getId());
    Mockito.verify(userRepository, Mockito.times(2)).findById(friend.getId());
    Mockito.verify(userRepository, Mockito.times(1)).save(friend);
    Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).flush();
    assertTrue(testUser.getFriends().isEmpty());
  }

  @Test
  public void manageFriendRequest_accepted_success() {
    User newFriend = new User();
    newFriend.setId(2L);
    testUser.addFriendrequest(newFriend.getId());
    Mockito.when(userRepository.findById(newFriend.getId())).thenReturn(Optional.of(newFriend));
    userService.manageFriendRequest(newFriend, testUser.getId(), true);
    Mockito.verify(userRepository, Mockito.times(2)).findById(testUser.getId());
    Mockito.verify(userRepository, Mockito.times(1)).save(newFriend);
    Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).flush();
    assertTrue(testUser.getFriends().contains(newFriend.getId()));
    assertTrue(newFriend.getFriends().contains(testUser.getId()));
    assertTrue(newFriend.getFriendrequests().isEmpty());
  }

  @Test
  public void manageFriendRequest_declined_throwsException() {
    User newFriend = new User();
    newFriend.setId(2L);
    testUser.addFriendrequest(newFriend.getId());
    Mockito.when(userRepository.findById(newFriend.getId())).thenReturn(Optional.of(newFriend));
    userService.manageFriendRequest(newFriend, testUser.getId(), false);
    Mockito.verify(userRepository, Mockito.times(2)).findById(testUser.getId());
    Mockito.verify(userRepository, Mockito.times(1)).save(newFriend);
    Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).flush();
    assertTrue(testUser.getFriends().isEmpty());
    assertTrue(newFriend.getFriends().isEmpty());
    assertTrue(newFriend.getFriendrequests().isEmpty());
  }

  @Test
  public void sendFriendRequest_success() {
    User friend = new User();
    friend.setId(2L);
    Mockito.when(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend));
    userService.sendFriendRequest(friend.getId(), testUser);
    Mockito.verify(userRepository, Mockito.times(1)).findById(friend.getId());
    Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).flush();
    assertTrue(testUser.getFriendrequests().contains(friend.getId()));
  }

  @Test
  public void setStatusToPlaying_success() {
      testUser.setStatus(UserStatus.ONLINE);
      Long userId = testUser.getId();

      userService.setStatusToPlaying(userId);

      Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
      Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).flush();
      assertEquals(UserStatus.PLAYING, testUser.getStatus());
  }

}

package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called

    newUser.setCreation_date(new Date());
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage, "username", "is"));
    }
  }

  /**
   * Gets User by username
   *
   * @param username
   * @throws org.springframework.web.server.ResponseStatusException
   * @return userFound
  */
  public User getUserByUsername(String username) {
    User userFound = userRepository.findByUsername(username);

    if (userFound == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
    }

    return userFound;
  }

  public User getUserById(Long id) {
    Optional<User> userFound = userRepository.findById(id);

    if (userFound.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
    }
    return userFound.get();
  }

  /* sets UserStatus to ONLINE after login*/
  public void login(Long id){
    User user = getUserById(id);
    user.setStatus(UserStatus.ONLINE);
    userRepository.save(user);
    userRepository.flush();
  }

  /* sets UserStatus to OFFLINE as logout*/
  public void logout(Long id){
    User user = getUserById(id);
    user.setStatus(UserStatus.OFFLINE);
    userRepository.save(user);
    userRepository.flush();
  }

  public User updateUser(User userToUpdate){
    User user = getUserById(userToUpdate.getId());

    //check if username was changed
    if (userToUpdate.getUsername() != null) {
          //check if new username is unique;
          User userByUsername = userRepository.findByUsername(userToUpdate.getUsername());
          if (userByUsername != null) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not unique");
          }

          user.setUsername(userToUpdate.getUsername());
      }
    //check if birthday was  changed
    if (userToUpdate.getBirthday() != null) {
      user.setBirthday(userToUpdate.getBirthday());
    }
    return userRepository.save(user);
  }

  /**
  * Delete Friend
  *
  * @param user The user whose friend is deleted
  * @param friendUserId The userId of the friend to be removed
  **/
  public void deleteFriend(User user, Long friendUserId) {
      //check if user with friendUserId exists
      getUserById(friendUserId);
      User friend = getUserById(friendUserId);

      //Remove friend
      user.removeFriend(friendUserId);
      friend.removeFriend(user.getId());

      //save updated User to Database
      userRepository.save(user);
      userRepository.save(friend);
      userRepository.flush();
  }

  /**
   *
   * @param user The User which friendrequests are managed
   * @param userId2 The userId of the request
   * @param accepted True if the friendrequest is accepted, False otherwise
   * @return user
  */
  public User manageFriendRequest(User user, Long userId2, Boolean accepted){
    //check if User with userId userId2 exists
    getUserById(userId2);
    User friend = getUserById(userId2);

    //accepted
    if (accepted) {
      user.setFriends(userId2);
      friend.setFriends(user.getId());
    }
    //declined
    else {
      user.declineFriendRequest(userId2);
    }
    //save updated user
    userRepository.save(user);
    userRepository.save(friend);
    userRepository.flush();
    return user;
  }

  public void sendFriendRequest(Long userIdSender, User userReceiver) {
    //to throw exception if senderId doesn't exist
    User userSender = this.getUserById(userIdSender);

    userReceiver.sendFriendRequest(userIdSender);
    userRepository.save(userReceiver);
    userRepository.flush();
  }

  public List<Long> getOpenFriendRequests(User user) {
    return user.getOpenFriendRequests();
  }

}
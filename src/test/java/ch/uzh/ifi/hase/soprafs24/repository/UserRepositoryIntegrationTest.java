package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.ProfilePicture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUsername_success() {
    ProfilePicture profilePicture = new ProfilePicture();
    profilePicture.setUrl("https://image.com");
    profilePicture.setId(1L);

    profilePicture = entityManager.merge(profilePicture);
    entityManager.flush();

    User user = new User();
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setCreation_date(new Date());
    user.setBirthday(new Date());
    user.setPassword("1234");
    user.setFriends(Arrays.asList(2L, 3L));
    user.setFriendrequests(Arrays.asList(4L, 5L));
    user.setOpenLobbyInvitations(Arrays.asList(6L, 7L));
    user.setLobbyId(8L);
    user.setProfilePicture(profilePicture);
    user.setDescription("Test description");

    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByUsername(user.getUsername());

    assertNotNull(found);
    assertEquals(user.getUsername(), found.getUsername());
    assertEquals(user.getPassword(), found.getPassword());
    assertEquals(user.getCreation_date(), found.getCreation_date());
    assertEquals(user.getBirthday(), found.getBirthday());
    assertEquals(user.getToken(), found.getToken());
    assertEquals(user.getStatus(), found.getStatus());
    assertEquals(user.getFriends(), found.getFriends());
    assertEquals(user.getFriendrequests(), found.getFriendrequests());
    assertEquals(user.getOpenLobbyInvitations(), found.getOpenLobbyInvitations());
    assertEquals(user.getLobbyId(), found.getLobbyId());
    assertEquals(user.getProfilePicture(), found.getProfilePicture());
    assertEquals(user.getDescription(), found.getDescription());
  }
}

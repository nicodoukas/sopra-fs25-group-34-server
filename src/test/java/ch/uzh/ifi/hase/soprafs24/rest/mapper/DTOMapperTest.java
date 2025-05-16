package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.ProfilePicture;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void test_convertUserPostDTOtoEntity_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password123");
        userPostDTO.setCreation_date(new Date());
        userPostDTO.setBirthday(new Date());
        userPostDTO.setStatus(UserStatus.OFFLINE);
        userPostDTO.setToken("token123");
        userPostDTO.setId(1L);

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getCreation_date(), user.getCreation_date());
        assertEquals(userPostDTO.getBirthday(), user.getBirthday());
        assertEquals(userPostDTO.getStatus(), user.getStatus());
        assertEquals(userPostDTO.getToken(), user.getToken());
        assertEquals(userPostDTO.getId(), user.getId());
    }

    @Test
    public void test_convertUserPostDTOtoEntity_failure() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(null);
        userPostDTO.setPassword(null);
        userPostDTO.setCreation_date(null);
        userPostDTO.setBirthday(null);
        userPostDTO.setStatus(null);
        userPostDTO.setToken(null);
        userPostDTO.setId(null);

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getCreation_date());
        assertNull(user.getBirthday());
        assertNull(user.getStatus());
        assertNull(user.getToken());
        assertNull(user.getId());
    }

    @Test
    public void test_convertEntityToUserGetDTO_success() {
        // create User
        User user = new User();
        user.setId(1L);
        user.setUsername("firstname@lastname");
        user.setPassword("password123");
        user.setCreation_date(new Date());
        user.setBirthday(new Date());
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("token123");
        user.setFriends(Arrays.asList(2L, 3L));
        user.setFriendrequests(Arrays.asList(4L, 5L));
        user.setOpenLobbyInvitations(Arrays.asList(6L, 7L));
        user.setLobbyId(8L);
        ProfilePicture profilePicture = new ProfilePicture();
        user.setProfilePicture(profilePicture);
        user.setDescription("test description");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getCreation_date(), userGetDTO.getCreation_date());
        assertEquals(user.getBirthday(), userGetDTO.getBirthday());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
        assertEquals(user.getFriends(), userGetDTO.getFriends());
        assertEquals(user.getFriendrequests(), userGetDTO.getFriendrequests());
        assertEquals(user.getOpenLobbyInvitations(), userGetDTO.getOpenLobbyInvitations());
        assertEquals(user.getLobbyId(), userGetDTO.getLobbyId());
        assertEquals(user.getProfilePicture(), userGetDTO.getProfilePicture());
        assertEquals(user.getDescription(), userGetDTO.getDescription());
    }

    @Test
    public void test_convertEntityToUserGetDTO_failure() {
        User user = new User();
        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setCreation_date(null);
        user.setBirthday(null);
        user.setStatus(null);
        user.setToken(null);
        user.setFriends(null);
        user.setFriendrequests(null);
        user.setOpenLobbyInvitations(null);
        user.setLobbyId(null);
        user.setProfilePicture(null);
        user.setDescription(null);

        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        assertNotNull(userGetDTO);
        assertNull(userGetDTO.getId());
        assertNull(userGetDTO.getUsername());
        assertNull(userGetDTO.getPassword());
        assertNull(userGetDTO.getCreation_date());
        assertNull(userGetDTO.getBirthday());
        assertNull(userGetDTO.getStatus());
        assertNull(userGetDTO.getFriends());
        assertNull(userGetDTO.getFriendrequests());
        assertNull(userGetDTO.getOpenLobbyInvitations());
        assertNull(userGetDTO.getLobbyId());
        assertNull(userGetDTO.getProfilePicture());
        assertNull(userGetDTO.getDescription());
    }

    @Test
    public void test_convertUserGetDTOToEntity_success() {
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(1L);
        userGetDTO.setUsername("testUser");
        userGetDTO.setPassword("testPassword");
        userGetDTO.setCreation_date(new Date());
        userGetDTO.setBirthday(new Date());
        userGetDTO.setStatus(UserStatus.ONLINE);
        userGetDTO.setFriends(Arrays.asList(2L, 3L));
        userGetDTO.setFriendrequests(Arrays.asList(4L, 5L));
        userGetDTO.setOpenLobbyInvitations(Arrays.asList(6L, 7L));
        userGetDTO.setLobbyId(8L);

        User user = DTOMapper.INSTANCE.convertUserGetDTOToEntity(userGetDTO);

        assertEquals(userGetDTO.getId(), user.getId());
        assertEquals(userGetDTO.getUsername(), user.getUsername());
        assertEquals(userGetDTO.getPassword(), user.getPassword());
        assertEquals(userGetDTO.getCreation_date(), user.getCreation_date());
        assertEquals(userGetDTO.getBirthday(), user.getBirthday());
        assertEquals(userGetDTO.getStatus(), user.getStatus());
        assertEquals(userGetDTO.getFriends(), user.getFriends());
        assertEquals(userGetDTO.getFriendrequests(), user.getFriendrequests());
        assertEquals(userGetDTO.getOpenLobbyInvitations(), user.getOpenLobbyInvitations());
        assertEquals(userGetDTO.getLobbyId(), user.getLobbyId());
    }

    @Test
    public void test_convertUserGetDTOToEntity_failure() {
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(null);
        userGetDTO.setUsername(null);
        userGetDTO.setPassword(null);
        userGetDTO.setCreation_date(null);
        userGetDTO.setBirthday(null);
        userGetDTO.setStatus(null);
        userGetDTO.setFriends(null);
        userGetDTO.setFriendrequests(null);
        userGetDTO.setOpenLobbyInvitations(null);
        userGetDTO.setLobbyId(null);

        User user = DTOMapper.INSTANCE.convertUserGetDTOToEntity(userGetDTO);

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getCreation_date());
        assertNull(user.getBirthday());
        assertNull(user.getStatus());
        assertEquals(new ArrayList<>(), user.getFriends());
        assertEquals(new ArrayList<>(),user.getFriendrequests());
        assertEquals(new ArrayList<>(),user.getOpenLobbyInvitations());
        assertNull(user.getLobbyId());
    }

    @Test
    public void test_convertUserPutDTOtoEntity_success() {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedUser");
        userPutDTO.setBirthday(new Date());
        userPutDTO.setId(1L);
        ProfilePicture profilePicture = new ProfilePicture();
        userPutDTO.setProfilePicture(profilePicture);
        userPutDTO.setDescription("Updated description");

        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getBirthday(), user.getBirthday());
        assertEquals(userPutDTO.getId(), user.getId());
        assertEquals(userPutDTO.getProfilePicture(), user.getProfilePicture());
        assertEquals(userPutDTO.getDescription(), user.getDescription());
    }

    @Test
    public void test_convertUserPutDTOtoEntity_failure() {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername(null);
        userPutDTO.setBirthday(null);
        userPutDTO.setId(null);
        userPutDTO.setProfilePicture(null);
        userPutDTO.setDescription(null);

        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getBirthday());
        assertNull(user.getId());
        assertNull(user.getProfilePicture());
        assertNull(user.getDescription());
    }

}

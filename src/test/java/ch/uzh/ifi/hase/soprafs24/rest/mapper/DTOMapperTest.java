package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.ProfilePicture;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}

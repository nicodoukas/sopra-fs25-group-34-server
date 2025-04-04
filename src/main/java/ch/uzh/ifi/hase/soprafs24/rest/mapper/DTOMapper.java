package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "creation_date", target = "creation_date")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "id", target = "id")
  @Mapping(source = "token", target = "token")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "creation_date", target = "creation_date")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "friends", target = "friends")
  @Mapping(source = "friendrequests", target = "friendrequests")
  @Mapping(source = "openLobbyInvitations", target = "openLobbyInvitations")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "creation_date", target = "creation_date")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "friends", target = "friends", qualifiedByName = "MapListLong")
  @Mapping(source = "friendrequests", target = "friendrequests")
  @Mapping(source = "openLobbyInvitations", target = "openLobbyInvitations")
  User convertUserGetDTOToEntity(UserGetDTO userGetDTO);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "id", target = "id")
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "lobbyName", target = "lobbyName")
  @Mapping(source = "host", target = "host")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "lobbyName", target = "lobbyName")
  @Mapping(source = "members", target = "members")
  @Mapping(source = "host", target ="host")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);


}

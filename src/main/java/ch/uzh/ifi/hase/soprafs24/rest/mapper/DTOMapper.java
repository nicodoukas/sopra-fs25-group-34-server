package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;

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
  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "profilePicture", target = "profilePicture")
  @Mapping(source = "description", target = "description")
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
  @Mapping(source = "lobbyId", target = "lobbyId")
  User convertUserGetDTOToEntity(UserGetDTO userGetDTO);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "id", target = "id")
  @Mapping(source = "profilePicture", target = "profilePicture")
  @Mapping(source = "description", target = "description")
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

  @Mapping(source = "userId", target="userId")
  @Mapping(source = "gameId", target="gameId")
  @Mapping(source = "coinBalance", target="coinBalance")
  @Mapping(source = "username", target="username")
  @Mapping(source = "timeline", target="timeline")
  Player convertPlayerGetDTOtoEntity(PlayerGetDTO playerGetDTO);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "gameId", target = "gameId")
  @Mapping(source = "coinBalance", target = "coinBalance")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "timeline", target = "timeline")
  @Mapping(source = "profilePicture", target = "profilePicture")
  PlayerGetDTO convertEntityToPlayerGetDTO(Player player);

  @Mapping(source = "addCoin", target = "addCoin")
  @Mapping(source = "position", target = "position")
  @Mapping(source = "songCard", target = "songCard")
  PlayerPutDTO convertPlayerPutDTOtoEntity(PlayerPutDTO playerPutDTO);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "artist", target = "artist")
  @Mapping(source = "year", target = "year")
  @Mapping(source = "songURL", target = "songURL")
  SongCard convertSongCardGetDTOtoEntity(SongCardGetDTO songCardGetDTO);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "artist", target = "artist")
  @Mapping(source = "year", target = "year")
  @Mapping(source = "songURL", target = "songURL")
  SongCardGetDTO convertEntityToSongCardGetDTO(SongCard songCard);

  @Mapping(source = "players", target = "players")
  @Mapping(source = "gameId", target = "gameId")
  @Mapping(source = "turnCount", target = "turnCount")
  @Mapping(source = "turnOrder", target = "turnOrder")
  @Mapping(source = "host", target = "host")
  @Mapping(source = "currentRound", target = "currentRound")
  @Mapping(source = "gameName", target = "gameName")
  GameGetDTO convertEntitytoGameGetDTO(Game game);

  @Mapping(source = "guessedTitle", target = "guessedTitle")
  @Mapping(source = "guessedArtist", target = "guessedArtist")
  @Mapping(source = "player", target = "player")
  Guess convertGuessPostDTOtoEntity(GuessPostDTO guessPostDTO);

}

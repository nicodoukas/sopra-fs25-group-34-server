package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void test_convertUserPostDTOtoEntity_success() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password123");
        userPostDTO.setCreation_date(new Date());
        userPostDTO.setBirthday(new Date());
        userPostDTO.setStatus(UserStatus.OFFLINE);
        userPostDTO.setToken("token123");
        userPostDTO.setId(1L);

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

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

        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

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
        assertEquals(new ArrayList<>(), user.getFriends()); // if null, friends is initialized as empty list
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

    @Test
    public void test_convertLobbyPostDTOtoEntity_success() {
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyId(1L);
        lobbyPostDTO.setLobbyName("testLobby");

        UserGetDTO hostDTO = new UserGetDTO();
        hostDTO.setId(1L);
        hostDTO.setUsername("testHost");
        lobbyPostDTO.setHost(hostDTO);

        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        assertEquals(lobbyPostDTO.getLobbyId(), lobby.getLobbyId());
        assertEquals(lobbyPostDTO.getLobbyName(), lobby.getLobbyName());
        assertNotNull(lobby.getHost());
        assertEquals(lobbyPostDTO.getHost().getId(), lobby.getHost().getId());
        assertEquals(lobbyPostDTO.getHost().getUsername(), lobby.getHost().getUsername());
    }

    @Test
    public void test_convertLobbyPostDTOtoEntity_failure() {
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyId(null);
        lobbyPostDTO.setLobbyName(null);
        lobbyPostDTO.setHost(null);

        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        assertNotNull(lobby);
        assertNull(lobby.getLobbyId());
        assertNull(lobby.getLobbyName());
        assertNull(lobby.getHost());
    }

    @Test
    public void test_convertEntityToLobbyGetDTO_success() {
        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setLobbyName("testLobby");

        User host = new User();
        host.setId(1L);
        host.setUsername("testHost");

        User member1 = new User();
        member1.setId(2L);
        member1.setUsername("testMember1");

        User member2 = new User();
        member2.setId(3L);
        member2.setUsername("testMember2");

        lobby.setHost(host);
        lobby.setMembers(Arrays.asList(host, member1, member2));

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        assertEquals(lobby.getLobbyId(), lobbyGetDTO.getLobbyId());
        assertEquals(lobby.getLobbyName(), lobbyGetDTO.getLobbyName());
        assertNotNull(lobbyGetDTO.getHost());
        assertEquals(lobby.getHost().getId(), lobbyGetDTO.getHost().getId());
        assertEquals(lobby.getHost().getUsername(), lobbyGetDTO.getHost().getUsername());
        assertEquals(3, lobbyGetDTO.getMembers().size());
        assertEquals(lobby.getMembers().get(0).getId(), lobbyGetDTO.getMembers().get(0).getId());
        assertEquals(lobby.getMembers().get(1).getId(), lobbyGetDTO.getMembers().get(1).getId());
    }

    @Test
    public void test_convertEntityToLobbyGetDTO_failure() {
        Lobby lobby = new Lobby();
        lobby.setLobbyId(null);
        lobby.setLobbyName(null);
        lobby.setHost(null);
        lobby.setMembers(null);

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        assertNotNull(lobbyGetDTO);
        assertNull(lobbyGetDTO.getLobbyId());
        assertNull(lobbyGetDTO.getLobbyName());
        assertNull(lobbyGetDTO.getHost());
        assertNull(lobbyGetDTO.getMembers());
    }

    @Test
    public void test_convertPlayerGetDTOtoEntity_success() {
        PlayerGetDTO playerGetDTO = new PlayerGetDTO();
        playerGetDTO.setUserId(1L);
        playerGetDTO.setGameId(10L);
        playerGetDTO.setCoinBalance(3);
        playerGetDTO.setUsername("testPlayer");

        SongCardGetDTO song1 = new SongCardGetDTO();
        song1.setTitle("Teen Age Riot");
        song1.setYear(1988);
        song1.setArtist("Sonic Youth");

        SongCardGetDTO song2 = new SongCardGetDTO();
        song2.setTitle("The Diamond Sea");
        song2.setYear(1995);
        song1.setArtist("Sonic Youth");

        playerGetDTO.setTimeline(Arrays.asList(song1, song2));

        Player player = DTOMapper.INSTANCE.convertPlayerGetDTOtoEntity(playerGetDTO);

        assertEquals(playerGetDTO.getUserId(), player.getUserId());
        assertEquals(playerGetDTO.getGameId(), player.getGameId());
        assertEquals(playerGetDTO.getCoinBalance(), player.getCoinBalance());
        assertEquals(playerGetDTO.getUsername(), player.getUsername());
        assertNotNull(player.getTimeline());
        assertEquals(2, player.getTimeline().size());
        assertEquals("Teen Age Riot", player.getTimeline().get(0).getTitle());
        assertEquals("The Diamond Sea", player.getTimeline().get(1).getTitle());
    }

    @Test
    public void test_convertPlayerGetDTOtoEntity_failure() {
        PlayerGetDTO playerGetDTO = new PlayerGetDTO();
        playerGetDTO.setUserId(null);
        playerGetDTO.setGameId(null);
        playerGetDTO.setCoinBalance(0);
        playerGetDTO.setUsername(null);
        playerGetDTO.setTimeline(null);

        Player player = DTOMapper.INSTANCE.convertPlayerGetDTOtoEntity(playerGetDTO);

        assertNotNull(player);
        assertNull(player.getUserId());
        assertNull(player.getGameId());
        assertEquals(0, player.getCoinBalance());
        assertNull(player.getUsername());
        assertNull(player.getTimeline());
    }

    @Test
    public void test_convertEntityToPlayerGetDTO_success() {
        Player player = new Player();
        player.setUserId(1L);
        player.setGameId(10L);
        player.setCoinBalance(3);
        player.setUsername("testPlayer");

        SongCard song1 = new SongCard();
        song1.setTitle("Teen Age Riot");
        song1.setYear(1988);
        song1.setArtist("Sonic Youth");

        SongCard song2 = new SongCard();
        song2.setTitle("The Diamond Sea");
        song2.setYear(1995);
        song1.setArtist("Sonic Youth");

        player.setTimeline(Arrays.asList(song1, song2));

        ProfilePicture profilePicture = new ProfilePicture();
        player.setProfilePicture(profilePicture);

        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);

        assertEquals(player.getUserId(), playerGetDTO.getUserId());
        assertEquals(player.getGameId(), playerGetDTO.getGameId());
        assertEquals(player.getCoinBalance(), playerGetDTO.getCoinBalance());
        assertEquals(player.getUsername(), playerGetDTO.getUsername());
        assertNotNull(playerGetDTO.getTimeline());
        assertEquals(2, playerGetDTO.getTimeline().size());
        assertEquals("Teen Age Riot", playerGetDTO.getTimeline().get(0).getTitle());
        assertEquals("The Diamond Sea", playerGetDTO.getTimeline().get(1).getTitle());
        assertEquals(player.getProfilePicture(), playerGetDTO.getProfilePicture());
    }

    @Test
    public void test_convertEntityToPlayerGetDTO_failure() {
        Player player = new Player();
        player.setUserId(null);
        player.setGameId(null);
        player.setCoinBalance(0);
        player.setUsername(null);
        player.setTimeline(null);
        player.setProfilePicture(null);

        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);

        assertNotNull(playerGetDTO);
        assertNull(playerGetDTO.getUserId());
        assertNull(playerGetDTO.getGameId());
        assertEquals(0, playerGetDTO.getCoinBalance());
        assertNull(playerGetDTO.getUsername());
        assertNull(playerGetDTO.getTimeline());
        assertNull(playerGetDTO.getProfilePicture());
    }

    @Test
    public void test_convertPlayerPutDTOtoEntity_success() {
        PlayerPutDTO playerPutDTO = new PlayerPutDTO();
        playerPutDTO.setAddCoin(true);
        playerPutDTO.setPosition(2);

        SongCard songCard = new SongCard();
        songCard.setTitle("Kool Thing");
        songCard.setArtist("Sonic Youth");
        songCard.setYear(1990);
        playerPutDTO.setSongCard(songCard);

        PlayerPutDTO res = DTOMapper.INSTANCE.convertPlayerPutDTOtoEntity(playerPutDTO);

        assertEquals(playerPutDTO.getAddCoin(), res.getAddCoin());
        assertEquals(playerPutDTO.getPosition(), res.getPosition());
        assertEquals(playerPutDTO.getSongCard(), res.getSongCard());
        assertEquals("Kool Thing", res.getSongCard().getTitle());
        assertEquals("Sonic Youth", res.getSongCard().getArtist());
        assertEquals(1990, res.getSongCard().getYear());
    }

    @Test
    public void test_convertPlayerPutDTOtoEntity_failure() {
        PlayerPutDTO playerPutDTO = new PlayerPutDTO();
        playerPutDTO.setAddCoin(false);
        playerPutDTO.setPosition(0);
        playerPutDTO.setSongCard(null);

        PlayerPutDTO res = DTOMapper.INSTANCE.convertPlayerPutDTOtoEntity(playerPutDTO);

        assertNotNull(res);
        assertFalse(res.getAddCoin());
        assertEquals(0, res.getPosition());
        assertNull(res.getSongCard());
    }

    @Test
    public void test_convertSongCardGetDTOtoEntity_success() {
        SongCardGetDTO songCardGetDTO = new SongCardGetDTO();
        songCardGetDTO.setTitle("Theresa's Sound-World");
        songCardGetDTO.setArtist("Sonic Youth");
        songCardGetDTO.setYear(1992);
        songCardGetDTO.setSongURL("https://example.com");

        SongCard songCard = DTOMapper.INSTANCE.convertSongCardGetDTOtoEntity(songCardGetDTO);

        assertEquals(songCardGetDTO.getTitle(), songCard.getTitle());
        assertEquals(songCardGetDTO.getArtist(), songCard.getArtist());
        assertEquals(songCardGetDTO.getYear(), songCard.getYear());
        assertEquals(songCardGetDTO.getSongURL(), songCard.getSongURL());
    }

    @Test
    public void test_convertSongCardGetDTOtoEntity_failure() {
        SongCardGetDTO songCardGetDTO = new SongCardGetDTO();
        songCardGetDTO.setTitle(null);
        songCardGetDTO.setArtist(null);
        songCardGetDTO.setYear(0);
        songCardGetDTO.setSongURL(null);

        SongCard songCard = DTOMapper.INSTANCE.convertSongCardGetDTOtoEntity(songCardGetDTO);

        assertNotNull(songCard);
        assertNull(songCard.getTitle());
        assertNull(songCard.getArtist());
        assertEquals(0, songCard.getYear());
        assertNull(songCard.getSongURL());
    }

    @Test
    public void test_convertEntityToSongCardGetDTO_success() {
        SongCard songCard = new SongCard();
        songCard.setTitle("Tom Violence");
        songCard.setArtist("Sonic Youth");
        songCard.setYear(1986);
        songCard.setSongURL("https://example.com");

        SongCardGetDTO songCardGetDTO = DTOMapper.INSTANCE.convertEntityToSongCardGetDTO(songCard);

        assertEquals(songCard.getTitle(), songCardGetDTO.getTitle());
        assertEquals(songCard.getArtist(), songCardGetDTO.getArtist());
        assertEquals(songCard.getYear(), songCardGetDTO.getYear());
        assertEquals(songCard.getSongURL(), songCardGetDTO.getSongURL());
    }

    @Test
    public void test_convertEntityToSongCardGetDTO_failure() {
        SongCard songCard = new SongCard();
        songCard.setTitle(null);
        songCard.setArtist(null);
        songCard.setYear(0);
        songCard.setSongURL(null);

        SongCardGetDTO songCardGetDTO = DTOMapper.INSTANCE.convertEntityToSongCardGetDTO(songCard);

        assertNotNull(songCardGetDTO);
        assertNull(songCardGetDTO.getTitle());
        assertNull(songCardGetDTO.getArtist());
        assertEquals(0, songCardGetDTO.getYear());
        assertNull(songCardGetDTO.getSongURL());
    }

    @Test
    public void test_convertEntitytoGameGetDTO_success() {
        Game game = new Game();
        game.setGameId(1L);
        game.setGameName("testGame");
        game.setTurnCount(3);

        Player host = new Player();
        host.setUserId(1L);
        host.setUsername("testHostPlayer");
        game.setHost(host);

        Player player1 = new Player();
        player1.setUserId(2L);
        player1.setUsername("Player1");

        Player player2 = new Player();
        player2.setUserId(3L);
        player2.setUsername("Player2");

        List<Player> players = Arrays.asList(host, player1, player2);
        game.setPlayers(players);

        Queue<Player> turnOrder = new LinkedList<>(players);
        game.setTurnOrder(turnOrder);

        Round round = new Round();
        round.setRoundNr(1);
        game.setCurrentRound(round);

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);

        assertEquals(game.getGameId(), gameGetDTO.getGameId());
        assertEquals(game.getGameName(), gameGetDTO.getGameName());
        assertEquals(game.getTurnCount(), gameGetDTO.getTurnCount());
        assertEquals(game.getHost(), gameGetDTO.getHost());
        assertEquals(game.getPlayers(), gameGetDTO.getPlayers());
        assertEquals(game.getTurnOrder(), gameGetDTO.getTurnOrder());
        assertEquals(game.getCurrentRound(), gameGetDTO.getCurrentRound());
    }

    @Test
    public void test_convertEntitytoGameGetDTO_failure() {
        Game game = new Game();
        game.setGameId(null);
        game.setGameName(null);
        game.setTurnCount(0);
        game.setHost(null);
        game.setPlayers(null);
        game.setTurnOrder(null);
        game.setCurrentRound(null);

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);

        assertNotNull(gameGetDTO);
        assertNull(gameGetDTO.getGameId());
        assertNull(gameGetDTO.getGameName());
        assertEquals(0, gameGetDTO.getTurnCount());
        assertNull(gameGetDTO.getHost());
        assertNull(gameGetDTO.getPlayers());
        assertNull(gameGetDTO.getTurnOrder());
        assertNull(gameGetDTO.getCurrentRound());
    }

    @Test
    public void test_convertGuessPostDTOtoEntity_success() {
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGuessedTitle("  Schizophrenia  ");
        guessPostDTO.setGuessedArtist("  Sonic Youth  ");

        Player player = new Player();
        player.setUserId(1L);
        player.setUsername("testPlayer");
        guessPostDTO.setPlayer(player);

        Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);

        assertEquals("Schizophrenia", guess.getGuessedTitle());
        assertEquals("Sonic Youth", guess.getGuessedArtist());
        assertEquals(player, guess.getPlayer());
    }

    @Test
    public void test_convertGuessPostDTOtoEntity_failure() {
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGuessedTitle(null);
        guessPostDTO.setGuessedArtist(null);
        guessPostDTO.setPlayer(null);

        Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);

        assertNotNull(guess);
        assertNull(guess.getGuessedTitle());
        assertNull(guess.getGuessedArtist());
        assertNull(guess.getPlayer());
    }

}

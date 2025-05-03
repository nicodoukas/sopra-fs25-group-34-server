package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;


import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    private final UserService userService;

    private final WebSocketMessenger webSocketMessenger;

    LobbyController(LobbyService lobbyService, UserService userService, WebSocketMessenger webSocketMessenger) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.webSocketMessenger = webSocketMessenger;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO LobbyPostDTO) {

        Lobby userInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(LobbyPostDTO);

        Lobby createdLobby = lobbyService.createLobby(userInput);

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable Long lobbyId) {

        Lobby lobby = lobbyService.getLobbyById(lobbyId);

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllMembers(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);

        // fetch all users in the internal representation
        List<User> users = lobbyService.getMembers(lobby);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/lobbies/invite/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO inviteUser(@PathVariable Long userId, @RequestBody Long lobbyId) {
        User user = userService.getUserById(userId);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(lobbyService.inviteUserToLobby(user, lobbyId));
    }

    @PostMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO manageLobbyRequest(@PathVariable Long lobbyId, @RequestBody Map<String, Object> RequestBody) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Long userId = Long.parseLong(RequestBody.get("userId").toString());
        Boolean accepted = (Boolean) RequestBody.get("accepted");

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobbyService.manageLobbyRequest(lobby, userId, accepted));
    }

    @DeleteMapping("/lobbies/{lobbyId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable Long lobbyId, @PathVariable Long userId) {
        lobbyService.leaveOrDeleteLobby(lobbyId, userId);
    }
    /*
    @MessageMapping("/delete")
    public void deleteLobby(String lobbyId) {
        System.out.println("Backend: Deleting lobby with ID " + lobbyId);
        webSocketMessenger.sendMessage("/games/" + lobbyId, "delete-lobby", null);
    }
    */
}
package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    private final UserService userService;

    LobbyController(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
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
        System.out.println("pvar userId: " + userId);
        System.out.println("reqBody lobbyId: " + lobbyId);
        User user = userService.getUserById(userId);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        System.out.println("user is now: " + user);
        System.out.println("lobby is now: " + lobby);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(lobbyService.inviteUserToLobby(user,lobbyId));
    }


}
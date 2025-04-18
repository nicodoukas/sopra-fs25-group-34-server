package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.LobbyStorage;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class LobbyService {
    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyStorage lobbyStorage;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public LobbyService(@Qualifier("lobbyStorage") LobbyStorage lobbyStorage, UserRepository userRepository, UserService userService) {
        this.lobbyStorage = lobbyStorage;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Lobby createLobby(Lobby newLobby) {
        newLobby.createLobbyId(); //generate the lobbyID with the IDCounter

        newLobby = lobbyStorage.addLobby(newLobby); //stores the Lobby in the LobbyStorage
        log.debug("Created Information for User: {}", newLobby);
        return newLobby;
    }

    public Lobby getLobbyById(Long lobbyId) {
        return lobbyStorage.getLobbyById(lobbyId);
    }

    public List<User> getMembers(Lobby lobby) {
        return lobby.getMembers();
    }

    public User inviteUserToLobby(User user, Long lobbyId) { // now saving lobbyId and not lobby object
        user.addLobbyInvitation(lobbyId);
        // System.out.println("lobbyService: addLobbyInvitation works");

        userRepository.save(user);
        userRepository.flush();
        // System.out.println("userRepo works");
        return user;
    }

    public Lobby manageLobbyRequest(Lobby lobby, Long userId, Boolean accepted){
        User user=userService.getUserById(userId);
        // if accepted add lobbyId to user instance, delete invitation from openLobbyInvitation and add user as member to lobby instance
        if (accepted){
            user.acceptLobbyInvitation(lobby.getLobbyId());
            lobby.joinLobby(user);
        }
        // if declined remove openLobbyInvitation with lobbyId
        else{
            user.declineLobbyInvitation(lobby.getLobbyId());
        }

        // save and persist user
        userRepository.save(user);
        userRepository.flush();
        System.out.println(lobby);
        return lobby;
    }

}

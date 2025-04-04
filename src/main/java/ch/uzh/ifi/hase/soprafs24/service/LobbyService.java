package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.LobbyStorage;
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

    @Autowired
    public LobbyService(@Qualifier("lobbyStorage") LobbyStorage lobbyStorage, UserRepository userRepository) {
        this.lobbyStorage = lobbyStorage;
        this.userRepository = userRepository;
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

    public User inviteUserToLobby(User user, Lobby lobby) {
        user.addLobbyInvitation(lobby);

        userRepository.save(user);
        userRepository.flush();
        return user;
    }


}

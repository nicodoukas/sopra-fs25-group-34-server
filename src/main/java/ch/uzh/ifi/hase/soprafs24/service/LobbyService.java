package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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

    @Autowired
    public LobbyService(@Qualifier("lobbyStorage") LobbyStorage lobbyStorage) {
        this.lobbyStorage = lobbyStorage;
    }

    public Lobby createLobby(Lobby newLobby) {
        newLobby = lobbyStorage.addLobby(newLobby); //stores the Lobby in the LobbyStorage

        log.debug("Created Information for User: {}", newLobby);
        return newLobby;
    }

    public Lobby getLobbyById(Long lobbyId) {
        return lobbyStorage.getLobbyBId(lobbyId);
    }

    public List<User> getMembers(Lobby lobby) {
        return lobby.getMembers();
    }



}

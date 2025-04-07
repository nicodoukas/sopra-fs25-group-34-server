package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;

public class LobbyPostDTO {
    private Long lobbyId;
    private String lobbyName;
    private UserGetDTO host;

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setHost(UserGetDTO host) {
        this.host = host;
    }

    public UserGetDTO getHost() {
        return host;
    }
}
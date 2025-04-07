package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;

import java.util.List;

public class LobbyGetDTO {
    private Long lobbyId;
    private String lobbyName;
    private List<UserGetDTO> members;
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

    public void setMembers(List<UserGetDTO> members) {
        this.members = members;
    }

    public List<UserGetDTO> getMembers() {
        return members;
    }

    public void setHost(UserGetDTO host) {
        this.host = host;
    }

    public UserGetDTO getHost() {
        return host;
    }
    
}
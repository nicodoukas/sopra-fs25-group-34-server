package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UserService userService;

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column
    @ElementCollection
    private List<User> members;

    @Column(nullable = false)
    private Long host;


    public Lobby(UserService userService, Long host) {
        this.userService = userService;
        setHost(host);
        setMembers(new ArrayList<User>());
        joinLobby(host);
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    private void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public List<User> getMembers() {
        return members;
    }

    private void setMembers(List<User> members) {
        this.members = members;
    }

    public Long getHost() {
        return host;
    }

    private void setHost(Long host) {
        this.host = host;
    }


    public void joinLobby(Long userId) {
        members.add(userService.getUserById(userId));
    }

}
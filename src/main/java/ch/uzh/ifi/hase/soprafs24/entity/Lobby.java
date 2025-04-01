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

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column(nullable = false)
    private String lobbyName;

    @Column
    @ElementCollection
    private List<User> members;

    @Column(nullable = false)
    private User host;


    public Lobby(User host, String lobbyName) {
        setHost(host);
        setMembers(new ArrayList<User>());
        joinLobby(host);
        setLobbyName(lobbyName);
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    private void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    private void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public List<User> getMembers() {
        return members;
    }

    private void setMembers(List<User> members) {
        this.members = members;
    }

    public User getHost() {
        return host;
    }

    private void setHost(User host) {
        this.host = host;
    }


    public void joinLobby(User user) {
        members.add(user);
    }

}
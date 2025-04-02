package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.*;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final AtomicLong IDCounter = new AtomicLong(1); //multi-thread safe ID

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column(nullable = false)
    private String lobbyName;

    @OneToMany //relationship
    private List<User> members;

    @OneToOne //relationship
    private User host;


    public Lobby() {
        setMembers(new ArrayList<User>());
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public void createLobbyId() {
        this.lobbyId = IDCounter.getAndIncrement();
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
        joinLobby(host);
    }


    public void joinLobby(User user) {
        members.add(user);
    }

}
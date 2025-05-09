package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class Lobby implements Serializable {

    private static final AtomicLong IDCounter = new AtomicLong(1); //multi-thread safe ID

    private static final long serialVersionUID = 1L;

    private Long lobbyId;

    private String lobbyName;

    private List<User> members;

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
        members.add(user);user.setLobbyId(this.lobbyId);
    }

    @Override
    public String toString() {
        return "Lobby{id=" + lobbyId + ", lobbyname='" + lobbyName + "', members='" + members + "}";
    }

    public void leaveLobby(User user) {members.removeIf(member -> member.getId().equals(user.getId()));} //makes sure also is removed if something in user changes

}

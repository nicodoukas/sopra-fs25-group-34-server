package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;


import java.util.Date;
import java.util.List;

public class UserGetDTO {

  private Long id;
  private String username;
  private String password;
  private Date creation_date;
  private Date birthday;
  private UserStatus status;
  private List<Long> friends;
  private List<Long> friendrequests;
  private List<LobbyGetDTO> openLobbyInvitations;

  public Long getId() {  return id; }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) { this.password = password; }

  public String getPassword() { return password; }

  public void setCreation_date(Date creation_date) { this.creation_date = creation_date; }

  public Date getCreation_date() { return creation_date;}

  public void setBirthday(Date birthday) {this.birthday = birthday;}

  public Date getBirthday() { return birthday; }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public List<Long> getFriends(){
    return friends;
  }

  public void setFriends(List<Long> friends){
    this.friends = friends;
  }

  public List<Long> getFriendrequests(){
    return friendrequests;
  }

  public void setFriendrequests(List<Long> friendrequests){
    this.friendrequests = friendrequests;
  }

  public List<LobbyGetDTO> getOpenLobbyInvitations(){
    return openLobbyInvitations;
  }

  public void setOpenLobbyInvitations(List<LobbyGetDTO> openLobbyInvitations){
    this.openLobbyInvitations = openLobbyInvitations;
  }
  
}

package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;


import java.util.Date;

public class UserGetDTO {

  private Long id;
  private String username;
  private String password;
  private Date creation_date;
  private Date birthday;
  private UserStatus status;

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

  public void setCreation_date(Date creationdate) {this.creation_date = creation_date;}

  public Date getCreation_date() { return creation_date;}

  public void setBirthday(Date birthday) {this.birthday = birthday;}

  public Date getBirthday() { return birthday; }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }
}

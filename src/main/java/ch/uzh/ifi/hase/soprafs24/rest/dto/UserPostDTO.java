package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.util.Date;

public class UserPostDTO {

  private String username;

  private String password;

  private Date creationdate;

  private Date birthday;

  private String token;

  private Long id;

  private UserStatus status;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {this.password = password;}

  public String getPassword() { return password;}

  public void setCreationdate(Date creationdate) { this.creationdate= creationdate;}

  public Date getCreationdate() { return creationdate;}

  public void setBirthday(Date birthday) { this.birthday = birthday;}

  public Date getBirthday() { return birthday;}

  public Long getId() {
        return id;
    }

  public void setId(Long id) {
        this.id = id;
    }

  public UserStatus getStatus() {
        return status;
    }

  public void setStatus(UserStatus status) {
        this.status = status;
    }

  public void setToken(String token) { this.token = token;}

  public String getToken() { return token;}
}

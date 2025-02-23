package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public class UserGetDTO {

  private Long id;
  private String name;
  private String username;
  private String password;
  private LocalDate creationdate;
  private UserStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) { this.password = password; }

  public String getPassword() { return password; }

  public void setCreationdate(LocalDate creationdate) {this.creationdate = creationdate;}

  public LocalDate getCreationdate() { return creationdate;}

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }
}

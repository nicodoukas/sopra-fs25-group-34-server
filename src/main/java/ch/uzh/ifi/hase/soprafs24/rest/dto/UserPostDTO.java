package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.time.LocalDate;

public class UserPostDTO {

  private String username;

  private String password;

  private LocalDate creationdate;

  private LocalDate birthday;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {this.password = password;}

  public String getPassword() { return password;}

  public void setCreationdate(LocalDate creationdate) { this.creationdate= creationdate;}

  public LocalDate getCreationdate() { return creationdate;}

  public void setBirthday(LocalDate birthday) { this.birthday = birthday;}

  public LocalDate getBirthday() { return birthday;}
}

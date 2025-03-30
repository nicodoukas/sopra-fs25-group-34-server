package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column(nullable = false)
  private Date creation_date;

  @Column
  private Date birthday;

  @Column
  @ElementCollection
  private List<Long> friends;

  @Column
  @ElementCollection
  private List<Long> friendrequests;

  public User() {
    this.friends = new ArrayList<Long>();
    this.friendrequests = new ArrayList<Long>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) { this.password = password;}

  public String getPassword() { return password;}

  public void setCreation_date(Date creation_date) {this.creation_date = creation_date;}

  public Date getCreation_date() { return creation_date;}

  public void setBirthday(Date birthday) {this.birthday = birthday;}

  public Date getBirthday() { return birthday;}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public List<Long> getFriends(){
    return friends;
  }

  public void setFriends(Long userid){
    if (!this.friendrequests.contains(userid)){
      this.friends.add(userid);
    }
  }

  public void removeFriend(Long userid){
    this.friends.remove(userid);
  }

  public List<Long> getFriendrequests(){
    return friendrequests;
  }

  public void setFriendrequests(Long userid){
    if (!this.friendrequests.contains(userid) && !this.friends.contains(userid)){
      this.friendrequests.add(userid);
    }

  }

  public void declineFriendRequest(Long userid){
    this.friendrequests.remove(userid);
  }

    @Override
    public String toString() {
      return "User{id=" + id + ", username='" + username + "', password='" + password + "', status=" + status + ", creation_date=" + creation_date + "}";
    }
}

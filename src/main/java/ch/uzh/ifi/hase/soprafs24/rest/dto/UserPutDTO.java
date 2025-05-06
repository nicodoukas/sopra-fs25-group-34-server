package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.ProfilePicture;

import java.util.Date;

public class UserPutDTO {

    private String username;

    private Date birthday;

    private Long id;

    private String description;

    private ProfilePicture profilePicture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description;}

    public void setProfilePicture(ProfilePicture profilePicture) {this.profilePicture = profilePicture;}
    public ProfilePicture getProfilePicture() {return profilePicture;}
}

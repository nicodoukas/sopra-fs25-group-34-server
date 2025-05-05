package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

@Entity
public class ProfilePicture {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 512)
    private String url;

    public ProfilePicture() {}

    public ProfilePicture(String url) {this.url = url;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

}

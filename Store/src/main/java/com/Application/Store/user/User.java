package com.Application.Store.user;

import org.springframework.data.annotation.Id;
// import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("User")
public class User {
    // priperties
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String profilePhoto;
    private String theme;

    // constructors
    public User() {
    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getProfilePhoto() {
        return this.profilePhoto;
    }

    public String getTheme() {
        return this.theme;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String toString() {
        return "id:" + this.id + "\n" +
                "name:" + this.name + "\n" +
                "email:" + this.email + "\n" +
                "password:" + this.password + "\n" +
                "photo:" + this.profilePhoto + "\n";
    }
}

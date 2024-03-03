package com.Application.Store.team;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.Application.Store.user.User;

public class Team {
    @Id
    String id;
    String name;
    String photo;
    List<User> members;

    // constructors
    public Team() {
    }

    // getters
    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoto() {
        return this.photo;
    }

    public List<User> getMembers() {
        return this.members;
    }

    // setters
    public void setTeamPhoto(String photo) {
        this.photo = photo;
    }

}

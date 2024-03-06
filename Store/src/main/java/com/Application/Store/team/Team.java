package com.Application.Store.team;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Application.Store.user.User;

@Document("Team")
public class Team {
    @Id
    String id;
    String name;
    String photo;
    List<User> members;
    User owner;

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

    public User getOwner() {
        return this.owner;
    }

    // setters
    public void setTeamPhoto(String photo) {
        this.photo = photo;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

}

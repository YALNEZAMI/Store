package com.Application.Store.project;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Application.Store.user.User;

@Document("Project")
public class Project {
    // priperties
    @Id
    private String id;
    private String name;
    private String description;
    private String deadLine;
    private ArrayList<User> participants;
    private User owner;
    private String photo;

    // constructors
    public Project() {

    }

    public Project(String id, String name) {
        this.id = id;
        this.name = name;

    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDeadLine() {
        return this.deadLine;
    }

    public User getOwner() {
        return this.owner;
    }

    public String getPhoto() {
        return this.photo;
    }

    public ArrayList<User> getParticipants() {
        return this.participants;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProjectPhoto(String photo) {
        this.photo = photo;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }
}

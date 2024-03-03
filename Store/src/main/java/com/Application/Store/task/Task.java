package com.Application.Store.task;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
// import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Application.Store.user.User;

@Document("Task")
public class Task {
    // priperties
    @Id
    private String id;
    private String name;
    private String project_id;
    private String description;
    private String deadLine;
    private ArrayList<User> participants;
    private int order;
    private String photo;

    // constructors
    public Task() {

    }

    public Task(String id, String name, String email, String password) {
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

    public String getProjectId() {
        return this.project_id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDeadLine() {
        return this.deadLine;
    }

    public String getPhoto() {
        return this.photo;
    }

    public int getOrder() {
        return this.order;
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

}

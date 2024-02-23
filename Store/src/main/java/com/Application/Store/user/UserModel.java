package com.Application.Store.user;

// import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserModel")
public class UserModel {
    // priperties
    private String _id;
    private String name;
    private String email;
    private String password;

    // constructors
    public UserModel() {
    }

    public UserModel(String _id, String name, String email, String password) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getId() {
        return this._id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

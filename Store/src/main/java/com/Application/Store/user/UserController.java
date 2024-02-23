package com.Application.Store.user;

import java.util.List;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getUsers() {
        List<UserModel> users = this.userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserModel> getUser(@PathVariable String id) {
        UserModel user = this.userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserModel> register(@RequestBody UserModel entity) {
        UserModel saving = this.userService.register(entity);
        // hash password
        return new ResponseEntity<>(saving, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<UserModel> login(@RequestBody UserModel entity) {
        UserModel saving = this.userService.login(entity);
        // hash password
        return new ResponseEntity<>(saving, HttpStatus.OK);
    }

}

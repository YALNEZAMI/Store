package com.Application.Toog.user;

import java.util.List;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        User user = this.userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("isAvailableName")
    public ResponseEntity<Boolean> isAvailableName(@RequestParam String name) {
        boolean isAvailableName = this.userService.isAvailableName(name);
        return new ResponseEntity<>(isAvailableName, HttpStatus.OK);
    }

    @GetMapping("searchByName/{key}")
    public ResponseEntity<List<User>> searchByName(@PathVariable String key) {
        List<User> users = this.userService.searchByName(key);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody User entity) {
        User saving = this.userService.register(entity);
        // hash password
        return new ResponseEntity<>(saving, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User saving = this.userService.login(user);
        // hash password
        return new ResponseEntity<>(saving, HttpStatus.OK);
    }

    @PostMapping("/uploadProfilePhoto")
    public User uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        User user = this.userService.uploadProfilePhoto(file, userId);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User updatedUser = this.userService.updateUser(user);
        return updatedUser;
    }
}

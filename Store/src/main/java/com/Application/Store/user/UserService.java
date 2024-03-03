package com.Application.Store.user;

import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Application.Store.Hash;

@Service
public class UserService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    UserRepo userRepo;

    public User register(User user) {
        // short password
        if (user.getPassword().length() < 6) {
            return null;
        }
        // make the email small letter
        user.setEmail(user.getEmail().toLowerCase());
        // check email existance
        User userEmail = this.userRepo.findByEmail(user.getEmail());
        if (userEmail != null) {
            return null;
        }
        // check if the user name is available
        if (!this.isAvailableName(user.getName())) {
            return null;
        }
        // hash password
        user.setPassword(Hash.hashPassword(user.getPassword()));
        User registeredUser = this.userRepo.save(user);
        registeredUser.setPassword("");

        return registeredUser;
    }

    // login method
    public User login(User user) {
        try {
            // make the email small letter
            user.setEmail(user.getEmail().toLowerCase());
            User userEmail = this.userRepo.findByEmail(user.getEmail());
            User userName = this.userRepo.findByName(user.getEmail());

            // email does not exist
            if (userEmail == null) {
                // name does not exist
                if (userName == null) {
                    return null;
                } else {// if the user provided the right name, deal with the resulting user
                    userEmail = userName;
                }
            }
            // email or name exist and password match
            if (Hash.checkPassword(user.getPassword(), userEmail.getPassword())) {
                userEmail.setPassword("");
                return userEmail;
            } else {// email exist but wrong password
                return null;

            }
        } catch (Exception e) {
            return null;
        }
    }

    // get all users
    public List<User> getUsers() {
        return this.userRepo.findAll();
    };

    // get a user according to an id
    public User getUser(String id) {
        Optional<User> optionalUser = this.userRepo.findById(id);
        return optionalUser.orElse(null); // Return null if user not found, handle as needed
    };

    // check if a user name is alreday used
    public boolean isAvailableName(String name) {
        // check if name is empty
        if (name == "") {
            return false;
        }
        User user = this.userRepo.findByName(name);
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    public User uploadProfilePhoto(MultipartFile file, String userId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
            String UPLOAD_FOLDER = "./Store/src/main/resources/static/profilePhoto/"; // Change this to
            Path directory = Paths.get(UPLOAD_FOLDER);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            String ext = "." + split[split.length - 1];

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + userId + ext);
            Files.write(path, bytes);
            // update the user photo url in the data base
            User userToUpdate = this.userRepo.findById(userId).orElse(null);
            userToUpdate.setProfilePhoto(this.API_URL + "/profilePhoto/" + userId + ext);
            this.userRepo.save(userToUpdate);
            return userToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User updateUser(User userToUpdate) {

        User currentUser = this.getUser(userToUpdate.getId());
        // update email
        currentUser.setEmail(userToUpdate.getEmail());
        // updating name
        if (userToUpdate.getName() != currentUser.getName()) {
            // check availablity of new name
            if (this.isAvailableName(userToUpdate.getName())) {
                currentUser.setName(userToUpdate.getName());
            }
        }
        // update password
        if (userToUpdate.getPassword() != "" && userToUpdate.getPassword().length() >= 6) {
            currentUser.setPassword(userToUpdate.getPassword());
            // hash password
            currentUser.setPassword(Hash.hashPassword(userToUpdate.getPassword()));
        }
        // update theme
        currentUser.setTheme(userToUpdate.getTheme());
        User userUpdated = this.userRepo.save(currentUser);
        userUpdated.setPassword("");
        return userUpdated;

    }

    public List<User> searchByName(String key) {
        List<User> users = this.userRepo.findByNameContaining(key);
        return users;
    }
}

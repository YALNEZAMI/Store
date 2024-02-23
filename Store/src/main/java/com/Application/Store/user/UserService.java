package com.Application.Store.user;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Application.Store.Hash;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public UserModel register(UserModel user) {
        // check email existance
        UserModel userEmail = this.userRepo.findByEmail(user.getEmail());
        if (userEmail != null) {
            return null;
        }
        // hash password
        user.setPassword(Hash.hashPassword(user.getPassword()));
        UserModel saving = this.userRepo.save(user);
        return saving;
    }

    // login method
    public UserModel login(UserModel user) {
        try {
            UserModel userEmail = this.userRepo.findByEmail(user.getEmail());
            // email does not exist
            if (userEmail == null) {
                System.out.println("email does not exist");
                return null;
            } else {
                // email exist and password match
                if (Hash.checkPassword(user.getPassword(), userEmail.getPassword())) {
                    System.out.println("goooooooooooooooooooooooooooooooooooooooooooooooooood");
                    return userEmail;
                } else {// email exist but wrong password
                    System.out.println("email exist but wrong password");
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }

        // // check email existance
        // UserModel userEmail = this.userRepo.findByEmail(user.getEmail());
        // if (userEmail == null) {
        // return null;
        // } else {
        // System.out.println(userEmail);
        // // hash password
        // user.setPassword(Hash.hashPassword(user.getPassword()));
        // UserModel saving = this.userRepo.save(user);
        // return saving;
        // }

    }

    public List<UserModel> getUsers() {
        return this.userRepo.findAll();
    };

    public UserModel getUser(String _id) {
        Optional<UserModel> optionalUser = this.userRepo.findById(_id);
        return optionalUser.orElse(null); // Return null if user not found, handle as needed
    };

}

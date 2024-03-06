package com.Application.Toog.user;

import java.util.List;

// import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    // other crud methods
    User findByEmail(String email);

    User findByName(String name);

    List<User> findByNameContaining(String name);

    public long count();
}

package com.Application.Store.user;

// import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<UserModel, String> {
    // other crud methods
    UserModel findByEmail(String email);

    public long count();
}

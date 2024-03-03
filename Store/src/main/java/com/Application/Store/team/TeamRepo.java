package com.Application.Store.team;

import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends MongoRepository<Team, String> {

}

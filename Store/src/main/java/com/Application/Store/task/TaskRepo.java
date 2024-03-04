package com.Application.Store.task;

import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepo extends MongoRepository<Task, String> {
    List<Task> findByProjectId(String projectId);
}

package com.Application.Store.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    TaskRepo taskRepo;

    public Task create(Task task) {
        return this.taskRepo.save(task);
    }

}

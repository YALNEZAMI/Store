package com.Application.Store.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Application.Store.project.Project;

@Service
public class TaskService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    TaskRepo taskRepo;

    public Task create(Task task) {
        return this.taskRepo.save(task);
    }

    public Task uploadTaskPhoto(MultipartFile file, String taskId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
            String UPLOAD_FOLDER = "./Store/src/main/resources/static/taskPhoto/"; // Change this to
            Path directory = Paths.get(UPLOAD_FOLDER);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            String ext = "." + split[split.length - 1];

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + taskId + ext);
            Files.write(path, bytes);
            // update the user photo url in the data base
            Task taskToUpdate = this.taskRepo.findById(taskId).orElse(null);
            taskToUpdate.setTaskPhoto(this.API_URL + "/taskPhoto/" + taskId + ext);
            this.taskRepo.save(taskToUpdate);
            return taskToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

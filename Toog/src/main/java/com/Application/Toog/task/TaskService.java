package com.Application.Toog.task;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Application.Toog.user.User;
import com.Application.Toog.user.UserService;

@Service
public class TaskService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    TaskRepo taskRepo;
    @Autowired
    UserService userService;

    public Task create(Task task) {
        return this.taskRepo.save(task);
    }

    public Task uploadTaskPhoto(MultipartFile file, String taskId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
            String UPLOAD_FOLDER = "./Toog/src/main/resources/static/taskPhoto/"; // Change this to
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

    public List<Task> getWithMemberId(String participantId) {
        List<Task> tasks = this.taskRepo.findAll();
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            for (User user : task.getParticipants()) {
                if (user.getId().equals(participantId)) {
                    result.add(task);
                }
            }
        }
        return result;
    }

    public List<Task> getTasksByProjectId(String projectId) {
        return this.taskRepo.findByProjectId(projectId);
    }

    public Task getTaskById(String id) {
        Task task = this.taskRepo.findById(id).orElse(null);
        ArrayList<User> participants = new ArrayList<>();
        for (User user : task.getParticipants()) {
            participants.add(this.userService.getUser(user.getId()));
        }
        task.setParticipants(participants);

        return task;
    }

    public void deleteTaskById(String id) {
        this.taskRepo.deleteById(id);
    }

    public void deleteProjectTasks(String projectId) {
        // delete photos related
        List<Task> tasks = this.getTasksByProjectId(projectId);
        for (Task task : tasks) {
            String photoUrl = task.getPhoto();
            String[] photoUrlSplit = photoUrl.split("/");
            String photoName = photoUrlSplit[photoUrlSplit.length - 1];
            String path = "./Toog/src/main/resources/static/taskPhoto/" + photoName;
            File file = new File(path);
            System.out.println(photoName != "default_task.png");
            // Check if the file exists
            if (file.exists() && photoName != "default_task.png") {
                System.out.println("photoName" + photoName);
                // Attempt to delete the file
                if (file.delete()) {
                    System.out.println("File deleted successfully.");
                } else {
                    System.err.println("Failed to delete the file.");
                }
            } else {
                System.err.println("File does not exist.");
            }
        }
        // delete tasks from database
        this.taskRepo.deleteByProjectId(projectId);
    }
}

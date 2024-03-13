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

import com.Application.Toog.project.Project;
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
    String UPLOAD_FOLDER = "./Toog/src/main/resources/static/taskPhoto/"; // Change this to

    public Task create(Task task) {
        return this.taskRepo.save(task);
    }

    public Task uploadTaskPhoto(MultipartFile file, String taskId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
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
            // delete the old file
            this.deletePhoto(taskToUpdate);
            // set new photo url
            taskToUpdate.setTaskPhoto(this.API_URL + "/taskPhoto/" + taskId + ext);
            this.taskRepo.save(taskToUpdate);
            return taskToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePhoto(Task task) {
        String oldPhotoUrl = task.getPhoto();
        String[] oldPhotoUrlSplit = oldPhotoUrl.split("/");
        String oldPhotoPath = UPLOAD_FOLDER + oldPhotoUrlSplit[oldPhotoUrlSplit.length - 1];
        File fileToDelete = new File(oldPhotoPath);
        // Check if the file exists before attempting to delete it
        if (fileToDelete.exists()) {
            // Attempt to delete the file
            boolean deleted = fileToDelete.delete();

            // Check if the deletion was successful
            if (deleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
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
        Task task = this.getTaskById(id);
        this.deletePhoto(task);
        this.taskRepo.deleteById(id);
    }

    public void deleteProjectTasks(String projectId) {
        // delete photos related
        List<Task> tasks = this.getTasksByProjectId(projectId);
        for (Task task : tasks) {
            this.deleteTaskById(task.getId());
        }

    }
}
